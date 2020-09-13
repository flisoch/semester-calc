package ru.itis.cal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.cal.domain.StudentUser;

import javax.servlet.http.Cookie;
import java.util.Optional;

@Repository
public interface StudentUserRepository extends JpaRepository<StudentUser, Long> {
    Optional<StudentUser> findByCookieId(String userId);
}
