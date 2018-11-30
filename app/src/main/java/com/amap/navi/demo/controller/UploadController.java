package com.amap.navi.demo.controller;

import android.content.Context;

import com.amap.navi.demo.common.constants.EeyeConstants;
import com.amap.navi.demo.model.api.IHttpProxy;
import com.amap.navi.demo.model.api.MHttpRequest;
import com.amap.navi.demo.model.bean.CommandBean;

import java.util.Observable;
import java.util.Observer;

public class UploadController extends AbsModuleObserver implements IHttpProxy.IOnHttpStateListener {

    //处理上传的命令
    private MHttpRequest mHttpRequest;

    public UploadController(Context context) {
        mHttpRequest = new MHttpRequest(context);

    }

    public void startUpload(String url, String saveFilePath) {

        final String furl = url;
        final String fsaveFilePath = saveFilePath;

        new Thread() {
            @Override
            public void run() {

                mHttpRequest.download(furl, fsaveFilePath, UploadController.this);

                super.run();
            }
        }.start();
    }

    @Override
    public void onSuccess(int state, String resultStr, Object resultObj) {


    }

    @Override
    public void onFailed(int state, String resultStr) {


    }

    @Override
    public void onProgress(int progress) {


    }

    //缓存上传任务
    //开启子线程执行上传任务


    @Override
    public void update(Observable o, Object arg) {

        CommandBean c = (CommandBean) arg;

        switch (c.getState()) {

            case EeyeConstants.MODULE_MANAGER_UPLOAD:
                //c.getCommand();
                //上传这个文件夹下的所有数据结果

                break;
        }

    }
}
