package com.amap.navi.demo.model.api;

public interface ISocketProxy {

    void openSocket(String ip, int port, IOnSocketStateListener listener);

    void sendMsg(String msg);

    void closeSocket();

    public interface IOnSocketStateListener{
        final int CONNECTED = 0;
        final int DISCONNECTED = 1;

        void onMsgReceived(String msg);

        void onTCPStatusChanged(int status);
    }

}
