package com.peatroxd.congratsinator.congratsinator.repository;

import com.peatroxd.congratsinator.congratsinator.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    @Query("SELECT p FROM Person p WHERE FUNCTION('MONTH', p.birthday) = :month AND FUNCTION('DAY', p.birthday) = :day")
    List<Person> findByBirthdayMonthAndBirthdayDay(@Param("month") int month, @Param("day") int day);

    @Query("""
                SELECT p FROM Person p
                WHERE (FUNCTION('MONTH', p.birthday) > :currentMonth)
                      OR (FUNCTION('MONTH', p.birthday) = :currentMonth AND FUNCTION('DAY', p.birthday) >= :currentDay)
                ORDER BY FUNCTION('MONTH', p.birthday), FUNCTION('DAY', p.birthday)
            """)
    List<Person> findUpcomingBirthdays(@Param("currentMonth") int from, @Param("currentDay") int to);
}
