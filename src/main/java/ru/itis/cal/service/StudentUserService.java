package ru.itis.cal.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface StudentUserService {
    boolean userAuthorized(HttpServletRequest request);

    Cookie saveUserCookie();
}
