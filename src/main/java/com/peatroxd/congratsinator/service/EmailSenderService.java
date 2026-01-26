package com.peatroxd.congratsinator.service;

import java.util.List;

public interface EmailSenderService {
    void send(List<String> to, String subject, String body);
}
