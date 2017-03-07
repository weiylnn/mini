package com.qianfan123.minya.network.interactors.update.usecase;

import com.qianfan123.minya.network.http.BaseUseCase;
import com.qianfan123.minya.network.http.bean.ResponseBean;
import com.qianfan123.minya.network.interactors.update.UpdateServiceApi;
import com.qianfan123.minya.network.model.UpdateModel;

import rx.Observable;

/**
 * Created by dzq on 2016/11/1.
 */

public class CheckVersionCase extends BaseUseCase<UpdateServiceApi> {

  private String appToken;
  private String pid;
  private String appId;
  private String appKey;
  private String versionCode;

  /**
   * 检查新版本
   *
   * @param appToken
   * @param appKey
   *          时间撮
   * @param pid
   *          应用类型 android平台传1 iOS平台传2
   * @param appId
   *          应用内部标识
   * @param versionCode
   *          应用版本号
   * @return
   */
  public CheckVersionCase(String appToken, String appKey, String pid, String appId,
      String versionCode) {
    this.appToken = appToken;
    this.pid = pid;
    this.appKey = appKey;
    this.appId = appId;
    this.versionCode = versionCode;
  }

  @Override
  protected Observable<ResponseBean<UpdateModel>> buildCase() {
    return createConnection().checkVersion(appToken, appKey, pid, appId, versionCode);
  }
}