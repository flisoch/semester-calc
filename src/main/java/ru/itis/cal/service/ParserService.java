package ru.itis.cal.service;

import com.google.api.services.sheets.v4.Sheets;
import ru.itis.cal.domain.Class;

import java.io.IOException;
import java.util.List;

public interface ParserService {

    List<List<Object>> getRawClasses(Sheets service, String spreadsheetId, String sheetName, String group) throws IOException;

    List<Class> mapToClasses(List<List<Object>> rawClasses);
}
