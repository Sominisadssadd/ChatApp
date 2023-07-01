package com.example.chatapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.viewmodel.LoginViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String PARAMS_KEY = "email";

    private EditText editTextEmailResetField;
    private Button resetPasswordButton;

    private ResetPasswordViewModel resetPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observableEvents();
        editTextEmailResetField = findViewById(R.id.editTextResetPassword);
        resetPasswordButton = findViewById(R.id.buttonResetPassword);

        String email = getIntent().getStringExtra(PARAMS_KEY);
        editTextEmailResetField.setText(email);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValueInEmailEditText = editTextEmailResetField.getText().toString();
                resetPasswordViewModel.sendResetPasswordToEmail(currentValueInEmailEditText);
            }
        });
    }


    public void observableEvents() {
        resetPasswordViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        resetPasswordViewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                Toast.makeText(ResetPasswordActivity.this,
                        "Письмо было отправленно вам на почту",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(PARAMS_KEY, email);
        return intent;
    }
}