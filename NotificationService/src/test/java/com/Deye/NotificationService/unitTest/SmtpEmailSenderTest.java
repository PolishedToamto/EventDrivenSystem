package com.Deye.NotificationService.unitTest;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SmtpEmailSenderTest {
    private GreenMail greenMail;

    @Autowired
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setup() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);//ServerSetUpTest.SMTP use the 3025 port, the default port for non test is 25 which is restricted
        greenMail.start();
    }

    @AfterEach
    public void tearDown() {
        greenMail.stop();
    }

    @Test
    public void testSendEmail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("your-real-email@test.com");
        message.setSubject("Test");
        message.setText("Hello");

        javaMailSender.send(message);

        MimeMessage[] mails = greenMail.getReceivedMessages();

        Assert.assertEquals(1, mails.length);
        Assert.assertEquals("Test", mails[0].getSubject());
        Assert.assertEquals("Hello", mails[0].getContent());
    }
}
