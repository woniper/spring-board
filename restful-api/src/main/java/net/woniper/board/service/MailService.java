package net.woniper.board.service;

/**
 * Created by woniper on 2015. 10. 8..
 */
public interface MailService {

    void send(String subject, String body);

    void send(String to, String subject, String body);
}
