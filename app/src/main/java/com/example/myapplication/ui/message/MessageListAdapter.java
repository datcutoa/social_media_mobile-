package com.example.myapplication.ui.message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ComponentMessage componentMessage);
    }
    private List<ComponentMessage> data;
    private final OnItemClickListener listener;

    public MessageListAdapter(List<ComponentMessage> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComponentMessage item = data.get(position);
        holder.tvName.setText(item.getName());
        holder.tvLastMessage.setText(item.getLastMessage());
        holder.tvTime.setText(item.getLastMessageTime());

        // Gán sự kiện click vào itemView
        holder.itemView.setOnClickListener(v -> {
            Log.d("MessageListAdapter", "Click event triggered at position: " + position);
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        // Đảm bảo item không bị chặn click
        holder.itemView.setClickable(true);
        holder.itemView.setFocusable(false);
    }






    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ComponentMessage> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvName, tvLastMessage, tvTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvTime);

        }
    }
}
