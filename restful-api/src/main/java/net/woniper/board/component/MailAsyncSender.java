package net.woniper.board.component;

/**
 * Created by woniper on 2015. 10. 8..
 */
public interface MailAsyncSender {

    void send(String subject, String body);

    void send(String to, String subject, String body);
}
