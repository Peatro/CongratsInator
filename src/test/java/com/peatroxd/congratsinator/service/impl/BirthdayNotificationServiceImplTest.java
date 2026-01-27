package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.TestData;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.notification.BirthdayNotificationMessageBuilder;
import com.peatroxd.congratsinator.notification.NotificationProperties;
import com.peatroxd.congratsinator.service.BirthdayService;
import com.peatroxd.congratsinator.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.peatroxd.congratsinator.TestData.createListOfEmails;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BirthdayNotificationServiceImplTest {

    private static final String SUBJECT_UPCOMING_BIRTHDAYS = "Ближайшие дни рождения";
    private static final int UPCOMING_DAYS = 7;

    @Mock
    private BirthdayService birthdayService;

    @Mock
    private EmailSenderService emailSender;

    @Mock
    private NotificationProperties notificationProperties;

    @Mock
    private BirthdayNotificationMessageBuilder messageBuilder;

    @InjectMocks
    private BirthdayNotificationServiceImpl service;

    @Test
    void sendUpcomingBirthdayNotification_whenNoUpcoming_doesNothing() {
        when(notificationProperties.getUpcomingDays()).thenReturn(UPCOMING_DAYS);
        when(birthdayService.getUpcomingBirthdays(UPCOMING_DAYS)).thenReturn(List.of());

        service.sendUpcomingBirthdayNotification();

        verify(notificationProperties).getUpcomingDays();
        verify(birthdayService).getUpcomingBirthdays(7);

        verifyNoInteractions(messageBuilder);
        verifyNoInteractions(emailSender);
        verifyNoMoreInteractions(notificationProperties, birthdayService);
    }

    @Test
    void sendUpcomingBirthdayNotification_whenUpcomingExists_buildsMessage_andSendsEmail() {
        List<String> recipients = createListOfEmails();

        Person p1 = TestData.createPersonUsingDateOfBirth(LocalDate.of(1990, 12, 31));
        Person p2 = TestData.createPersonUsingDateOfBirth(LocalDate.of(1992, 1, 2));
        List<Person> upcoming = List.of(p1, p2);

        when(notificationProperties.getUpcomingDays()).thenReturn(UPCOMING_DAYS);
        when(notificationProperties.getRecipients()).thenReturn(recipients);
        when(birthdayService.getUpcomingBirthdays(UPCOMING_DAYS)).thenReturn(upcoming);
        when(messageBuilder.build(upcoming, UPCOMING_DAYS)).thenReturn("message");

        service.sendUpcomingBirthdayNotification();

        verify(notificationProperties).getUpcomingDays();
        verify(birthdayService).getUpcomingBirthdays(UPCOMING_DAYS);

        verify(messageBuilder).build(upcoming, UPCOMING_DAYS);

        verify(notificationProperties).getRecipients();
        verify(emailSender).send(recipients, SUBJECT_UPCOMING_BIRTHDAYS, "message");

        verifyNoMoreInteractions(notificationProperties, birthdayService, messageBuilder, emailSender);
    }

    @Test
    void sendUpcomingBirthdayNotification_readsRecipientsOnlyWhenThereIsSomethingToSend() {
        when(notificationProperties.getUpcomingDays()).thenReturn(UPCOMING_DAYS);
        when(birthdayService.getUpcomingBirthdays(UPCOMING_DAYS)).thenReturn(List.of());

        service.sendUpcomingBirthdayNotification();

        verify(notificationProperties, never()).getRecipients();
        verify(emailSender, never()).send(anyList(), anyString(), anyString());
    }
}
