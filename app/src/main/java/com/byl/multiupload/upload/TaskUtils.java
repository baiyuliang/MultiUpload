package com.byl.multiupload.upload;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.upload.upload.TaskState;
import com.byl.multiupload.upload.upload.UploadManager;
import com.byl.multiupload.upload.upload.UploadTask;
import com.byl.multiupload.upload.upload.UploadTaskListener;

public class TaskUtils {

    public static void addUpdateUIListener(Context mContext, String taskId, View view, TextView tvProgress, ProgressBar progressBar) {
        UploadTask uploadTask = UploadManager.getInstance().getTask(taskId);
        if (uploadTask != null) {
            uploadTask.removeUploadTaskListener();
            uploadTask.setUploadTaskListener(new UploadTaskListener() {

                @Override
                public void onQueue(UploadTask uploadTask) {
                    if (view.getTag().equals(taskId)) {
                        tvProgress.setText("等待上传");
                    }
                }

                @Override
                public void onUploading(UploadTask uploadTask, long currentSize, long totalSize) {
                    if (view.getTag().equals(taskId)) {
                        if (totalSize > 0) {
                            int progress = (int) (currentSize * 100 / totalSize);
                            progressBar.setProgress(progress);
                            tvProgress.setText(progress + "%");
                        } else {
                            tvProgress.setText("0/%");
                            progressBar.setProgress(0);
                        }
                    }
                }

                @Override
                public void onCancel(UploadTask uploadTask) {

                }

                @Override
                public void onFinish(UploadTask uploadTask) {
                    if (view.getTag().equals(taskId)) {
                        tvProgress.setText("上传完成");
                    }
                }

                @Override
                public void onError(int error_code, UploadTask uploadTask, int code) {
                    if (view.getTag().equals(taskId)) {
                        tvProgress.setText("上传错误,点击重试");
                    }
                }
            });

        }
    }

    public static void showStatus(String taskId, TextView tvProgress, ProgressBar viewProgress) {
        UploadTask uploadTask = UploadManager.getInstance().getTask(taskId);
        if (uploadTask != null && uploadTask.getTaskEntity() != null) {
            TaskEntity taskEntity = uploadTask.getTaskEntity();
            if (taskEntity.getTotalSize() == 0) {
                viewProgress.setProgress(0);
                tvProgress.setText("等待上传");
            } else {
                int progress = (int) (taskEntity.getCompletedSize() * 100 / taskEntity.getTotalSize());
                viewProgress.setProgress(progress);
                tvProgress.setText(progress + "%");
            }
            switch (taskEntity.getTaskStatus()) {
                case TaskState.TASK_STATUS_INIT:

                    break;
                case TaskState.TASK_STATUS_QUEUE://等待中
                    tvProgress.setText("等待上传");
                    break;
                case TaskState.TASK_STATUS_UPLOADING://正在上传

                    break;
                case TaskState.TASK_STATUS_FINISH://已完成
                    tvProgress.setText("上传完成");
                    break;
                case TaskState.TASK_STATUS_ERROR:
                    tvProgress.setText("上传错误,点击重试");
                    break;
            }
        }
    }

    public static int getPercent(long completed, long total) {
        if (total > 0) {
            double fen = ((double) completed / (double) total) * 100;
            return (int) fen;
        }
        return 0;
    }
}
