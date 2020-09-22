package ru.itis.cal.service;

import com.google.common.hash.HashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.cal.domain.Class;
import ru.itis.cal.domain.StudentUser;
import ru.itis.cal.dto.ClassDto;
import ru.itis.cal.repository.ScheduleClassRepository;
import ru.itis.cal.repository.StudentUserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StudentUserServiceImpl implements StudentUserService {

    StudentUserRepository studentUserRepository;
    ScheduleClassRepository classRepository;

    @Autowired
    public StudentUserServiceImpl(StudentUserRepository studentUserRepository, ScheduleClassRepository classRepository) {
        this.studentUserRepository = studentUserRepository;
        this.classRepository = classRepository;
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
        List<Class> classes = classRepository.findBySubject_electiveAndGroups_number(false, "11-701");
        studentUser.setClasses(classes);
        studentUserRepository.save(studentUser);
        return new Cookie("userId", hashCode.toString());
    }

    @Override
    @Transactional
    public ClassDto saveClassesById(Long userId, Long id) {
        Optional<Class> classOptional = classRepository.findById(id);
        Optional<StudentUser> studentUser = studentUserRepository.findById(userId);
        if (classOptional.isPresent() && studentUser.isPresent()) {
            StudentUser user = studentUser.get();
            List<Class> classes = user.getClasses();
            classes.add(classOptional.get());
            user.setClasses(classes);
            studentUserRepository.save(user);
            return ClassDto.from(classOptional.get());
        }
        return null;
    }

    @Override
    @Transactional
    public void removeClassFromUserSchedule(Long userId, Long classId) {
        Optional<Class> classOptional = classRepository.findById(classId);
        Optional<StudentUser> studentUser = studentUserRepository.findById(userId);
        if (classOptional.isPresent() && studentUser.isPresent()) {
            StudentUser user = studentUser.get();
            List<Class> classes = user.getClasses();
            classes.remove(classOptional.get());
            user.setClasses(classes);
            classOptional.get().getStudentUsers().remove(user);
            studentUserRepository.save(user);
            classRepository.save(classOptional.get());
        }
    }

    @Override
    public StudentUser getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> userId = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("userId")).findAny();
        if (userId.isPresent()) {
            Optional<StudentUser> byCookieId = studentUserRepository.findByCookieId(userId.get().getValue());
            if (byCookieId.isPresent()) {
                return byCookieId.get();
            }
        }
        return null;
    }
}
