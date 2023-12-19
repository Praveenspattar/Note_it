package com.myapps.note_it;

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

public class CreateAccActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText,confirmPasswordET;
    Button createacc_btn;
    ProgressBar progressBar;
    TextView loginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordET = findViewById(R.id.conform_password);
        createacc_btn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginTv = findViewById(R.id.login_text_view_btn);

        createacc_btn.setOnClickListener(v -> createAccount());
        loginTv.setOnClickListener(v -> finish());
    }

    private void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if (!isValidated){
            return;
        }

        createAccountinFirebase(email,password);

    }

    void  createAccountinFirebase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            //create account is done
                            Utility.showtoast(CreateAccActivity.this, "Successfully created account, Check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            //failure
                            Utility.showtoast(CreateAccActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );

    }

    void changeInProgress(boolean inProgress){
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createacc_btn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            createacc_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password,String confirmPassword){
        // validation of data by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            passwordEditText.setError("password is short");
            return false;
        }
        if (!password.equals(confirmPassword)){
            confirmPasswordET.setError("Password not matches");
            return false;
        }
        return true;
    }
}