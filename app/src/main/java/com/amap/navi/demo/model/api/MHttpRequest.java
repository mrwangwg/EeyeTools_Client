package com.amap.navi.demo.model.api;

import android.content.Context;

public class MHttpRequest {

    private IHttpProxy mHttpProxy;

    public MHttpRequest(Context context) {

        //默认
        mHttpProxy = new HttpProxyImpl(context);
    }

    public void sendGET(String url, IHttpProxy.IOnHttpStateListener listener){
        mHttpProxy.sendGET(url, listener);
    }

    public void sendPOST(String url, String params, IHttpProxy.IOnHttpStateListener listener){
        mHttpProxy.sendPOST(url, params, listener);
    }

    public void download(String url, String saveFilePath, IHttpProxy.IOnHttpStateListener listener){
        mHttpProxy.download(url, saveFilePath, listener);
    }

    public void update(String url, String uploadFilePath, IHttpProxy.IOnHttpStateListener listener){
        mHttpProxy.upload(url, uploadFilePath, listener);
    }


    public void setHttpProxy(IHttpProxy httpProxy){
        mHttpProxy = httpProxy;
    }

}
