package com.example.chatapp.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {


    private onUserItemClick onUserItemClick;

    public void setOnUserItemClick(onUserItemClick itemClick) {
        onUserItemClick = itemClick;
    }

    private List<User> users = new ArrayList<>();

    public void setUsers(List<User> usersList) {
        users = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {

        User currentUser = users.get(position);

        int bgResId;
        if (currentUser.isOnline()) {
            bgResId = R.drawable.is_active;
        } else {
            bgResId = R.drawable.is_not_active;
        }

        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), bgResId);
        holder.activeStatus.setBackground(background);

        String userInfo = String.format("%s %s %s", currentUser.getName(), currentUser.getSerName(),
                currentUser.getAge());
        holder.textViewInfoUser.setText(userInfo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUserItemClick.onUserClickListener(currentUser);
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewInfoUser;
        private View activeStatus;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInfoUser = itemView.findViewById(R.id.textViewUserInfo);
            activeStatus = itemView.findViewById(R.id.activeStatusView);
        }
    }

    interface onUserItemClick {
        void onUserClickListener(User user);
    }
}
