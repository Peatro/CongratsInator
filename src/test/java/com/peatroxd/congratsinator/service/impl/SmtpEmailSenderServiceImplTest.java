package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.service.EmailSenderService;
import com.peatroxd.congratsinator.testdata.Emails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.peatroxd.congratsinator.testdata.Values.TEST_NOTIFICATION_EMAIL_BODY;
import static com.peatroxd.congratsinator.testdata.Values.TEST_NOTIFICATION_EMAIL_SUBJECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class SmtpEmailSenderServiceImplTest {

    private static final String FROM = "peatroxd@gmail.com";
    public static final String MAIL_SENDER_FROM_FIELD = "from";

    @Mock
    private JavaMailSender mailSender;

    private EmailSenderService emailSender;

    @BeforeEach
    void setUp() {
        emailSender = new SmtpEmailSenderServiceImpl(mailSender);
        ReflectionTestUtils.setField(emailSender, MAIL_SENDER_FROM_FIELD, FROM);
    }

    @Test
    void send_buildsMessageAndDelegatesToJavaMailSender() {
        List<String> to = Emails.recipients();
        String subject = TEST_NOTIFICATION_EMAIL_SUBJECT;
        String body = TEST_NOTIFICATION_EMAIL_BODY;

        emailSender.send(to, subject, body);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        verifyNoMoreInteractions(mailSender);

        SimpleMailMessage msg = captor.getValue();

        assertThat(msg.getFrom()).isEqualTo(FROM);
        assertThat(msg.getTo()).containsExactly("a@ex.com", "b@ex.com");
        assertThat(msg.getSubject()).isEqualTo(subject);
        assertThat(msg.getText()).isEqualTo(body);
    }
}
