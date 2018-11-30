package com.amap.navi.demo.controller;

import android.content.Context;
import android.util.Log;

import com.amap.navi.demo.common.Configs;
import com.amap.navi.demo.common.constants.EeyeConstants;
import com.amap.navi.demo.model.api.IHttpProxy;
import com.amap.navi.demo.model.api.MHttpRequest;
import com.amap.navi.demo.model.bean.CommandBean;
import com.amap.navi.demo.model.bean.DeviceCommand;
import com.amap.navi.demo.model.bean.ToolsConfigBean;

import java.io.File;
import java.util.Observable;

public class DownloadController extends AbsModuleObserver implements IHttpProxy.IOnHttpStateListener {

    private static final String TAG = "DownloadController";
    //处理收到的下载命令
    private MHttpRequest mHttpRequest;
    private String mUrl = "";

    private boolean isDirectCallback = true;

    //缓存下载任务
    //开启子线程启动下载
    public DownloadController(Context context) {
        mHttpRequest = new MHttpRequest(context);
    }

    public void startDownload(String url, String saveFilePath) {

        final String furl = url;
        final String fsaveFilePath = saveFilePath;

        Log.e(TAG, "startDownload->" + url + ";;;saveFilePath->" + saveFilePath);

        new Thread() {
            @Override
            public void run() {

                mHttpRequest.download(furl, fsaveFilePath, DownloadController.this);

                super.run();
            }
        }.start();
    }

    @Override
    public void onSuccess(int state, String resultStr, Object resultObj) {

        if(isDirectCallback)
            notifyManager(EeyeConstants.MODULE_DOWNLOAD_FINISH, resultStr, resultObj);
        else{
            notifyManager(EeyeConstants.MODULE_DOWNLOAD_FINISH, resultStr, resultObj);
            isDirectCallback = true;
        }
    }


    @Override
    public void onFailed(int state, String resultStr) {

        if(isDirectCallback)
            notifyManager(EeyeConstants.MODULE_DOWNLOAD_FINISH, resultStr, null);
        else{
            notifyManager(EeyeConstants.MODULE_DOWNLOAD_FINISH, resultStr, null);
            isDirectCallback = true;
        }
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void update(Observable o, Object arg) {
        CommandBean c = (CommandBean) arg;

        switch (c.getState()) {

            case EeyeConstants.MODULE_MANAGER_INIT:
                ToolsConfigBean toolsConfigBean = (ToolsConfigBean) c.getObj();

                mUrl = "http://" + toolsConfigBean.getIp() + ":" + toolsConfigBean.getHttpPort() + "/action/download?filePath=";

                break;

            case EeyeConstants.MODULE_MANAGER_DOWNLOAD:
                //c.getCommand();
                //下载这个文件夹下的所有数据结果
                DeviceCommand d = (DeviceCommand) c.getObj();

                String savePath2 = buildAndroidSavePath(d.getSourceData());

                File file = new File(savePath2);

                boolean isExists = file.exists();

                if(!isExists) {
                    isDirectCallback = false;
                }else{
                    isDirectCallback = true;
                }
                //下载配置文件
                String url = mUrl + d.getConfigPath();

                String savePath = Configs.EYECONFIG_PATH;

                startDownload(url, savePath);

                //下载源数据
                String url2 = mUrl + d.getSourceData();

                if(!isExists) {
                    startDownload(url2, savePath2);
                }

                break;
        }

    }


    public String buildAndroidSavePath(String path) {

        String tmp = path.replace("D:\\git_space\\EeyeTools_Server\\routes\\tmp\\filter\\", "");

//        Log.e(TAG, "tmp->" + tmp);

        int index = tmp.lastIndexOf("\\");

        String subTmp1 = tmp.substring(0, index);

        String subTmp2 = tmp.substring(index+1, index+3);

        String subTmp3 = tmp.substring(index+1, tmp.length());

//        Log.e(TAG, "subTmp1->"+subTmp1+";;;subTmp2->"+subTmp2+";;;subTmp3->"+subTmp3);

        return "/mnt/sdcard/CameraData/"+subTmp1+"/"+subTmp2+"/"+subTmp3;
    }

}
