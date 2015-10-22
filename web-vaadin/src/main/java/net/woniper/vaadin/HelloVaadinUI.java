package net.woniper.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * Created by woniper on 2015. 10. 21..
 */
@SpringUI
@Theme("hello")
public class HelloVaadinUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Button btnHello = new Button("Hello");
        btnHello.setWidth(10, Unit.CM);
        btnHello.setHeight(10, Unit.CM);
        btnHello.addClickListener(e -> Notification.show("Hello~ Vaadin"));

        setContent(btnHello);
    }
}
