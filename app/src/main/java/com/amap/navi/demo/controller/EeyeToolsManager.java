package com.amap.navi.demo.controller;

import android.content.Context;
import android.util.Log;

import com.amap.navi.demo.common.Configs;
import com.amap.navi.demo.common.constants.EeyeConstants;
import com.amap.navi.demo.model.bean.CommandBean;
import com.amap.navi.demo.model.bean.DeviceCommand;
import com.amap.navi.demo.model.bean.ToolsConfigBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;


public class EeyeToolsManager extends Observable implements IModuleManager {
    private static final String TAG = "EeyeToolsManager";

    private Context mContext;
    private StateCommandController scController;

    public EeyeToolsManager(Context context) {
        mContext = context;

        scController = new StateCommandController(context);
    }

    @Override
    public void init() {
        Log.e(TAG, "init->");

        ToolsConfigBean toolsConfigBean = readToolsConfig();

        Log.e(TAG, "init->" + toolsConfigBean.toString());

        addObserver(ObserverFactory.createObserver(mContext, this, EeyeConstants.MODULE_DOWNLOAD));
        addObserver(ObserverFactory.createObserver(mContext, this, EeyeConstants.MODULE_UPLOAD));
        addObserver(ObserverFactory.createObserver(mContext, this, EeyeConstants.MODULE_SOCKET));
        addObserver(ObserverFactory.createObserver(mContext, this, EeyeConstants.MODULE_CATCHEYE));

        notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_INIT, "", toolsConfigBean));
    }

    @Override
    public void uninit() {

        notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_UNINIT, "", null));

        deleteObservers();
    }

    @Override
    public void onModuleChanged(int state, String str, Object obj) {
        Log.e(TAG, "onModuleChanged->state=" + state + ";str=" + str + ";obj=" + obj);

        switch (state) {
            //下载
            case EeyeConstants.MODULE_DOWNLOAD_START:

                break;
            case EeyeConstants.MODULE_DOWNLOAD_FINISH:
                //开始抓取电子眼数据

                //此时的str为eyeConfig的路径
                notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_START_CATCH, str, null));

                break;
            case EeyeConstants.MODULE_DOWNLOAD_PAUSE:


                break;
            //end-----

            //上传
            case EeyeConstants.MODULE_UPLOAD_START:


                break;
            case EeyeConstants.MODULE_UPLOAD_FINISH:
                //上报服务器，我这会儿处于空闲状态

                String s = scController.buildReadyCommand(null);

                //调用socket模块通知服务端
                notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_SENDMSG, s, null));

                break;
            case EeyeConstants.MODULE_UPLOAD_PAUSE:


                break;
            //end-----

            //socket
            case EeyeConstants.MODULE_SOCKET_CONNECT:
                //检查状态，上报服务器
                checkState();

                break;
            case EeyeConstants.MODULE_SOCKET_DISCONNECT:


                break;
            case EeyeConstants.MODULE_SOCKET_ONRECEIVED:
                //解析msg
                DeviceCommand deviceCommand = scController.parseCommand(str);

                dispatchCommad(deviceCommand);
                break;
            //end----

            //数据抓取
            case EeyeConstants.MODULE_CATCHEYE_START:


                break;
            case EeyeConstants.MODULE_CATCHEYE_FINISH:
                //调用上传模块上传结果
                notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_UPLOAD, "", null));

                break;
            case EeyeConstants.MODULE_CATCHEYE_PAUSE:


                break;
        }
    }

//1读取配置文件获取ip地址

    //2连接socket

    //3检查程序执行状态

    //4通过socket与服务端执行交互协议

    //5收到命令开始下载数据和eyeConfig

    //6下载完成后，开始抓取电子眼数据

    //7抓取完成后，上传结果

    //8从3开始重复操作
    private void checkState() {

        //检查状态
        int status = scController.getStatus();

        String s = "";
        switch (status) {
            case EeyeConstants.DEVICE_STATUS_READY:
                s = scController.buildReadyCommand(null);
                break;
            case EeyeConstants.DEVICE_STATUS_RUNNING:
                //通知服务端，程序正在抓取数据
                s = scController.buildRunningCommand(null);
                break;

        }

        Log.e(TAG, "status->" + status + ";msg->" + s);

        notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_SENDMSG, s, null));
    }

    private void dispatchCommad(DeviceCommand d) {

        switch (d.getStatus()) {
            case EeyeConstants.DEVICE_STATUS_READY:
                //接收到服务端指令暂时不处理；

                break;
            case EeyeConstants.DEVICE_STATUS_RUNNING:
                scController.saveStatus(EeyeConstants.DEVICE_STATUS_RUNNING);

                //检查是不是已经下载相应的文件
                //否-》进行下载
                //是-》》开始抓取数据


                notifyModule(new CommandBean(EeyeConstants.MODULE_MANAGER_DOWNLOAD, "", d));
                break;
        }
    }

    private void notifyModule(CommandBean c) {
        setChanged();
        notifyObservers(c);
    }


    private ToolsConfigBean readToolsConfig() {
        try {
            File file = new File(Configs.TOOLS_CONFIG_PATH);
            ToolsConfigBean toolsConfigBean;

            if (null != file && file.exists()) {

                String content = "";
                String line;
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(new FileInputStream(Configs.TOOLS_CONFIG_PATH)));
                while ((line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }


                if (content != "") {
                    toolsConfigBean = new ToolsConfigBean();
                    JSONObject jsonObject = new JSONObject(content);

                    if (jsonObject.has("ip")) {
                        String ip = jsonObject.getString("ip");
                        toolsConfigBean.setIp(ip);
                    } else {
                        toolsConfigBean.setIp(Configs.IP_DEFAULT);
                    }

                    if (jsonObject.has("httpPort")) {
                        int httpPort = jsonObject.getInt("httpPort");
                        toolsConfigBean.setHttpPort(httpPort);
                    } else {
                        toolsConfigBean.setHttpPort(Configs.HTTP_PORT_DEFAULT);
                    }

                    if (jsonObject.has("socketPort")) {
                        int socketPort = jsonObject.getInt("socketPort");
                        toolsConfigBean.setSocketPort(socketPort);
                    } else {
                        toolsConfigBean.setSocketPort(Configs.SOCKET_PORT_DEFAULT);
                    }


                } else {
                    toolsConfigBean = new ToolsConfigBean();
                    toolsConfigBean.setIp(Configs.IP_DEFAULT);
                    toolsConfigBean.setHttpPort(Configs.HTTP_PORT_DEFAULT);
                    toolsConfigBean.setSocketPort(Configs.SOCKET_PORT_DEFAULT);
                }

                return toolsConfigBean;
            } else {
                toolsConfigBean = new ToolsConfigBean();
                toolsConfigBean.setIp(Configs.IP_DEFAULT);
                toolsConfigBean.setHttpPort(Configs.HTTP_PORT_DEFAULT);
                toolsConfigBean.setSocketPort(Configs.SOCKET_PORT_DEFAULT);
            }

            return toolsConfigBean;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
