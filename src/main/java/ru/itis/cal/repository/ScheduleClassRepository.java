package ru.itis.cal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.cal.domain.Class;
import ru.itis.cal.domain.StudentUser;

import java.util.List;

@Repository
public interface ScheduleClassRepository extends JpaRepository<Class, Long> {
    List<Class> findByGroups_number(String groupNumber);
    List<Class> findBySubject_electiveAndGroups_number(Boolean isElective, String groupNumber);
}
