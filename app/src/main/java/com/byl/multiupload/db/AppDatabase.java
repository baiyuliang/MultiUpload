package com.byl.multiupload.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.byl.multiupload.App;
import com.byl.multiupload.db.dao.TaskDao;
import com.byl.multiupload.db.entity.TaskEntity;


/**
 * 表名不区分大小写
 */
@Database(entities = {TaskEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();

    public static AppDatabase getInstance() {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(App.getContext(), AppDatabase.class, "mu.db")
                        .fallbackToDestructiveMigration()//崩溃后重建
                        .allowMainThreadQueries() //允许主线程访问数据库
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract TaskDao taskDao();

}
