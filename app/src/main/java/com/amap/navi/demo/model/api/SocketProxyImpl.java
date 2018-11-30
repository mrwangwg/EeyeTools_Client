package com.amap.navi.demo.model.api;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketProxyImpl implements ISocketProxy{

    private static final String TAG = "SocketProxyImpl";

    private String ip;
    private int port;
    private IOnSocketStateListener listener;

    private PrintWriter mPrintWriter = null;
    private int currentStatus = -1;
    private boolean isShutDown = false;

    public SocketProxyImpl(Context context) {


    }

    @Override
    public void openSocket(String ip, int port, IOnSocketStateListener listener) {
        this.ip = ip;
        this.port = port;
        this.listener = listener;

        Log.e(TAG, "IP->" + ip + ";;;PORT->" + port);

        Log.e(TAG, "openSocket ->" );

        isShutDown = false;

        Socket socket = null;

        while (!isShutDown && socket == null) {
            try {
                socket = new Socket(ip, port);

                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                callbackTCPStatus(IOnSocketStateListener.CONNECTED);

            } catch (IOException e) {
                e.printStackTrace();

                socket = null;

                callbackTCPStatus(IOnSocketStateListener.DISCONNECTED);

                SystemClock.sleep(1000);
                Log.e(TAG, "The TCP connect failed !");
            }

        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!isShutDown) {
                Log.e(TAG, "333333");
                String msg = br.readLine();
                Log.e(TAG, "received msg2222 ->" + msg);

                if (null != msg) {
                    Log.e(TAG, "received msg ->" + msg);

                    if (null != listener)
                        listener.onMsgReceived(msg);
                }
            }

            mPrintWriter.close();
            br.close();
            socket.close();
            mPrintWriter = null;
//            socket = null;

            callbackTCPStatus(IOnSocketStateListener.DISCONNECTED);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMsg(String msg) {
        Log.e(TAG, "sendMsg ->" + msg);
        if(null!=mPrintWriter) {
            mPrintWriter.println(msg);
            mPrintWriter.flush();
        }
    }

    @Override
    public void closeSocket() {
        Log.e(TAG, "closeSocket ->" );
        isShutDown = true;
    }

    private void callbackTCPStatus(int status) {
        currentStatus = status;
        if (null != listener)
            listener.onTCPStatusChanged(currentStatus);
    }
}
