package com.example.companion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.companion.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    String email;
    String password;
    private FirebaseAuth mAuth;
    TextView warningLogin;
    ContentLoadingProgressBar progressBar;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        submit = findViewById(R.id.activity_submit_button_login);
        progressBar = findViewById(R.id.loading_login);
        progressBar.hide();
        if(mAuth.getCurrentUser()!=null){
            onLoginComplete();
        }
    }

    public void clickLogin(View view) {
        submit.setClickable(false);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        final TextInputEditText emailEditText = findViewById(R.id.email_textinput_login);
        email = emailEditText.getText().toString();
        final TextInputEditText passwordEditText = findViewById(R.id.password_textinput_login);
        password = passwordEditText.getText().toString();

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Password or email can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("success","Login Successful");
                            checkAndWritePreferences();
                    } else {
                        Log.w("failed",task.getException().getMessage() , task.getException());
                        emailEditText.setError(task.getException().getMessage());
                        passwordEditText.setError(task.getException().getMessage());
                        progressBar.hide();
                    }
                }
            });
        }
        submit.setClickable(true);
    }

    private void checkAndWritePreferences() {
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login Credentials",0);
        if(!sharedPreferences.contains("name")){
            Log.d("prefs", "checkAndWritePreferences: "+"Preferences not found");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("user-details").document(mAuth.getCurrentUser().getEmail());
            docRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("prefs", "onSuccess: "+mAuth.getCurrentUser().getEmail()+" "+documentSnapshot.get("name"));
                            User user = documentSnapshot.toObject(User.class);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name",user.getName());
                            editor.putString("regd",user.getRegd());
                            editor.putString("branch",user.getBranch());
                            editor.putString("sem",String.valueOf(user.getSem().charAt(0)));
                            editor.putString("email",user.getEmail());
                            editor.commit();
                            onLoginComplete();
                        }
                    });


        }
    }

    private void onLoginComplete() {
        finish();
        startActivity(new Intent(this,WelcomeActivity.class));
    }

    public void toRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void resetPassword(View view) {
        startActivity(new Intent(this,ResetPasswordActivity.class));

    }
}
