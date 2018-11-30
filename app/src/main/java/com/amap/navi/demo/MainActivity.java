package com.amap.navi.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.navi.demo.controller.EeyeToolsManager;
import com.amap.navi.demo.controller.IModuleManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private IModuleManager mModuleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        String saveFilePath = "/mnt/sdcard/CameraData/hebei/02/02.track";

        File file = new File(saveFilePath);
        boolean mkSuccess = false;
        while(!mkSuccess) {
            Log.e("TAG", "111111");
            mkSuccess = file.getParentFile().mkdirs();
            Log.e("TAG", "2222mkSuccess->"+mkSuccess);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("TAG", "3333mkSuccess->"+mkSuccess);
        }*/

        mModuleManager = new EeyeToolsManager(getApplicationContext());

        mModuleManager.init();
    }

    @Override
    protected void onDestroy() {

        mModuleManager.uninit();

        super.onDestroy();
    }
}
