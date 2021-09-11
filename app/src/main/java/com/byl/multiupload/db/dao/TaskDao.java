package com.byl.multiupload.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.byl.multiupload.db.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("select * from upload_task where taskId= :taskId")
    TaskEntity getById(String taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskEntity taskEntity);

    @Query("select * from upload_task")
    List<TaskEntity> getAll();

    @Query("select * from upload_task where taskStatus != 5")
    List<TaskEntity> getAllUploading();

    @Update
    void update(TaskEntity entity);

    @Delete
    void delete(TaskEntity entity);

}
