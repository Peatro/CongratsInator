package com.peatroxd.congratsinator.notification;

import com.peatroxd.congratsinator.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BirthdayNotificationMessageBuilder {

    public String build(List<Person> persons, int days) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ближайшие дни рождения (")
                .append(days)
                .append(" дней):\n\n");

        persons.forEach(p ->
                sb.append(p.getName())
                        .append(" — ")
                        .append(p.getBirthday())
                        .append("\n")
        );

        return sb.toString();
    }
}
