package com.example.chatapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.viewmodel.RegisterViewModel;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText serNameEditText;
    private EditText ageEditText;
    private Button buttonRegistration;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        initViews();
        observableEvents();

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonReg) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String serName = serNameEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());
                if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !serName.isEmpty()) {

                    registerViewModel.registerUser(email, password,name,serName, age);

                } else {
                    Toast.makeText(RegistrationActivity.this, "Заполните поля корректно",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void observableEvents() {
        registerViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
              if(firebaseUser != null){
                  Intent intent = ListOfUserActivity.getNewIntent(RegistrationActivity.this,firebaseUser.getUid());
                  startActivity(intent);
                  finish();
              }
            }
        });

        registerViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        serNameEditText = findViewById(R.id.serNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        buttonRegistration = findViewById(R.id.buttonRegister);

    }


    public static Intent newIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }

}