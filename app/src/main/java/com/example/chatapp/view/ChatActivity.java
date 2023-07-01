package com.example.chatapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.example.chatapp.viewmodel.ChatViewModel;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String currentUserKey = "currentUser";
    private static final String OtherUserKey = "otherUser";


    private EditText editTextMessageSendField;
    private TextView textViewOtherManName;
    private View activeStatus;
    private RecyclerView recyclerViewMessages;
    private ImageView sendMessageButton;
    private MessageAdapter messageAdapter;
    private String currentUserId;
    private String otherUserId;
    private ChatViewModel chatViewModel;
    private ViewModelFactory viewModelFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        currentUserId = getIntent().getStringExtra(currentUserKey);
        otherUserId = getIntent().getStringExtra(OtherUserKey);

        viewModelFactory = new ViewModelFactory(currentUserId, otherUserId);
        chatViewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        messageAdapter = new MessageAdapter(currentUserId);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfed = editTextMessageSendField.getText().toString();
                Message message = new Message(mfed, currentUserId, otherUserId);
                chatViewModel.sendMessage(message);
            }
        });

        observableViewModel();
        initRecyclerView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setUserOnline(false);
    }

    private void observableViewModel() {
        chatViewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
            }
        });

        chatViewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                textViewOtherManName.setText(user.getName());

                int idColor;
                if(user.isOnline()){
                    idColor = R.drawable.is_active;
                }else{
                    idColor = R.drawable.is_not_active;
                }

                Drawable background = ContextCompat.getDrawable(ChatActivity.this,idColor);
                activeStatus.setBackground(background);


            }
        });

        chatViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        chatViewModel.getTrueSentMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean wasSend) {
                if(wasSend){
                    editTextMessageSendField.setText("");
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);
    }


    private void initViews() {
        editTextMessageSendField = findViewById(R.id.editTextFieldForMessage);
        textViewOtherManName = findViewById(R.id.textViewNameOfOtherUser);
        activeStatus = findViewById(R.id.activeStatusOfOtherUser);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        sendMessageButton = findViewById(R.id.imageViewSendMessage);
    }

    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(currentUserKey, currentUserId);
        intent.putExtra(OtherUserKey, otherUserId);
        return intent;
    }
}