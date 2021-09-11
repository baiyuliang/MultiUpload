package com.byl.multiupload.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "upload_task")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String taskId;//任务id
    private String userId;//用户id，区分不同用户
    private String modelId;//如果上传是在某一条数据下进行的，可设置此字段做不同数据下的上传区分（如需其它字段，可自行添加）
    private String title;//
    private int taskType;//任务类型(区分上传文件类型，如图片，视频，文件等类型，如需要可设置此字段)
    private long totalSize;//上传文件总大小
    private long completedSize;//已上传大小
    private String url;//此url用作再源文件基础上进行处理后产生的新路径如压缩，不需要可不设置
    private String realUrl;//文件本地真实路径
    private int taskStatus;//任务状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCompletedSize() {
        return completedSize;
    }

    public void setCompletedSize(long completedSize) {
        this.completedSize = completedSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
}
