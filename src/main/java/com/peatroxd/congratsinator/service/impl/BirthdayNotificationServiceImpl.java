package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.notification.BirthdayNotificationMessageBuilder;
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
    private final BirthdayNotificationMessageBuilder messageBuilder;

    private static final String SUBJECT_UPCOMING_BIRTHDAYS = "Ближайшие дни рождения";

    @Transactional(readOnly = true)
    public void sendUpcomingBirthdayNotification() {
        int days = notificationProperties.getUpcomingDays();

        List<Person> upcoming = birthdayService.getUpcomingBirthdays(days);

        if (upcoming.isEmpty()) {
            return;
        }

        String message = messageBuilder.build(upcoming, days);

        emailSender.send(
                notificationProperties.getRecipients(),
                SUBJECT_UPCOMING_BIRTHDAYS,
                message
        );

        log.info("Sent upcoming birthday notification for {} persons", upcoming.size());
    }
}
