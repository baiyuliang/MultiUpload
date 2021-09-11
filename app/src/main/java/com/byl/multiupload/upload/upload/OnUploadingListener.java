package com.byl.multiupload.upload.upload;

public interface OnUploadingListener {
    void onDownLoading(String taskId, String progress);

    void onDownLoadComplete(String _taskId);
}
