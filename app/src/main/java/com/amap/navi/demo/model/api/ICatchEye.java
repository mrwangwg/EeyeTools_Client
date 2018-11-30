package com.amap.navi.demo.model.api;

public interface ICatchEye {

    void startCatch();

    void stopCatch();

    //返回跑数据的状态
    public interface IOnCatchEyeStateListener{

        void onCatchFinish(int taskId, String path);

    }

}
