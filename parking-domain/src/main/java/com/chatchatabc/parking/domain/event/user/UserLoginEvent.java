package com.chatchatabc.parking.domain.event.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserLoginEvent extends ApplicationEvent {
    private String phone;
    private String otp;

    public UserLoginEvent(Object source, String phone, String otp) {
        super(source);
        this.phone = phone;
        this.otp = otp;
    }
}
