package com.amap.navi.demo.model.bean;

public class DeviceCommand {

    private String guid;
    private int status;
    private String configPath;
    private String sourceData;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    @Override
    public String toString() {
        return "DeviceCommand{" +
                "guid='" + guid + '\'' +
                ", status=" + status +
                ", configPath='" + configPath + '\'' +
                ", sourceData='" + sourceData + '\'' +
                '}';
    }

}
