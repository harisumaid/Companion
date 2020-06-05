package com.example.companion;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void calculatorActivity(View view) {
        startActivity(new Intent(this,CalculatorActivity.class));
    }

    public void timetableActivity(View view) {
        startActivity(new Intent(this,TimetableActivity.class));
    }

    public void syllabusActivity(View view) {
        startActivity(new Intent(this,SyllabusActivity.class));
    }

    public void academicActivity(View view) {
        startActivity(new Intent(this,AcademicActivity.class));
    }

    public void fineActivity(View view) {
        startActivity(new Intent(this,LibraryFineActivity.class));
    }

    public void todoActivity(View view) {
        startActivity(new Intent(this,TodoActivity.class));
    }

    private String getMimeType(Uri uri){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));

    }
}
