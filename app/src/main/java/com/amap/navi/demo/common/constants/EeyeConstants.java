package com.amap.navi.demo.common.constants;

public class EeyeConstants {

    public static final int MODULE_MANAGER_INIT = 1;//程序启动
    public static final int MODULE_MANAGER_UNINIT = 2;//程序关闭
    public static final int MODULE_MANAGER_DOWNLOAD = 3;//程序下载
    public static final int MODULE_MANAGER_UPLOAD = 4;//程序上传
    public static final int MODULE_MANAGER_SENDMSG = 5;//发送消息
    public static final int MODULE_MANAGER_START_CATCH = 6;//开始抓取

    public static final int DEVICE_STATUS_READY = 7;
    public static final int DEVICE_STATUS_RUNNING = 8;

    public static final int MODULE_DOWNLOAD = 10;
    public static final int MODULE_UPLOAD = 20;
    public static final int MODULE_SOCKET = 30;
    public static final int MODULE_CATCHEYE = 40;

    //状态回调
    /**
     * 下载模块
     */
    public static final int MODULE_DOWNLOAD_START = MODULE_DOWNLOAD + 1;
    public static final int MODULE_DOWNLOAD_FINISH = MODULE_DOWNLOAD + 2;
    public static final int MODULE_DOWNLOAD_PAUSE = MODULE_DOWNLOAD + 3;

    /**
     * 上传模块
     */
    public static final int MODULE_UPLOAD_START = MODULE_UPLOAD + 1;
    public static final int MODULE_UPLOAD_FINISH = MODULE_UPLOAD + 2;
    public static final int MODULE_UPLOAD_PAUSE = MODULE_UPLOAD + 3;

    /**
     * socket模块
     */
    public static final int MODULE_SOCKET_CONNECT = MODULE_SOCKET + 1;
    public static final int MODULE_SOCKET_DISCONNECT = MODULE_SOCKET + 2;
    public static final int MODULE_SOCKET_ONRECEIVED = MODULE_SOCKET + 3;

    /**
     * 数据抓取模块
     */
    public static final int MODULE_CATCHEYE_START = MODULE_CATCHEYE + 1;
    public static final int MODULE_CATCHEYE_FINISH = MODULE_CATCHEYE + 2;
    public static final int MODULE_CATCHEYE_PAUSE = MODULE_CATCHEYE + 3;


}
