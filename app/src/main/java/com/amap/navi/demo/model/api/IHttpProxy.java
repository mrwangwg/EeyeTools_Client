package com.amap.navi.demo.model.api;

import java.net.MalformedURLException;

public interface IHttpProxy {

    int STATE_OK = 200;
    int STATE_IO_FAIL = 2;
    int STATE_URL_FAIL = 3;

    void sendGET(String url, IOnHttpStateListener listener);

    void sendPOST(String url, String params, IOnHttpStateListener listener);

    /**
     * @param url          请求下载的url
     * @param saveFilePath 要保存文件的目的路径
     */
    void download(String url, String saveFilePath, IOnHttpStateListener listener);

    /**
     * @param url            请求上传的url
     * @param uploadFilePath 要上传文件的路径
     */
    void upload(String url, String uploadFilePath, IOnHttpStateListener listener);

    interface IOnHttpStateListener {

        void onSuccess(int state, String resultStr, Object resultObj);

        void onFailed(int state, String resultStr);

        void onProgress(int progress);

    }

}
