package com.byl.multiupload;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.byl.multiupload.adapter.UploadListAdapter;
import com.byl.multiupload.db.daomanager.TaskDaoManager;
import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.upload.event.EventUpload;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class UploadingActivity extends AppCompatActivity {

    List<TaskEntity> taskList;
    UploadListAdapter uploadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);
        EventBus.getDefault().register(this);
        new Thread(() -> {
            taskList = TaskDaoManager.instance().getAllUploading();
            runOnUiThread(() -> {
                uploadListAdapter = new UploadListAdapter(this, taskList);
                RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(uploadListAdapter);
            });
        }).start();
    }

    @Subscribe
    public void onEventMainThread(EventUpload eventUpload) {
        for (int i = 0; i < taskList.size(); i++) {
            if (eventUpload.getTaskId().equals(taskList.get(i).getTaskId())) {
                taskList.remove(i);
                uploadListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}