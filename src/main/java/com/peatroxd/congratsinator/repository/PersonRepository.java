package com.peatroxd.congratsinator.repository;

import com.peatroxd.congratsinator.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Query(
            value = """
        SELECT * FROM person p
        WHERE EXTRACT(MONTH FROM p.birthday) = :currentMonth
          AND EXTRACT(DAY FROM p.birthday) = :currentDay
    """,
            nativeQuery = true
    )
    List<Person> findByBirthdayMonthAndBirthdayDay(
            @Param("currentMonth") int currentMonth,
            @Param("currentDay") int currentDay
    );
}