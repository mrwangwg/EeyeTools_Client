package com.amap.navi.demo.model.bean;

public class ToolsConfigBean {

    private String ip;
    private int httpPort;
    private int socketPort;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    @Override
    public String toString() {
        return "ToolsConfigBean{" +
                "ip='" + ip + '\'' +
                ", httpPort=" + httpPort +
                ", socketPort=" + socketPort +
                '}';
    }
}
