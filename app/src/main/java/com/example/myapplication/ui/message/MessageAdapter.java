package com.example.myapplication.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Emtity.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;
    private List<Message> messages;
    private int currentUserId;  // ID của user hiện tại
    private OnItemClickListener listener;


    public MessageAdapter(List<Message> messages, int currentUserId, OnItemClickListener listener) {
        this.messages = messages;
        this.currentUserId = currentUserId;
        this.listener = listener;
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return (message.getSenderId() == currentUserId) ? VIEW_TYPE_RIGHT : VIEW_TYPE_LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new RightMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new LeftMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof RightMessageViewHolder) {
            ((RightMessageViewHolder) holder).tvMessage.setText(message.getContent());
        } else {
            ((LeftMessageViewHolder) holder).tvMessage.setText(message.getContent());
        }

        // Gán sự kiện click cho itemView
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(message);
            }
        });
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class LeftMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        LeftMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageLeft);
        }
    }

    static class RightMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        RightMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessageRight);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Message message);
    }

}
