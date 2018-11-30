package com.amap.navi.demo.model.api;

import android.content.Context;
import android.util.Log;

import com.amap.navi.demo.common.util.SPUtils;
import com.amap.navi.demo.model.bean.HttpTaskBean;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HttpProxyImpl implements IHttpProxy {

    private static final String TAG = "HttpProxyImpl";

    private static final int TIMEOUT = 7 * 1000;
    private static final int THREAD_MUN = 5;//多线程下载的线程数量
    private static final int BUFFER_SIZE = 8 * 1024;

    private static final String PROGRESS_FILE = "progress_file";

    private Context mContext;
    private SPUtils spUtils;
    private long downloadProgress;
    private long uploadProgress;

    private static String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
    private static final String PREFIX = "--", LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data";   //内容类型
    private static final String CHARSET = "utf-8"; //设置编码

    public HttpProxyImpl(Context context) {
        mContext = context;
        spUtils = new SPUtils(context, PROGRESS_FILE);

    }

    @Override
    public void sendGET(String urlPath, IOnHttpStateListener response) {
        try {
            URL url = new URL(urlPath);

            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.connect();

            if (conn.getResponseCode() == 200) {

                byte[] data = readStream(conn.getInputStream());

                Log.i(TAG, "Get方式请求成功，返回数据如下：");

                Log.i(TAG, new String(data, "UTF-8"));

            } else {
                Log.i(TAG, "Get方式请求失败");
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPOST(String urlPath, String params, IOnHttpStateListener response) {

        try {
            byte[] postData = params.getBytes();

            URL url = new URL(urlPath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(TIMEOUT);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencode");
            conn.connect();

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.write(postData);
            dos.flush();
            dos.close();

            if (conn.getResponseCode() == 200) {

                byte[] data = readStream(conn.getInputStream());

                Log.i(TAG, "Post请求方式成功，返回数据如下：");

                Log.i(TAG, new String(data, "UTF-8"));
            } else {
                Log.i(TAG, "Post方式请求失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void download(String urlPath, String saveFilePath, IOnHttpStateListener listener) {
        downloadProgress = 0;

        File file = new File(saveFilePath);

        if(!file.getParentFile().exists()) {

            boolean mkSuccess = false;
            while (!mkSuccess) {
                Log.e(TAG, "111111");
                mkSuccess = file.getParentFile().mkdirs();
                Log.e(TAG, "2222mkSuccess->" + mkSuccess);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "3333mkSuccess->" + mkSuccess);
            }
        }

        HttpURLConnection conn;
        try {
            URL url = new URL(urlPath);

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == 200) {

                //TODO 获取文件名称

                long fileSize = conn.getContentLength();//获取文件大小

                long blockSize = fileSize / THREAD_MUN;

                //计算每个线程执行的起始和结束位置
                for (int i = 0; i < THREAD_MUN; i++) {
                    long startFlag = i * blockSize;
                    long endFlag = (i + 1) * blockSize;

                    HttpTaskBean httpTaskBean = new HttpTaskBean();
                    httpTaskBean.setThreadId(i);
                    httpTaskBean.setUrl(urlPath);
                    httpTaskBean.setFileSize(fileSize);
                    httpTaskBean.setType(HttpTaskBean.Type.DOWNLOAD);
                    httpTaskBean.setFilePath(saveFilePath);
                    httpTaskBean.setStartFlag(startFlag);
                    //TODO  读取进度文件
                    long progress = parseFile2TaskInfo(saveFilePath + i);

                    downloadProgress += progress;

                    httpTaskBean.setProgressFlag(progress);
                    httpTaskBean.setEndFlag(endFlag);

                    new Thread(new DownloadTask(httpTaskBean, listener)).start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            listener.onFailed(IHttpProxy.STATE_IO_FAIL, saveFilePath);
        }
    }

    @Override
    public void upload(String urlPath, String sourceFilePath, IOnHttpStateListener listener) {
        uploadProgress = 0;

        File file = new File(sourceFilePath);

        try {

            URL url = new URL(urlPath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT);
            conn.setConnectTimeout(TIMEOUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.setRequestProperty("action", "upload");
            conn.connect();

            if (file != null && file.exists()) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                StringBuffer sb = new StringBuffer();

                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                sb.append("Content-Disposition: form-data; name=\"data\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());

                long totalSpace = file.getTotalSpace();

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[BUFFER_SIZE];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    updateProgress(listener, 2, totalSpace, len, sourceFilePath);
                }
                is.close();

                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    String result = sb1.toString();
                    Log.e(TAG, "upload->" + result);
//                    listener.onSuccess(200, "ok", null);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listener.onFailed(IHttpProxy.STATE_URL_FAIL, sourceFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onFailed(IHttpProxy.STATE_IO_FAIL, sourceFilePath);
        }

    }

    private long parseFile2TaskInfo(String key) {
        long progress = spUtils.getInt(key, 0);

        return progress;
    }

    private void saveProgress(String key, long progress) {
        spUtils.putLong(key, progress);
    }


    private byte[] readStream(InputStream inputStream) {


        return null;
    }

    class DownloadTask implements Runnable {

        private HttpTaskBean taskBean;
        private IOnHttpStateListener listener;

        public DownloadTask(HttpTaskBean taskBean, IOnHttpStateListener listener) {
            this.taskBean = taskBean;
            this.listener = listener;
        }

        @Override
        public void run() {
            HttpURLConnection conn;
            BufferedInputStream bis;
            RandomAccessFile raf;

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            try {
                File file = new File(taskBean.getFilePath());

                raf = new RandomAccessFile(file, "rwd");

                URL url = new URL(taskBean.getUrl());

                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(TIMEOUT);
                conn.setReadTimeout(TIMEOUT);
                conn.setRequestMethod("GET");

                long startProgress = taskBean.getStartFlag() + taskBean.getProgressFlag();

                conn.setRequestProperty("Range", "bytes=" + startProgress + "-" + taskBean.getEndFlag());

                raf.seek(taskBean.getStartFlag());
                conn.connect();

//                if (conn.getResponseCode() == 200) {

                bis = new BufferedInputStream(conn.getInputStream());

                String key = taskBean.getFilePath() + taskBean.getThreadId();

                while (-1 != (len = bis.read(buffer))) {
                    raf.write(buffer, 0, len);

                    long progress = taskBean.getProgressFlag() + len;

                    taskBean.setProgressFlag(progress);
                    //保存记录
                    saveProgress(key, progress);

                    updateProgress(listener, 1, taskBean.getFileSize(), len, taskBean.getFilePath());
                }

                if (len == -1) {//如果读取到文件末尾则下载完成
                    Log.i("tag", "下载完了");
                } else {//否则下载系手动停止
                    Log.i("tag", "下载停止");
                }

                bis.close();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class UploadTask implements Runnable {

        public UploadTask(HttpTaskBean taskBean) {


        }

        @Override
        public void run() {


        }
    }

    private void updateProgress(IOnHttpStateListener listener, int type, long fileContentLength, int progressAdd, String savePath) {
        long progress = 0;
        if(type==1){// 下载
            downloadProgress += progressAdd;
            progress = downloadProgress;
        }else{//上传
            uploadProgress += progressAdd;
            progress = uploadProgress;
        }

        progress += progressAdd;

        Log.e(TAG, "updateProgress->downloadProgress->"+ downloadProgress);

        Log.e(TAG, "updateProgress->fileContentLength->"+ fileContentLength);

        Log.e(TAG, "updateProgress->"+ progress);

        int tmp = Math.round(progress * 100 / fileContentLength);

        if (progress >= fileContentLength) {
            listener.onSuccess(IHttpProxy.STATE_OK, savePath, null);
        } else {
            listener.onProgress(tmp);
        }
    }

}
