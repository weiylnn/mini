package com.qianfan123.minya.network.http;

/**
 * Created by dzq on 2016/11/2.
 */

public class AuthEvent {
    public static int TOKEN_EXPIRED = 1;

    public int type;

    public AuthEvent(int type) {
        this.type = type;
    }
}
