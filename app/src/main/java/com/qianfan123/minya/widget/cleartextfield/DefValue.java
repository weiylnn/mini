package com.qianfan123.minya.widget.cleartextfield;

import com.qianfan123.minya.R;

/**
 * Created by wangcong on 2017/1/11.
 */

enum DefValue {
  TXT_SIZE(36), TXT_COLOR(R.color.light_black), RIGHT_IMG(R.mipmap.ic_global_clear), BACKGROUND(
      0), HINT_COLOR(R.color.gray);

  int value;

  DefValue(int value) {
    this.value = value;
  }
}
