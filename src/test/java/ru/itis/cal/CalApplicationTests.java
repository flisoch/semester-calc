package ru.itis.cal;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itis.cal.domain.CalendarEvent;
import ru.itis.cal.dto.CalendarEventDto;
import ru.itis.cal.dto.ClassDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class CalApplicationTests {


	@Test
	void contextLoads() {
		CalendarEventDto calendarEvent = CalendarEventDto.builder()
				.startTime(LocalDateTime.parse("2020-09-13T11:30").plusNanos(1))
				.build();

		DateTime startDateTime = new DateTime(calendarEvent.getStartTime().toString());
		EventDateTime start = new EventDateTime()
				.setDateTime(startDateTime)
				.setTimeZone("Europe/Moscow");
		System.out.println(startDateTime);
		System.out.println(start);

	}

	@Test
	void absTo123Test() {
		String a = "A";
		int aNum = 0;
		Pattern pattern = Pattern.compile("(^.+)!(.+):(.+$)");
		Matcher matcher = pattern.matcher("name!A1:E7");
		matcher.find();
		System.out.println(matcher.group(0));
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));


	}

	@Test
	void numForChar() {
		String a = "A";
		int aNum = 0;
		System.out.println(getNumberForChar(a.charAt(0)));
	}
	private String getCharForNumber(int i) {
		return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	private int getNumberForChar(char c) {
		int num = c;
		num = num - 64;
		return num;
	}
}
