package ru.itis.cal.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.cal.CalApplication;
import ru.itis.cal.domain.StudentUser;
import ru.itis.cal.dto.ClassDto;
import ru.itis.cal.dto.Range;
import ru.itis.cal.service.ClassesService;
import ru.itis.cal.service.GoogleService;
import ru.itis.cal.service.StudentUserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Controller
@RequestMapping(path = "/google-calendar")
public class GoogleCalendarConnector {

    private static final String APPLICATION_NAME = "ITIS schedule Google Calendar connector";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final String CALLBACK_URI = "http://localhost:8080/google-calendar/oauth";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private GoogleAuthorizationCodeFlow flow;

    @Autowired
    StudentUserService studentUserService;
    @Autowired
    ClassesService classesService;
    @Autowired
    GoogleService googleService;

    @PostConstruct
    public void init() throws Exception {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(CalApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH)));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH))).build();

    }


    @GetMapping(value = { "/signin" })
    public void doGoogleSignIn(HttpServletResponse response) throws Exception {
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
        response.sendRedirect(redirectURL);
    }

    @GetMapping(value = { "/oauth" })
    public ResponseEntity saveAuthorizationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        if (code != null) {
            if (studentUserService.userAuthorized(request)) {
                return ResponseEntity.ok("You are already authorized!");

            }
            else {
                Cookie cookie = studentUserService.saveUserCookie();
                saveToken(code, cookie.getValue());
                response.addCookie(cookie);
            }
            return (ResponseEntity) ResponseEntity.ok("token is saved, sent you a cookie! <a href=\"http://localhost:8080/google-calendar/user-events\">Continue</a>");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No code received from google oauth");
    }

    private void saveToken(String code, String userIdentifierKey) throws Exception {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URI).execute();
        flow.createAndStoreCredential(response, userIdentifierKey);

    }

    @RequestMapping
    public ResponseEntity sheetData(HttpServletRequest request, @RequestParam String groupNumber) throws IOException {
//        groupNumber = "11-701";
        Credential credentials = getCredentials(request);
        if (credentials == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("The request is missing a valid API key.\n Please authorize in <a href=\"http://localhost:8080/google-calendar/signin\">link</a>");
        }
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
        com.google.api.services.calendar.model.Calendar groupCalendar = createGroupCalendar(groupNumber);
        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(groupCalendar).execute();
        List<ClassDto> classes = classesService.getClassesBy(groupNumber);
        List<Event> events = googleService.mapClassesToEvents(classes);
        List<String> eventsHtmlLinks = new ArrayList<>();
        events.forEach(event -> {
            try {
                Event execute = service.events().insert(createdCalendar.getId(), event).execute();
                eventsHtmlLinks.add(execute.getHtmlLink());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok("<a href=\"http://localhost:8080/google-calendar/user-events?calendarId=" + createdCalendar.getId() + "\">calendarEvents</a>" + " "
                + eventsHtmlLinks.toString());
    }

    private com.google.api.services.calendar.model.Calendar createGroupCalendar(String groupNumber) {
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary("11-701 schedule");
        calendar.setTimeZone("Europe/Moscow");
        return calendar;
    }

    private Credential getCredentials(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> userId = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("userId")).findAny();
            if (userId.isPresent()) {
                return flow.loadCredential(userId.get().getValue());
            }
        }
        return null;
    }

    @GetMapping("/user-events")
    public ResponseEntity eventsInCalendar(HttpServletRequest request, @RequestParam String calendarId) throws IOException {
        String groupNumber = "11-701";
        Credential credentials = getCredentials(request);
        if (credentials == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("The request is missing a valid API key.\n Please authorize in <a href=\"http://localhost:8080/google-calendar/signin\">link</a>");
        }
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();


        Events events = service.events().list(calendarId).execute();
        List<ClassDto> classes = classesService.getClassesBy(groupNumber);
        List<Event> items = events.getItems();
        return ResponseEntity.ok(items);
    }
}
