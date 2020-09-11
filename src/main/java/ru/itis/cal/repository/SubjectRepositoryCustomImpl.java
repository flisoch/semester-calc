package ru.itis.cal.repository;

import ru.itis.cal.dto.queryFilter.SubjectQueryFilter;
import ru.itis.cal.domain.Course;
import ru.itis.cal.domain.Subject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectRepositoryCustomImpl implements SubjectRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<Subject> findAllByFilter(SubjectQueryFilter filter) {
        filter.setGroupNumber("11-701");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Subject> criteriaQuery = cb.createQuery(Subject.class);
        Root<Subject> subject = criteriaQuery.from(Subject.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getGroupNumber() != null) {
            Join<Subject, Course> subjectCourseJoin = subject.join("course");
            Course course = em.createQuery("select c from Group g left join Course c on g.course.id = c.id where g.number like :groupNumber", Course.class)
                    .setParameter("groupNumber", filter.getGroupNumber())
                    .setMaxResults(1)
                    .getSingleResult();
            Predicate groupNumber = cb.equal(subjectCourseJoin.get("number"), course.getNumber());
            predicates.add(groupNumber);
        }

        TypedQuery<Subject> typedQuery = em.createQuery(criteriaQuery
                .select(subject)
                .where(predicates.toArray(new Predicate[]{})));
        List<Subject> resultList = typedQuery.getResultList();
        return resultList;
    }
}
