package com.amap.navi.demo.controller;

import android.content.Context;
import android.util.Log;

import com.amap.navi.demo.common.constants.EeyeConstants;

import java.util.Observer;

public class ObserverFactory {

    public static Observer createObserver(Context context, IModuleManager manager, int module) {
        AbsModuleObserver observer = null;

        switch (module) {
            case EeyeConstants.MODULE_DOWNLOAD:
                observer = new DownloadController(context);
                break;
            case EeyeConstants.MODULE_UPLOAD:
                observer = new UploadController(context);
                break;
            case EeyeConstants.MODULE_SOCKET:
                observer = new SocketController(context);
                break;
            case EeyeConstants.MODULE_CATCHEYE:
                observer = new EeyeCatchController(context);
                break;
            default:
                break;
        }

        if(null!=observer)
            observer.setManager(manager);
        Log.e("ObserverFactory", "observer->" + observer);

        return observer;

    }


}
