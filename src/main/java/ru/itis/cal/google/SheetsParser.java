package ru.itis.cal.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.cal.CalApplication;
import ru.itis.cal.domain.Class;
import ru.itis.cal.dto.Range;
import ru.itis.cal.service.ClassesService;
import ru.itis.cal.service.ParserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Controller
public class SheetsParser {

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    @Value("${google.spreadsheet.id}")
    private String SPREADSHEET_ID;
    private String SHEET_NAME = "раписание 1 семестр 2020";
    private static final String USER_IDENTIFIER_KEY = "MY_DUMMY_USER";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final String CALLBACK_URI = "http://localhost:8080/oauth";
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private GoogleAuthorizationCodeFlow flow;
    @Autowired
    private ParserService parserService;
    @Autowired
    private ClassesService classesService;

    @PostConstruct
    public void init() throws Exception {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(CalApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH)));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH))).build();

    }


    @GetMapping(value = { "/googlesignin" })
    public void doGoogleSignIn(HttpServletResponse response) throws Exception {
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
        response.sendRedirect(redirectURL);
    }

    @GetMapping(value = { "/oauth" })
    public String saveAuthorizationCode(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        if (code != null) {
            saveToken(code);

            return "good!";
        }

        return "token is not saved!";
    }

    private void saveToken(String code) throws Exception {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URI).execute();
        flow.createAndStoreCredential(response, USER_IDENTIFIER_KEY);

    }

    @GetMapping("/sheet")
    public ResponseEntity sheetData(@RequestParam(defaultValue = "11-701") String group) throws GeneralSecurityException, IOException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = flow.loadCredential(USER_IDENTIFIER_KEY);
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        List<List<Object>> rawClasses = parserService.getRawClasses(service, SPREADSHEET_ID, SHEET_NAME, group);
        List<Class> classes = parserService.mapToClasses(rawClasses);
        classes = classesService.saveAll(classes);
        return ResponseEntity.ok(classes);
    }

}
