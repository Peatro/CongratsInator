package com.peatroxd.congratsinator.congratsinator.repository;

import com.peatroxd.congratsinator.congratsinator.model.Person;
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

    @Query(
            value = """
                        SELECT * FROM Person p
                        WHERE EXTRACT(MONTH FROM p.birthday) > :currentMonth
                            OR (EXTRACT(MONTH FROM p.birthday) = :currentMonth
                                AND EXTRACT(DAY FROM p.birthday) >= :currentDay )
                        ORDER BY
                            EXTRACT(MONTH FROM p.birthday),
                            EXTRACT(DAY FROM p.birthday)
                    """,
            nativeQuery = true
    )
    List<Person> findUpcomingBirthdays(@Param("currentMonth") int currentMonth, @Param("currentDay") int currentDay);
}