package com.peatroxd.congratsinator.scheduler;

import com.peatroxd.congratsinator.service.BirthdayNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BirthdayNotificationScheduler {
    private final BirthdayNotificationService birthdayNotificationService;

    @Scheduled(
            cron = "${notifications.cron}",
            zone = "Europe/Moscow"
    )
    public void sendNotifications() {
        log.debug("Starting scheduled task: send upcoming birthday notifications");
        birthdayNotificationService.sendUpcomingBirthdayNotification();
    }
}
