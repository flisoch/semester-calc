package ru.itis.cal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.cal.domain.Class;

import java.util.List;

@Repository
public interface ScheduleClassRepository extends JpaRepository<Class, Long> {
    List<Class> findByGroups_number(String groupNumber);
}
