package net.woniper.board.component;

import lombok.extern.slf4j.Slf4j;
import net.woniper.board.component.MailAsyncSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by woniper on 2015. 10. 8..
 */
@Service
@Slf4j
public class MailAsyncSenderImpl implements MailAsyncSender {

    private MailSender mailSender;
    private SimpleMailMessage mailTemplate;

    @Autowired
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setMailTemplate(SimpleMailMessage simpleMailMessage) {
        this.mailTemplate = simpleMailMessage;
    }

    @Async
    @Override
    public void send(String subject, String body) {
        SimpleMailMessage smm = new SimpleMailMessage(mailTemplate);
        smm.setSubject(subject);
        smm.setText(body);
        try {
            mailSender.send(smm);
        } catch (MailException ex) {
            log.error("fail send mail subject : {}, body : {}, ex : {}"
                    , subject, body, ex.getMessage());
        }
    }

    @Async
    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(body);
        try {
            mailSender.send(smm);
        } catch (MailException ex) {
            log.error("fail send mail subject : {}, body :{}", subject, body);
        }
    }
}
