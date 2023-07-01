package com.example.chatapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.Message;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolderMessage> {

    private static final int receiverMessage = 101;
    private static final int senderMessage = 100;

    public MessageAdapter(String currentUserId) {
        this.currentUserID = currentUserId;
    }

    private String currentUserID;

    private List<Message> messages = new ArrayList<>();

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int messageStatus;
        if (viewType == senderMessage) {
            messageStatus = R.layout.my_message_item;
        } else {
            messageStatus = R.layout.other_message_item;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(messageStatus, parent, false);
        return new ViewHolderMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessage holder, int position) {
        Message currentMessage = messages.get(position);

        //если в сообщение была цифра, то он сразу вылетит
        holder.textViewMessage.setText(String.valueOf(currentMessage.getMessage()));

    }

    @Override
    public int getItemViewType(int position) {
        Message currentMessage = messages.get(position);

        if (currentMessage.getSenderId().equals(currentUserID)) {
            return senderMessage;
        } else {
           return  receiverMessage;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolderMessage extends RecyclerView.ViewHolder {

        private TextView textViewMessage;

        public ViewHolderMessage(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMyMessage);
        }
    }
}
