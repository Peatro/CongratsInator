package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.service.EmailSenderService;
import com.peatroxd.congratsinator.notification.NotificationProperties;
import com.peatroxd.congratsinator.service.BirthdayNotificationService;
import com.peatroxd.congratsinator.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BirthdayNotificationServiceImpl implements BirthdayNotificationService {

    private final BirthdayService birthdayService;
    private final EmailSenderService emailSender;
    private final NotificationProperties notificationProperties;

    @Transactional(readOnly = true)
    public void sendUpcomingBirthdayNotification() {
        int days = notificationProperties.getUpcomingDays();

        List<Person> upcoming = birthdayService.getUpcomingBirthdays(days);

        if (upcoming.isEmpty()) {
            return;
        }

        String message = buildMessage(upcoming, days);

        emailSender.send(
                notificationProperties.getRecipients(),
                "Ближайшие дни рождения",
                message
        );

        log.info("Sent upcoming birthday notification for {} persons", upcoming.size());
    }

    private String buildMessage(List<Person> persons, int days) {
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
