package com.example.chatapp.view;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.viewmodel.LoginViewModel;
import com.example.chatapp.viewmodel.RegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private TextView textViewForgotPassword;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewToRegisterActivity;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initViews();
        observeOn();
        clickableViews();

    }


    private void clickableViews() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailIsEmpty() && !passwordEmpty()) {
                    String email = parseAndGetEmail();
                    String password = parseAndGetPassword();
                    loginViewModel.loginUser(email, password);
                }
            }
        });


        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = parseAndGetEmail();
                Intent intentToResetPassword = ResetPasswordActivity.newIntent(MainActivity.this, email);
                startActivity(intentToResetPassword);
            }
        });

        textViewToRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToRegActivity = RegistrationActivity.newIntent(MainActivity.this);
                startActivity(intentToRegActivity);
            }
        });
    }


    private void observeOn() {
        loginViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Intent intentToUserList = ListOfUserActivity.getNewIntent(MainActivity.this,firebaseUser.getUid());
                startActivity(intentToUserList);
                finish();
            }
        });

        loginViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (!errorMessage.isEmpty()) {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private Boolean emailIsEmpty() {
        String email = editTextLogin.getText().toString();
        return email.isEmpty();
    }

    private Boolean passwordEmpty() {
        String password = editTextPassword.getText().toString();
        return password.isEmpty();
    }

    private String parseAndGetEmail() {
        return editTextLogin.getText().toString().trim();
    }

    private String parseAndGetPassword() {
        return editTextPassword.getText().toString().trim();
    }


    private void initViews() {
        editTextLogin = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewToRegisterActivity = findViewById(R.id.textViewGotoRegisterActivity);
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}