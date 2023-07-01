package com.example.chatapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {


    //trueSentMessage нужн затем, чтоб если успешно отправили поле отчищается сообщения
    private MutableLiveData<Boolean> trueSentMessage = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> otherUser = new MutableLiveData<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersReferences = database.getReference("users");
    private DatabaseReference messagesReference = database.getReference("messages");

    private String userID;
    private String otherUserID;


    public ChatViewModel(String userId, String otherUserId) {
        this.userID = userId;
        this.otherUserID = otherUserId;
        //отслеживать состояние собеседника. Если он вышел, то не в сети. И получить его имя для TextView
        usersReferences.child(otherUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError errorMessage) {
                error.setValue(errorMessage.getMessage());
            }
        });

        messagesReference.child(userID).child(otherUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messagesList = new ArrayList<>();
                for (DataSnapshot message : snapshot.getChildren()) {
                    messagesList.add(message.getValue(Message.class));
                }
                messages.setValue(messagesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError errorMessage) {
                error.setValue(errorMessage.getMessage());
            }
        });


    }


    public void setUserOnline(boolean online) {
        usersReferences.child(userID).child("online").setValue(online);
    }


    public void sendMessage(Message message) {
        //записываем сообщение сначала пользователю, который отправил, это, а потом его собеседнику
        messagesReference
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        messagesReference
                                .child(message.getReceiverId())
                                .child(message.getSenderId())
                                .push()
                                .setValue(message);
                        trueSentMessage.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }


    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getTrueSentMessage() {
        return trueSentMessage;
    }
}
