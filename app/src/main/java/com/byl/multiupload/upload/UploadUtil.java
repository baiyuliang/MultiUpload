package com.byl.multiupload.upload;


import com.byl.multiupload.db.daomanager.TaskDaoManager;
import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.upload.upload.TaskState;
import com.byl.multiupload.upload.upload.UploadManager;
import com.byl.multiupload.upload.upload.UploadTask;

import java.util.List;
import java.util.Random;

public class UploadUtil {

    /**
     * @param userId
     * @param modelId
     * @param pathList
     */
    public static void addUploadTask(int userId, int modelId, List<String> pathList) {
        for (int i = 0; i < pathList.size(); i++) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTaskId(System.currentTimeMillis() + "-" + new Random().nextInt(999));
            taskEntity.setUserId("" + userId);
            taskEntity.setModelId("" + modelId);
            taskEntity.setUrl(pathList.get(i));
            taskEntity.setRealUrl(pathList.get(i));
            taskEntity.setTaskStatus(TaskState.TASK_STATUS_INIT);
            taskEntity.setTotalSize(100);
            taskEntity.setTitle("item-" + (i + 1));
            TaskDaoManager.instance().insert(taskEntity);
            //在添加任务时，taskEntity必须要先获取到存入数据库后的id，这是更新数据的唯一凭证
            taskEntity = TaskDaoManager.instance().getById(taskEntity.getTaskId());
            UploadManager.getInstance().addTask(new UploadTask(taskEntity));
        }
    }

}
