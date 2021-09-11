package com.byl.multiupload.db.daomanager;


import com.byl.multiupload.db.AppDatabase;
import com.byl.multiupload.db.dao.TaskDao;
import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.utils.LogUtils;

import java.util.List;

public class TaskDaoManager {

    private static TaskDaoManager mInstance;

    private TaskDaoManager() {
    }

    public static TaskDaoManager instance() {
        synchronized (TaskDaoManager.class) {
            if (mInstance == null) {
                mInstance = new TaskDaoManager();
            }
        }
        return mInstance;
    }

    private TaskDao getTaskDao() {
        return AppDatabase.getInstance().taskDao();
    }

    public void insert(TaskEntity entity) {
        add(entity);
    }

    private void add(TaskEntity entity) {
        getTaskDao().insert(entity);
    }

    public void update(TaskEntity entity) {
        getTaskDao().update(entity);
    }

    public void delete(TaskEntity entity) {
        getTaskDao().delete(entity);
    }

    public void deleteById(String taskId) {
        TaskEntity entity = getById(taskId);
        if (entity != null) getTaskDao().delete(entity);
    }

    public TaskEntity getById(String taskId) {
        return getTaskDao().getById(taskId);
    }

    public List<TaskEntity> getAll() {
        return getTaskDao().getAll();
    }

    public List<TaskEntity> getAllUploading() {
        return getTaskDao().getAllUploading();
    }
}
