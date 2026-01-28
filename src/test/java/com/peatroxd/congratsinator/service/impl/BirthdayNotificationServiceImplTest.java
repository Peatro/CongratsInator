package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.notification.BirthdayNotificationMessageBuilder;
import com.peatroxd.congratsinator.notification.NotificationProperties;
import com.peatroxd.congratsinator.service.BirthdayService;
import com.peatroxd.congratsinator.service.EmailSenderService;
import com.peatroxd.congratsinator.testdata.Emails;
import com.peatroxd.congratsinator.testdata.Persons;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BirthdayNotificationServiceImplTest {

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
        verify(birthdayService).getUpcomingBirthdays(UPCOMING_DAYS);

        verifyNoInteractions(messageBuilder);
        verifyNoInteractions(emailSender);
        verify(notificationProperties, never()).getRecipients();

        verifyNoMoreInteractions(notificationProperties, birthdayService);
    }

    @Test
    void sendUpcomingBirthdayNotification_whenUpcomingExists_buildsMessage_andSendsEmail() {
        List<String> recipients = Emails.recipients();

        Person p1 = Persons.withBirthday(LocalDate.of(1990, 12, 31));
        Person p2 = Persons.withBirthday(LocalDate.of(1992, 1, 2));
        List<Person> upcoming = List.of(p1, p2);

        when(notificationProperties.getUpcomingDays()).thenReturn(UPCOMING_DAYS);
        when(birthdayService.getUpcomingBirthdays(UPCOMING_DAYS)).thenReturn(upcoming);
        when(messageBuilder.build(upcoming, UPCOMING_DAYS)).thenReturn("message");
        when(notificationProperties.getRecipients()).thenReturn(recipients);

        service.sendUpcomingBirthdayNotification();

        verify(notificationProperties).getUpcomingDays();
        verify(birthdayService).getUpcomingBirthdays(UPCOMING_DAYS);
        verify(messageBuilder).build(upcoming, UPCOMING_DAYS);

        verify(notificationProperties).getRecipients();
        verify(emailSender).send(eq(recipients), anyString(), eq("message"));

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
