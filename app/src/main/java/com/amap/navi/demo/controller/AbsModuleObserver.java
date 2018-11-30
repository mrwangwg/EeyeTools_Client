package com.amap.navi.demo.controller;

import java.util.Observer;

public abstract class AbsModuleObserver implements Observer{

    private IModuleManager manager;

    public void setManager(IModuleManager manager) {
        this.manager = manager;
    }

    public void notifyManager(int state, String str, Object obj){
        manager.onModuleChanged(state, str, obj);
    }


}
