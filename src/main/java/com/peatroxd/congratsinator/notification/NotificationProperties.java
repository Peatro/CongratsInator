package com.peatroxd.congratsinator.notification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "notifications")
@Getter
@Setter
public class NotificationProperties {
    private String cron;
    private int upcomingDays;
    private List<String> recipients;
}
