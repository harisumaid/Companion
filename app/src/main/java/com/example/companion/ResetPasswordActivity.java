package com.example.companion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ContentLoadingProgressBar progressBar;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.sumbit_reset_button);
    }

    public void resetPassword(View view) {
        submit.setClickable(false);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        final TextInputEditText emailEditText = findViewById(R.id.email_textinput_reset);
        final String email = emailEditText.getText().toString();
        if(!email.isEmpty()) {
            progressBar = findViewById(R.id.loading_reset);
            progressBar.show();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.hide();
                                Log.d("reset", "Email sent.");
                                Toast.makeText(ResetPasswordActivity.this, "Password reset link sent to your email id \n Please check your email", Toast.LENGTH_SHORT).show();
                                getBackToLogin();
                            } else {
                                progressBar.hide();
                                emailEditText.setError(task.getException().getMessage());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Email Id can't be empty \n Please enter a valid email id", Toast.LENGTH_SHORT).show();
        }
        submit.setClickable(true);
    }

    private void getBackToLogin() {
        finish();
    }
}
