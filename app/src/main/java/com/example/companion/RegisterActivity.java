package com.example.companion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import static java.security.AccessController.getContext;

public class RegisterActivity extends AppCompatActivity {
    String email;
    String password;
    private FirebaseAuth mAuth;
    ContentLoadingProgressBar progressBar;
    Button submit ;
    FirebaseFirestore db;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_register);
        progressBar=findViewById(R.id.loading_register);
        submit = findViewById(R.id.activity_submit_button_register);

        String [] SEMESTER = new String[] {"1st SEM","2nd SEM","3rd SEM","4th SEM","5th SEM","6th SEM","7th SEM","8th SEM","Completed B.tech"};
        String [] BRANCH = new String[] {"CSE","MECH","IT","EE","CIVIL"};

        ArrayAdapter<String> semester = new ArrayAdapter<String>(this,R.layout.dropdown_menu_autocomplete,SEMESTER);
        ArrayAdapter<String> branch = new ArrayAdapter<String>(this,R.layout.dropdown_menu_autocomplete,BRANCH);

        AutoCompleteTextView branch_register = findViewById(R.id.branch_autoTextView_register);
        branch_register.setAdapter(branch);

        AutoCompleteTextView sem_register = findViewById(R.id.sem_autoTextView_register);
        sem_register.setAdapter(semester);

    }

    public void clickRegister(View view) {
        submit.setClickable(false);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);


        final TextInputEditText emailEditText = findViewById(R.id.email_textinput_register);
        email = emailEditText.getText().toString();
        final TextInputEditText passwordEditText = findViewById(R.id.password_textinput_register);
        password = passwordEditText.getText().toString();

        TextInputEditText nameEditText = findViewById(R.id.name_editText_register);
        TextInputEditText regdEditText = findViewById(R.id.regd_editText_register);
        AutoCompleteTextView branchEditText = findViewById(R.id.branch_autoTextView_register);
        AutoCompleteTextView semEditText = findViewById(R.id.sem_autoTextView_register);

        String name = nameEditText.getText().toString();
        String regd = regdEditText.getText().toString();
        String branch = branchEditText.getText().toString();
        String sem = semEditText.getText().toString();



        if(email.isEmpty() || password.isEmpty()|| name.isEmpty()|| regd.isEmpty()|| branch.isEmpty()|| sem.isEmpty() ){
            Toast.makeText(getApplicationContext(), "Any details field can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.show();
            user = new User(email,name,regd,branch,sem);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("success","successfully created a user");
                        onWriteDetails();
                    } else {
                        Log.w("failed","task failed",task.getException());
                        emailEditText.setError(task.getException().getMessage());
                        passwordEditText.setError(task.getException().getMessage());
                        progressBar.hide();
                    }
                }
            });
        }
        submit.setClickable(true);

    }

    private void onWriteDetails() {
        db.collection("user-details")
                .document(user.getEmail())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createSharedPrefrences();
                        onRegisterationComplete();
                    }
                });
    }

    private void createSharedPrefrences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login Credentials",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",user.getName());
        editor.putString("regd",user.getRegd());
        editor.putString("branch",user.getBranch());
        if(!user.getSem().equals("Completed B.tech")){
            editor.putString("sem",String.valueOf(user.getSem().charAt(0)));
        } else{
            editor.putString("sem","9");
        }

        editor.putString("email",user.getEmail());
        editor.commit();
    }

    private void onRegisterationComplete() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
