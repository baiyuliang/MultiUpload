package com.byl.multiupload.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byl.multiupload.R;
import com.byl.multiupload.db.entity.TaskEntity;
import com.byl.multiupload.upload.TaskUtils;
import com.byl.multiupload.upload.upload.UploadManager;
import com.byl.multiupload.upload.upload.UploadTask;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.MyViewHolder> {

    Activity mContext;
    List<TaskEntity> listData;

    public UploadListAdapter(Activity context, List<TaskEntity> listData) {
        this.mContext = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UploadListAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_uploading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskEntity taskEntity = listData.get(position);
        holder.tv_title.setText(taskEntity.getTitle());
        holder.itemView.setTag(taskEntity.getTaskId());
        TaskUtils.showStatus(taskEntity.getTaskId(), holder.tv_progress, holder.mProgressBar);
        UploadTask uploadTask = UploadManager.getInstance().getTask(taskEntity.getTaskId());
        TaskUtils.addUpdateUIListener(
                mContext,
                taskEntity.getTaskId(),
                holder.itemView,
                holder.tv_progress,
                holder.mProgressBar
        );
        holder.tv_cancel.setOnClickListener(v -> {
            UploadManager.getInstance().cancelTask(uploadTask);
            listData.remove(position);
            notifyDataSetChanged();
        });
        holder.itemView.setOnClickListener(v -> {
            UploadManager.getInstance().addTask(uploadTask);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ProgressBar mProgressBar;
        TextView tv_progress;
        TextView tv_cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            mProgressBar = itemView.findViewById(R.id.mProgressBar);
            tv_progress = itemView.findViewById(R.id.tv_progress);
            tv_cancel = itemView.findViewById(R.id.tv_cancel);
        }
    }

}
