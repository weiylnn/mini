package com.leap.mini.cmp.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 异步任务下载任务类
 * <p>
 * </> Created by weiyaling on 17/3/7.
 */

// 一般我们把AsyncTask的子类定义在Activity的内部
// 通过这种方式，我们就可以轻松地在这里更改UI线程
class UpdateTask extends AsyncTask<String, Integer, String> {
  private Context context;
  private PowerManager.WakeLock mWakeLock;
  private ProgressBar mProgressBar;
  private TextView tvProgress;
  private String filePath;
  private OnProgressListener progressListener;
  private OnSuccessListener successListener;
  private OnErrorListener errorListener;
  private long position = 0;

  UpdateTask(Context context, long position, ProgressBar progressBar, TextView textView,
      String filePath) {
    this.context = context;
    this.mProgressBar = progressBar;
    this.tvProgress = textView;
    this.position = position;
    this.filePath = filePath;
  }

  public UpdateTask(Context context, long position, String filePath) {
    this.context = context;
    this.position = position;
    this.filePath = filePath;
  }

  @Override
  protected String doInBackground(String... targetData) {
    InputStream input = null;
    OutputStream output = null;
    HttpURLConnection connection = null;
    try {
      URL url = new URL(targetData[0]);
      connection = (HttpURLConnection) url.openConnection();
      // 如果下载位置大于0，设置从上次下载的位置开始传送
      if (position > 0) {
        connection.setRequestProperty("User-Agent", "NetFox");
        connection.setRequestProperty("RANGE", "bytes=" + position);
      }
      connection.connect();

      // 避免因为接收到非HTTP 200 OK状态，而导致只或者错误代码，而不是要下载的文件
      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        return "Server returned HTTP " + connection.getResponseCode() + " "
            + connection.getResponseMessage();
      }

      // 这对显示下载百分比有帮助
      // 当服务器没有返回文件的大小时，数字可能为-1
      long fileLength = position + connection.getContentLength();

      // 下载文件
      input = connection.getInputStream();

      File file = new File(filePath);

      if (!file.exists()) {
        file.createNewFile();
      }

      output = new FileOutputStream(file);
      filePath = file.getAbsolutePath();
      byte data[] = new byte[4096];
      long total = position;
      int count;
      while ((count = input.read(data)) != -1) {
        // 允许用返回键取消下载
        if (isCancelled()) {
          input.close();
          return null;
        }
        total += count;
        // 更新下载进度
        if (fileLength > 0) // 只有当 fileLength>0 的时候才会调用
          publishProgress((int) (total * 100 / fileLength));
        output.write(data, 0, count);
      }
    } catch (Exception e) {
      return e.toString();
    } finally {
      try {
        if (output != null)
          output.close();
        if (input != null)
          input.close();
      } catch (IOException ignored) {
      }

      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    // 取得CPU锁，避免因为用户在下载过程中按了电源键而导致的失效
    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
    mWakeLock.acquire();
  }

  @Override
  protected void onProgressUpdate(Integer... progress) {
    super.onProgressUpdate(progress);
    if (progressListener != null) {
      progressListener.onProgressUpdate(progress[0]);
    }
    // 如果到了这里，文件长度是确定的，设置indeterminate为false
    if (mProgressBar != null) {
      mProgressBar.setIndeterminate(false);
      mProgressBar.setMax(100);
      mProgressBar.setProgress(progress[0]);
      tvProgress.setText(progress[0] + "%");
    }
  }

  @Override
  protected void onPostExecute(String result) {
    mWakeLock.release();
    if (result != null && errorListener != null) {
      errorListener.onError(result);
    } else if (successListener != null) {
      successListener.onSuccess(filePath);
    }
  }

  public void setOnProgressListener(OnProgressListener progressListener) {
    this.progressListener = progressListener;
  }

  public void setSuccessListener(OnSuccessListener successListener) {
    this.successListener = successListener;
  }

  public void setErrorListener(OnErrorListener errorListener) {
    this.errorListener = errorListener;
  }

  interface OnProgressListener {
    void onProgressUpdate(int progress);
  }

  public interface OnSuccessListener<T> {
    void onSuccess(T data);
  }

  interface OnErrorListener<T> {
    void onError(T data);
  }
}