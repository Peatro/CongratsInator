package com.peatroxd.congratsinator.testdata;

import com.peatroxd.congratsinator.dto.PersonDto;
import com.peatroxd.congratsinator.model.Person;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.peatroxd.congratsinator.testdata.ResultMatcherHelper.all;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public final class JsonPersonAsserts {

    public static ResultMatcher listSize(int expected) {
        return jsonPath("$.length()").value(expected);
    }

    public static ResultMatcher personAt(int index, Person person) {
        String base = "$[" + index + "]";
        return all(
                jsonPath(base + ".id").value(person.getId()
                        .toString()),
                jsonPath(base + ".name").value(person.getName()),

                person.getBirthday() != null
                        ? jsonPath(base + ".birthday").value(person.getBirthday()
                        .toString())
                        : jsonPath(base + ".birthday").doesNotExist()
        );
    }

    public static ResultMatcher dtoAt(int index, PersonDto dto) {
        String base = "$[" + index + "]";
        return all(

                dto.getId() != null
                        ? jsonPath(base + ".id").value(dto.getId()
                        .toString())
                        : jsonPath(base + ".id").doesNotExist(),
                jsonPath(base + ".name").value(dto.getName()),
                dto.getBirthday() != null
                        ? jsonPath(base + ".birthday").value(dto.getBirthday()
                        .toString())
                        : jsonPath(base + ".birthday").doesNotExist()
        );
    }

    public static ResultMatcher dto(PersonDto dto) {
        return all(
                dto.getId() != null
                        ? jsonPath("$.id").value(dto.getId()
                        .toString())
                        : jsonPath("$.id").doesNotExist(),
                jsonPath("$.name").value(dto.getName()),
                dto.getBirthday() != null
                        ? jsonPath("$.birthday").value(dto.getBirthday()
                        .toString())
                        : jsonPath("$.birthday").doesNotExist()
        );
    }

    private JsonPersonAsserts() {
    }
}
