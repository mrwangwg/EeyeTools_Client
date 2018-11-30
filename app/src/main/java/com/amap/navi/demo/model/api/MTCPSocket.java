package com.amap.navi.demo.model.api;

import android.content.Context;

public class MTCPSocket {

    private ISocketProxy socketProxy;

    public MTCPSocket(Context context) {
        socketProxy = new SocketProxyImpl(context);
    }

    public void openSocket(String ip, int port, ISocketProxy.IOnSocketStateListener listener) {
        socketProxy.openSocket(ip, port, listener);
    }

    public void sendMsg(String msg) {
        socketProxy.sendMsg(msg);
    }

    public void closeSocket() {
        socketProxy.closeSocket();
    }

    public void setSocketProxy(ISocketProxy socketProxy) {
        this.socketProxy = socketProxy;
    }
}
