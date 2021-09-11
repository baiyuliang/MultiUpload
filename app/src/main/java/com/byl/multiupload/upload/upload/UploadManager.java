package com.byl.multiupload.upload.upload;

import com.byl.multiupload.db.daomanager.TaskDaoManager;
import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.upload.UploadClientManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 上传管理
 */
public class UploadManager {
    private static final int MAX_THREAD_COUNT = 16;
    private static UploadManager mInstance;
    private int mThreadCount = 3;
    private ThreadPoolExecutor mExecutor;
    private Map<String, UploadTask> allTaskList, currentTaskList;
    private LinkedBlockingQueue<Runnable> mQueue;

    public static synchronized UploadManager getInstance() {
        if (mInstance == null) {
            mInstance = new UploadManager();
        }

        return mInstance;
    }

    public void init() {
        init(getAppropriateThreadCount());
    }

    /**
     * 初始化线程池
     *
     * @param threadCount
     */
    public void init(int threadCount) {
        mThreadCount = threadCount < 1 ? 1 : Math.min(threadCount, MAX_THREAD_COUNT);
        mExecutor = new ThreadPoolExecutor(
                mThreadCount,
                mThreadCount,
                20,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
        allTaskList = new HashMap<>();
        currentTaskList = new HashMap<>();
        mQueue = (LinkedBlockingQueue<Runnable>) mExecutor.getQueue();
    }

    private void initIfNull() {
        if (allTaskList == null || currentTaskList == null)
            UploadClientManager.getInstance().init();
    }

    /**
     * @return generate the appropriate thread count.
     */
    private int getAppropriateThreadCount() {
        return Runtime.getRuntime().availableProcessors() * 2 + 1;
    }

    /**
     * 添加上传任务到下载队列
     *
     * @param task
     */
    public void addTask(UploadTask task) {
        initIfNull();
        TaskEntity taskEntity = task.getTaskEntity();
        if (taskEntity == null) return;
        if (!currentTaskList.containsKey(taskEntity.getTaskId()) || taskEntity.getTaskStatus() != TaskState.TASK_STATUS_UPLOADING) {
            allTaskList.remove(taskEntity.getTaskId());
            currentTaskList.remove(taskEntity.getTaskId());
            task.init();
            allTaskList.put(taskEntity.getTaskId(), task);
            currentTaskList.put(taskEntity.getTaskId(), task);
            if (!mQueue.contains(task)) mExecutor.execute(task);
            if (mExecutor.getTaskCount() > mThreadCount) task.queue();
        }
    }

    public UploadTask getTask(String taskId) {
        initIfNull();
        UploadTask currTask = allTaskList.get(taskId);
        if (currTask == null) {
            TaskEntity entity = TaskDaoManager.instance().getById(taskId);
            if (entity != null) {
                int status = entity.getTaskStatus();
                currTask = new UploadTask(entity);
                if (status != TaskState.TASK_STATUS_FINISH) {
                    allTaskList.put(taskId, currTask);
                }
            }
        }
        return currTask;
    }

    public void cancelTask(UploadTask task) {
        if (task == null) {
            return;
        }
        TaskEntity taskEntity = task.getTaskEntity();
        if (taskEntity != null) {
            if (mQueue.contains(task)) {
                mQueue.remove(task);
            }
            allTaskList.remove(taskEntity.getTaskId());
            currentTaskList.remove(taskEntity.getTaskId());
            task.cancel();
        }
    }

    public void removeTask(String taskId) {
        allTaskList.remove(taskId);
        currentTaskList.remove(taskId);
    }

}
