package com.amap.navi.demo.model.bean;

public class HttpTaskBean {

    private long fileSize;//文件总的大小

    private String url;//文件请求的url

    private Type type;//哪种状态下的HttpTaskBean包括 DOWNLOAD UPLOAD

    private int threadId;//线程id

    private long startFlag;//任务开始的标记

    private long progressFlag;

    private long endFlag;//任务结束的标记

    private String filePath;//文件的位置

    private String tmpFilePath;//临时文件的位置

    public enum Type{
        DOWNLOAD, UPLOAD
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(long startFlag) {
        this.startFlag = startFlag;
    }

    public long getProgressFlag() {
        return progressFlag;
    }

    public void setProgressFlag(long progressFlag) {
        this.progressFlag = progressFlag;
    }

    public long getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(long endFlag) {
        this.endFlag = endFlag;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTmpFilePath() {
        return tmpFilePath;
    }

    public void setTmpFilePath(String tmpFilePath) {
        this.tmpFilePath = tmpFilePath;
    }

    @Override
    public String toString() {
        return "HttpTaskBean{" +
                "fileSize=" + fileSize +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", threadId=" + threadId +
                ", startFlag=" + startFlag +
                ", progressFlag=" + progressFlag +
                ", endFlag=" + endFlag +
                ", filePath='" + filePath + '\'' +
                ", tmpFilePath='" + tmpFilePath + '\'' +
                '}';
    }
}
