package com.example.companion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AcademicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

    }


    public void startBackPaperActivity(View view) {
        startActivity(new Intent(this,PaperRegisterActivity.class));
    }

    public void checkForBackPaper(View view) {
        startActivity(new Intent(this,CheckBackPaperActivity.class));
    }

    public void contactAcademicSection(View view) {
        startActivity(new Intent(this,ContactActivity.class));
    }
}
