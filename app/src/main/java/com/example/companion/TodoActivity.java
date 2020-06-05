package com.example.companion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
    }

    public void projectsActivity(View view) {
        startActivity(new Intent(this,ProjectActivity.class));
    }

    public void assignmentActivity(View view) {
        startActivity(new Intent(this,AssignmentActivity.class));
    }
}
