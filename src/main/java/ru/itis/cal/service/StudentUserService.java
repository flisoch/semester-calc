package ru.itis.cal.service;

import ru.itis.cal.domain.StudentUser;
import ru.itis.cal.dto.ClassDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface StudentUserService {
    boolean userAuthorized(HttpServletRequest request);

    Cookie saveUserCookie();

    ClassDto saveClassesById(Long userId, Long id);

    void removeClassFromUserSchedule(Long userId, Long classId);

    StudentUser getUser(HttpServletRequest request);
}
