package com.byl.multiupload.upload.event;

public class EventUpload {
    String taskId;
    String activeId;

    public EventUpload(String taskId, String activeId) {
        this.taskId = taskId;
        this.activeId = activeId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }
}
