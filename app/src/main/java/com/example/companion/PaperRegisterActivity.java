package com.example.companion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.example.companion.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PaperRegisterActivity extends AppCompatActivity {
    TextInputEditText name;
    TextInputEditText sem;
    TextInputEditText branch;
    TextInputEditText regd;
    ArrayList<View> subjectsEntered ;
    ArrayList<String> subjectOffered;
    ArrayAdapter<String> subjects;
    ArrayAdapter<String> subjects1;
    User user;
    ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_registration);
        name = findViewById(R.id.text_input_paper_name);
        sem = findViewById(R.id.text_input_paper_sem);
        branch = findViewById(R.id.text_input_paper_branch);
        regd = findViewById(R.id.text_input_paper_regd);

        subjectOffered = new ArrayList<>();
        subjectsEntered = new ArrayList<>();
        progressBar =findViewById(R.id.back_paper_progress_bar);
        progressBar.hide();
        addSubjectOffered();
        subjects = new ArrayAdapter<String>(this,R.layout.dropdown_menu_autocomplete,subjectOffered);

        subjects1 = new ArrayAdapter<String>(this,R.layout.dropdown_menu_autocomplete,subjectOffered.subList(0,3));
        autoFillDetails();
        createNewSubjectField();

    }

    private void addSubjectOffered() {
        subjectOffered.add("SUBJECT_1");
        subjectOffered.add("SUBJECT_2");
        subjectOffered.add("SUBJECT_3");
        subjectOffered.add("SUBJECT_4");
        subjectOffered.add("SUBJECT_5");
        subjectOffered.add("SUBJECT_6");
    }

    private void autoFillDetails() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Login Credentials",0);
        String user_name;
        String user_sem;
        String user_branch;
        String user_regd;
        String user_email;
        if(prefs.contains("name")){
            user_name = prefs.getString("name","");
            user_sem = prefs.getString("sem","");
            user_branch = prefs.getString("branch","");
            user_regd = prefs.getString("regd","");
            user_email = prefs.getString("email","");
            user = new User(user_email,user_name,user_regd,user_branch,user_sem);
            name.setText(user_name);
            sem.setText(user_sem);
            branch.setText(user_branch);
            regd.setText(user_regd);
        }

    }

    public void submitForRegistration(View view) {

        if(previousSubjectEditTextFilled()){
            proceedToSubmit(view);
        }

    }

    public void addSubjectInputLayer(View view) {
        if(subjectsEntered.size()==5){
            view.setClickable(false);
        }

        if(previousSubjectEditTextFilled()){
            createNewSubjectField();
        }
    }

    private void createNewSubjectField() {

        LinearLayout linear_layout_subject = findViewById(R.id.linear_layout_for_subject);
        View to_add_subject = getLayoutInflater().inflate(R.layout.layout_subject_paper,linear_layout_subject,false);
        AutoCompleteTextView autoCompleteTextView = to_add_subject.findViewById(R.id.text_input_paper_subject);
        autoCompleteTextView.setAdapter(subjects);
        autoCompleteTextView.setTag(subjectsEntered.size());
        autoCompleteTextView.addTextChangedListener(customTextWatcher((TextInputLayout)to_add_subject));
        subjectsEntered.add(to_add_subject);
        linear_layout_subject.addView(to_add_subject);

    }

    private TextWatcher customTextWatcher(final TextInputLayout to_add_subject) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                to_add_subject.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void proceedToSubmit(View view) {
        //  Proceed to submit
        progressBar.show();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (final View inputLayout :
                subjectsEntered) {
            final AutoCompleteTextView subject_textview = inputLayout.findViewWithTag(subjectsEntered.indexOf(inputLayout));
            String text_of_textview =subject_textview.getText().toString();
            db.collection("back-papers")
                    .document(text_of_textview)
                    .collection("students-registered")
                    .document(user.getEmail())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("back-paper", "onSuccess: "+"data sent");
                            if(subjectsEntered.indexOf(inputLayout) == subjectsEntered.size()-1){
                                finishedBackPaperRegistration();
                            }
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.hide();
                }
            });
        }
    }

    private void finishedBackPaperRegistration() {
        Toast.makeText(this, "Registration for back paper successful", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean previousSubjectEditTextFilled() {
        boolean result = true;
        for (View inputLayout:
             subjectsEntered) {
            AutoCompleteTextView subject_textview = inputLayout.findViewWithTag(subjectsEntered.indexOf(inputLayout));
            String text_of_textview =subject_textview.getText().toString();
            if(text_of_textview.isEmpty()){
                TextInputLayout subject_input_layout = inputLayout.findViewById(R.id.input_layout_paper_subject);
                subject_input_layout.setError("Required**");
                result = false;
                return result;
            } else {
                subjects.remove(text_of_textview);
            }
        }

        return result;
    }
}
