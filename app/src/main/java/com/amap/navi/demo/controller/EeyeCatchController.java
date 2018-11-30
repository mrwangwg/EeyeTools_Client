package com.amap.navi.demo.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amap.navi.demo.common.Configs;
import com.amap.navi.demo.common.constants.EeyeConstants;
import com.amap.navi.demo.model.bean.CommandBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

public class EeyeCatchController extends AbsModuleObserver {

    private static final String TAG = "EeyeCatchController";

    private Context mContext;

    public EeyeCatchController(Context context) {
        mContext = context;
    }

    @Override
    public void update(Observable o, Object arg) {

        CommandBean c = (CommandBean) arg;

        switch (c.getState()) {

            case EeyeConstants.MODULE_MANAGER_START_CATCH:
                //读取配置文件
                //发送广播
                Log.e(TAG, "catch start!!!");
                startCatch();

                break;
        }
    }

    public void startCatch() {
        new Thread() {
            @Override
            public void run() {

                ArrayList<String> adbList = readEyeConfig2List();

                if (adbList == null || adbList.size() == 0)
                    return;

                exeAdb(adbList);
                super.run();
            }
        }.start();
    }

    private ArrayList<String> readEyeConfig2List() {
        ArrayList<String> adbList = new ArrayList<String>();

        File file = new File(Configs.EYECONFIG_PATH);

        if (!file.exists() || file.length() == 0)
            return null;

        try {
            FileReader reader = new FileReader(Configs.EYECONFIG_PATH);

            BufferedReader br = new BufferedReader(reader);

            String line;

            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;

                if ((lineNum & 1) == 0) {
                    adbList.add(line);
                }
            }

            reader.close();
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adbList;
    }

    private void exeAdb(ArrayList<String> adbList) {

        try {
            for (int i = 0; i < adbList.size(); i++) {

                String cmd = adbList.get(i);

                String[] split = cmd.split(" ");

                String param = split[4].replaceAll("\"", "");

                Log.e(TAG, "runParameter->" + param + ";;;action->" + split[5]);

                Intent intent = new Intent();

                intent.setAction(split[5]);

                Bundle bundle = new Bundle();

                bundle.putString("runParameter", param);

                intent.putExtras(bundle);

                mContext.startService(intent);

                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
