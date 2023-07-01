package com.example.chatapp.view;

import static com.example.chatapp.view.ChatActivity.currentUserKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.example.chatapp.viewmodel.UsersViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListOfUserActivity extends AppCompatActivity {


    private UsersViewModel usersViewModel;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_user);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        currentUserID = getIntent().getStringExtra(currentUserKey);

        usersViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    Intent intentToLogin = MainActivity.newIntent(ListOfUserActivity.this);
                    startActivity(intentToLogin);
                    finish();
                }

            }
        });
        initRecyclerView();
        observableEvents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        usersViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersViewModel.setUserOnline(false);

    }

    private void initRecyclerView() {


        recyclerViewUsers = findViewById(R.id.recyclerViewOfUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter();
        usersViewModel.getUsersList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersAdapter.setUsers(users);
            }
        });
        usersAdapter.setOnUserItemClick(new UsersAdapter.onUserItemClick() {
            @Override
            public void onUserClickListener(User user) {
                Intent intent = ChatActivity.newIntent(ListOfUserActivity.this, currentUserID, user.getId());
                startActivity(intent);
            }
        });
        recyclerViewUsers.setAdapter(usersAdapter);

    }

    private void observableEvents() {
        usersViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(ListOfUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sing_out_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signOutDote) {
            usersViewModel.logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getNewIntent(Context context, String currentUserID) {
        Intent intent = new Intent(context, ListOfUserActivity.class);
        intent.putExtra(currentUserKey, currentUserID);
        return intent;
    }
}