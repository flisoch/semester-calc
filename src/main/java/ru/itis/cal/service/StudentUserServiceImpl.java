package ru.itis.cal.service;

import com.google.common.hash.HashCode;
import org.springframework.stereotype.Service;
import ru.itis.cal.domain.StudentUser;
import ru.itis.cal.repository.StudentUserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

@Service
public class StudentUserServiceImpl implements StudentUserService {

    StudentUserRepository studentUserRepository;

    public StudentUserServiceImpl(StudentUserRepository studentUserRepository) {
        this.studentUserRepository = studentUserRepository;
    }

    @Override
    public boolean userAuthorized(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> userId = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("userId")).findAny();
        if (userId.isPresent()) {
            Optional<StudentUser> byCookieId = studentUserRepository.findByCookieId(userId.get().getValue());
            return byCookieId.isPresent();
        }
        return false;
    }

    @Override
    public Cookie saveUserCookie() {
        HashCode hashCode = HashCode.fromLong(LocalDateTime.now().toEpochSecond(ZoneOffset.MIN));
        StudentUser studentUser = StudentUser.builder()
                .cookieId(hashCode.toString())
                .build();
        studentUserRepository.save(studentUser);
        return new Cookie("userId", hashCode.toString());
    }
}
