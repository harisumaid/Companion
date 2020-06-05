package com.example.companion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companion.adapter.CheckBackPaperAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CheckBackPaperActivity extends AppCompatActivity {

    ArrayList<String> subjects;
    ArrayList<String> subjectsRegistered;
    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    MaterialTextView heading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_back_register);
        heading = findViewById(R.id.check_back_paper_heading);
        heading.setText("Not registered for any subjects yet");
        subjects = new ArrayList<String>();
        subjectsRegistered = new ArrayList<String>();
        enterSubjects();
        recyclerView = findViewById(R.id.check_back_paper_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckBackPaperAdapter(subjectsRegistered);
        recyclerView.setAdapter(adapter);

        fetchAndSetAdapter();
    }

    private void enterSubjects() {
        subjects.add("SUBJECT_1");
        subjects.add("SUBJECT_2");
        subjects.add("SUBJECT_3");
        subjects.add("SUBJECT_4");
        subjects.add("SUBJECT_5");
        subjects.add("SUBJECT_6");
    }

    private void fetchAndSetAdapter() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Login Credentials",0);
        String email = pref.getString("email","");
        Log.d("back-paper", "fetchAndSetAdapter: "+email);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (final String subject :
                subjects) {
            db.collection("back-papers")
                    .document(subject)
                    .collection("students-registered")
                    .document(email)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("back-paper", "onSuccess: "+documentSnapshot.getId()+documentSnapshot.get("email"));
                            if(!(documentSnapshot.get("email") == null)){
                                subjectsRegistered.add(subject);
                                adapter.notifyDataSetChanged();
                                heading.setText("Registered For Subjects:");
                            }
                        }
                    });
        }
    }

    private void startFillData() {

    }
}
