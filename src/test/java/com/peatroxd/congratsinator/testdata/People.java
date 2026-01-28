package com.peatroxd.congratsinator.testdata;

import com.peatroxd.congratsinator.model.Person;

import java.util.List;
import java.util.stream.IntStream;

public final class People {
    public static List<Person> list(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> Persons.aPerson()
                        .withName("User_" + i)
                        .build())
                .toList();
    }

    private People() {
    }
}
