package com.amap.navi.demo.controller;

import android.content.Context;

import com.amap.navi.demo.common.constants.EeyeConstants;
import com.amap.navi.demo.common.util.SPUtils;
import com.amap.navi.demo.model.bean.CommandBean;
import com.amap.navi.demo.model.bean.DeviceCommand;
import com.mapbar.android.guid.GUIDController;

import org.json.JSONException;
import org.json.JSONObject;

public class StateCommandController {

    private static final String DEVICE_STATUS_READY = "ready";
    private static final String DEVICE_STATUS_RUNNING = "running";

    private static final String DEVICE_STATUS = "deviceStatus";

    private String mGuid;
    private SPUtils spUtils;

    public StateCommandController(Context context) {
        mGuid = GUIDController.getRandomGUID(context);
        spUtils = new SPUtils(context, DEVICE_STATUS);
    }

    //解析交互协议
    public DeviceCommand parseCommand(String command) {

        DeviceCommand deviceCommand = new DeviceCommand();

        try {
            JSONObject jsonObject = new JSONObject(command);

            if (jsonObject.has("guid")) {
                String guid = jsonObject.getString("guid");
                deviceCommand.setGuid(guid);
            }

            if (jsonObject.has("status")) {
                String status = jsonObject.getString("status");
                if(DEVICE_STATUS_READY.equals(status)){
                    deviceCommand.setStatus(EeyeConstants.DEVICE_STATUS_READY);
                }else{
                    deviceCommand.setStatus(EeyeConstants.DEVICE_STATUS_RUNNING);
                }
            }

            if (jsonObject.has("configPath")) {
                String configPath = jsonObject.getString("configPath");
                deviceCommand.setConfigPath(configPath);
            }

            if (jsonObject.has("sourceData")) {
                String sourceData = jsonObject.getString("sourceData");
                deviceCommand.setSourceData(sourceData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceCommand;
    }

    //生成交互协议
    public String buildReadyCommand(DeviceCommand deviceCommand) {
//        saveStatus(EeyeConstants.DEVICE_STATUS_READY);
        return "{\"guid\":"+"\""+mGuid+"\","
                +"\"status\":"+"\""+DEVICE_STATUS_READY+"\"}";
    }

    //生成交互协议
    public String buildRunningCommand(DeviceCommand deviceCommand) {
//        saveStatus(EeyeConstants.DEVICE_STATUS_RUNNING);

        if(deviceCommand==null)
            return "{\"guid\":"+"\""+mGuid+"\","
                    +"\"status\":"+"\""+DEVICE_STATUS_RUNNING+"\"}";
        else
            return "{\"guid\":"+"\""+mGuid+"\","
                +"\"status\":"+"\""+DEVICE_STATUS_RUNNING+"\","
                +"\"configPath\":"+"\""+deviceCommand.getConfigPath()+"\","
                +"\"sourceData\":"+"\""+deviceCommand.getSourceData()+"\"}";
    }

    public void saveStatus(int status) {
        spUtils.putInt("status", status);
    }

    public int getStatus() {
        return spUtils.getInt("status", EeyeConstants.DEVICE_STATUS_READY);
    }

}
