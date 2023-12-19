package com.myapps.note_it;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button login_btn;
    ProgressBar progressBar;
    TextView createaccountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        login_btn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createaccountTv = findViewById(R.id.create_account_text_view_btn);

        login_btn.setOnClickListener(v -> loginUser());
        createaccountTv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateAccActivity.class)));

    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email,password);
        if (!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);

    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Utility.showtoast(LoginActivity.this,"Email not verified, Please verify your email.");
                    }
                }else {
                    //login failed
                    Utility.showtoast(LoginActivity.this,task.getException().getLocalizedMessage());

                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            login_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password){
        // validation of data by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            passwordEditText.setError("password is short");
            return false;
        }
        return true;
    }
}