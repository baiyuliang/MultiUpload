package com.byl.multiupload.upload.upload;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.byl.multiupload.App;
import com.byl.multiupload.db.daomanager.TaskDaoManager;
import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.upload.UploadClient;
import com.byl.multiupload.upload.event.EventUpload;

import org.greenrobot.eventbus.EventBus;


public class UploadTask implements Runnable {
    private UploadClient uploadClient;
    private UploadTaskListener uploadTaskListener;
    private TaskEntity mTaskEntity;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int code = msg.what;
            switch (code) {
                case TaskState.TASK_STATUS_QUEUE:
                    if (uploadTaskListener != null)
                        uploadTaskListener.onQueue(UploadTask.this);
                    break;
                case TaskState.TASK_STATUS_UPLOADING:
                    if (uploadTaskListener != null)
                        uploadTaskListener.onUploading(UploadTask.this, mTaskEntity.getCompletedSize(), mTaskEntity.getTotalSize());
                    break;
                case TaskState.TASK_STATUS_CANCEL:
                    if (uploadTaskListener != null)
                        uploadTaskListener.onCancel(UploadTask.this);
                    break;
                case TaskState.TASK_STATUS_ERROR:
                    int response_code = (int) msg.obj;
                    if (uploadTaskListener != null)
                        uploadTaskListener.onError(response_code, UploadTask.this, TaskState.TASK_STATUS_ERROR);
                    break;
                case TaskState.TASK_STATUS_FINISH:
                    if (uploadTaskListener != null)
                        uploadTaskListener.onFinish(UploadTask.this);
                    //发送上传完成通知
                    EventBus.getDefault().post(new EventUpload(mTaskEntity.getTaskId(), mTaskEntity.getModelId()));
                    break;
                default:
                    break;
            }
        }
    };

    public UploadTask(TaskEntity taskEntity) {
        mTaskEntity = taskEntity;
    }

    public void setUploadTaskListener(UploadTaskListener uploadTaskListener) {
        this.uploadTaskListener = uploadTaskListener;
    }

    public void removeUploadTaskListener() {
        this.uploadTaskListener = null;
    }

    public TaskEntity getTaskEntity() {
        return mTaskEntity;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void run() {
        if (mTaskEntity == null || TextUtils.isEmpty(mTaskEntity.getTaskId()) || TextUtils.isEmpty(mTaskEntity.getUrl())) {
            return;
        }

        sendHandlerMessage(0, TaskState.TASK_STATUS_UPLOADING);
        mTaskEntity.setTaskStatus(TaskState.TASK_STATUS_UPLOADING);
        TaskDaoManager.instance().update(mTaskEntity);

        uploadClient = new UploadClient();
        uploadClient.setOnUploadProgress((currentSize, totalSize) -> {
            mTaskEntity.setCompletedSize(currentSize);
            mTaskEntity.setTotalSize(totalSize);
            TaskDaoManager.instance().update(mTaskEntity);
            sendHandlerMessage(0, TaskState.TASK_STATUS_UPLOADING);
        });
        uploadClient.upload(mTaskEntity.getUrl(), new UploadClient.OnUploadListener() {
            @Override
            public void onSuccess() {
                mTaskEntity.setTaskStatus(TaskState.TASK_STATUS_FINISH);
                sendHandlerMessage(0, TaskState.TASK_STATUS_FINISH);
                TaskDaoManager.instance().update(mTaskEntity);
//                TaskDaoManager.instance().delete(mTaskEntity);//正常情况下，上传完毕后应从数据库删除任务，demo因测试隐掉这一步
                UploadManager.getInstance().removeTask(mTaskEntity.getTaskId());
            }

            @Override
            public void onError() {
                mTaskEntity.setTaskStatus(TaskState.TASK_STATUS_ERROR);
                TaskDaoManager.instance().update(mTaskEntity);
                sendHandlerMessage(-1, TaskState.TASK_STATUS_ERROR);
                Looper.prepare();
                Toast.makeText(App.getContext(), "上传失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

    void sendHandlerMessage(int response_code, int status) {
        Message message = new Message();
        message.what = status;
        message.obj = response_code;
        mHandler.sendMessage(message);
    }

    public void init() {
        mTaskEntity.setTaskStatus(TaskState.TASK_STATUS_INIT);
        sendHandlerMessage(0, TaskState.TASK_STATUS_INIT);
    }

    public void queue() {
        mTaskEntity.setTaskStatus(TaskState.TASK_STATUS_QUEUE);
        sendHandlerMessage(0, TaskState.TASK_STATUS_QUEUE);
    }

    public void cancel() {
        if (uploadClient != null) uploadClient.cancel();
        mTaskEntity.setTaskStatus(TaskState.TASK_STATUS_CANCEL);
        TaskDaoManager.instance().delete(TaskDaoManager.instance().getById(mTaskEntity.getTaskId()));//从下载任务数据库中删除
        sendHandlerMessage(0, TaskState.TASK_STATUS_CANCEL);
    }
}