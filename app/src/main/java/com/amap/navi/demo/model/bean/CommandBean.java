package com.amap.navi.demo.model.bean;

public class CommandBean {

    private int state;
    private String command;
    private Object obj;

    public CommandBean() {
    }

    public CommandBean(int state, String command, Object obj) {
        this.state = state;
        this.command = command;
        this.obj = obj;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "CommandBean{" +
                "state=" + state +
                ", command='" + command + '\'' +
                ", obj=" + obj +
                '}';
    }
}
