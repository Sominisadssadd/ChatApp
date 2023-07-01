package com.example.chatapp.viewmodel;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.model.User;
import com.example.chatapp.view.ListOfUserActivity;
import com.google.android.play.core.integrity.model.a;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private FirebaseAuth auth;

    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    private MutableLiveData<List<User>> usersList = new MutableLiveData<>();

    public LiveData<List<User>> getUsersList() {
        return usersList;
    }

    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
        getUsers();
    }

    public void setUserOnline(boolean online) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        databaseReference.child(currentUser.getUid()).child("online").setValue(online);
    }

    private void getUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null) {
                    return;
                }
                List<User> users = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (!currentUser.getUid().equals(user.getId())) {
                            users.add(user);
                        }
                    }
                }
                if (!users.isEmpty()) {
                    usersList.setValue(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError errorMessage) {
                error.setValue(errorMessage.getMessage());
            }
        });
    }


    public void logoutUser() {
        setUserOnline(false);
        auth.signOut();
    }


}
