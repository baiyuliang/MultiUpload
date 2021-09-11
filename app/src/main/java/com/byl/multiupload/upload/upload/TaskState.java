package com.byl.multiupload.upload.upload;

public class TaskState {
    public static final int TASK_STATUS_INIT = 0;
    public static final int TASK_STATUS_QUEUE = 1;
    public static final int TASK_STATUS_UPLOADING = 2;
    public static final int TASK_STATUS_CANCEL = 3;
    public static final int TASK_STATUS_ERROR = 4;
    public static final int TASK_STATUS_FINISH = 5;
}
