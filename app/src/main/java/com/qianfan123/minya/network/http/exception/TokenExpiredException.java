package com.qianfan123.minya.network.http.exception;

import java.io.IOException;

/**
 * Token过期异常实体类
 * <p>
 * </> Created by weiyaling on 2017/2/23.
 */

public class TokenExpiredException extends IOException {
  private int code;
  private String message;

  public TokenExpiredException(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
