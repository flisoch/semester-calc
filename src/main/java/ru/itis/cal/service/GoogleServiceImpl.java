package ru.itis.cal.service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;
import ru.itis.cal.dto.CalendarEventDto;
import ru.itis.cal.dto.ClassDto;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleServiceImpl implements GoogleService {
    private static final String APPLICATION_NAME = "ITIS schedule Google Calendar connector";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final String CALLBACK_URI = "http://localhost:8080/google-calendar/oauth";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Override
    public List<Event> mapClassesToEvents(List<ClassDto> classes) {
        List<Event> events = new ArrayList<>();
        for (ClassDto classDto : classes) {
            Event event = new Event()
                    .setSummary(extractSummary(classDto));
            forwardEventTime(classDto, event);
            events.add(event);
        }
        return events;
    }

    private void forwardEventTime(ClassDto classDto, Event event) {
        CalendarEventDto calendarEvent = classDto.getCalendarEvent();
        DateTime startDateTime = new DateTime(calendarEvent.getStartTime().plusNanos(1).toString() + "+03:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Moscow");
        event.setStart(start);
        DateTime endDateTime = new DateTime(calendarEvent.getEndTime().plusNanos(1).toString() + "+03:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Moscow");
        event.setEnd(end);

        if (calendarEvent.getRepeatsNumber() != null) {
            String[] recurrence = new String[]{"RRULE:FREQ=WEEKLY;COUNT=" + calendarEvent.getRepeatsNumber()};
            event.setRecurrence(Arrays.asList(recurrence));
        } else if (calendarEvent.getRepeatsUntilDate() != null) {
            String[] recurrence = new String[]{"RRULE:FREQ=WEEKLY;UNTIL=" + calendarEvent.getRepeatsUntilDate()};
            event.setRecurrence(Arrays.asList(recurrence));
        }

    }

    private String extractSummary(ClassDto classDto) {
        return classDto.getSubjectName() +
                classDto.getClassroomNumber() +
                classDto.getClassType() +
                classDto.getTeacher().getLastName() +
                classDto.getTeacher().getName() +
                classDto.getTeacher().getMiddleName();
    }
}
