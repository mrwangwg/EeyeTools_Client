package com.amap.navi.demo.controller;

public interface IModuleManager {

    void init();

    void uninit();

    void onModuleChanged(int state, String str, Object obj);

}
