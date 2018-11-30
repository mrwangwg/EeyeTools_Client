package com.amap.navi.demo.controller;


import android.content.Context;
import android.util.Log;

import com.amap.navi.demo.common.constants.EeyeConstants;
import com.amap.navi.demo.model.api.ISocketProxy;
import com.amap.navi.demo.model.api.MTCPSocket;
import com.amap.navi.demo.model.bean.CommandBean;
import com.amap.navi.demo.model.bean.ToolsConfigBean;

import java.util.Observable;
import java.util.Observer;

public class SocketController extends AbsModuleObserver implements ISocketProxy.IOnSocketStateListener {

    private static final String TAG = "SocketController";
    //读取配置文件的ip和port

    //在子线程调用socket

    private MTCPSocket mTcpSocket;

    private String ip;
    private int port;

    public SocketController(Context context) {

        mTcpSocket = new MTCPSocket(context);
    }

    public void startSocket(ToolsConfigBean toolsConfigBean) {

        ip = toolsConfigBean.getIp();
        port = toolsConfigBean.getSocketPort();

        Log.e(TAG, "IP->" + ip + ";;;port->" + port);
        new Thread() {
            @Override
            public void run() {

                mTcpSocket.openSocket(ip, port, SocketController.this);

                super.run();
            }
        }.start();
    }


    public void stopSocket() {
        mTcpSocket.closeSocket();
    }

    public void sendSocketMsg(String msg) {
        Log.e(TAG, "sendSocketMsg->" + msg);
        mTcpSocket.sendMsg(msg);
    }

    //接收socket传过来的消息

    @Override
    public void onMsgReceived(String msg) {

        Log.e(TAG, "onMsgReceived->" + msg);

        notifyManager(EeyeConstants.MODULE_SOCKET_ONRECEIVED, msg, null);

    }

    @Override
    public void onTCPStatusChanged(int status) {

        switch (status) {
            case ISocketProxy.IOnSocketStateListener.CONNECTED:

                notifyManager(EeyeConstants.MODULE_SOCKET_CONNECT, "", null);

                break;
            case ISocketProxy.IOnSocketStateListener.DISCONNECTED:

                notifyManager(EeyeConstants.MODULE_SOCKET_DISCONNECT, "", null);

                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        Log.e(TAG, "update->" + arg);
        CommandBean c = (CommandBean) arg;

        switch (c.getState()) {

            case EeyeConstants.MODULE_MANAGER_INIT:
                startSocket((ToolsConfigBean) c.getObj());
                break;

            case EeyeConstants.MODULE_MANAGER_UNINIT:
                stopSocket();
                break;

            case EeyeConstants.MODULE_MANAGER_SENDMSG:
                sendSocketMsg(c.getCommand());
                break;
        }

    }
}
