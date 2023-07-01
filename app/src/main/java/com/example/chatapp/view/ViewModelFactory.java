package com.example.chatapp.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapp.viewmodel.ChatViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private String userId;
    private String otherUserId;

    public ViewModelFactory(String userId, String otherUserId) {
        this.userId = userId;
        this.otherUserId = otherUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(userId, otherUserId);
    }
}
