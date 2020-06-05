package com.example.companion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class SyllabusActivity extends AppCompatActivity {

    File parentFile;
    File rootFile;
    ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        parentFile = new File(getApplicationContext().getExternalFilesDir(null),"syllabus");
        progressBar = findViewById(R.id.syllabus_progressbar);
        rootFile = new File(parentFile,"syllabus.pdf");
        progressBar.show();
        //check if syllabus exist

        boolean fileExists  = syllabusFileExist(rootFile);

        //if exists then open in pdf viewer
        if(fileExists){
            viewInPdf();
        } else {
            downloadPdfFile();
        }

        //download then view in pdf viewer
    }

    private void downloadPdfFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference syllabusReference = storageReference.child(createUrlFromDetails());


        //progress bar implementation
        syllabusReference.getFile(rootFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        viewInPdf();
                        progressBar.hide();
                    }
                })
                .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            }
        });


    }

    private String createUrlFromDetails() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("Login Credentials",0);
        String branch = prefs.getString("branch",null)+"/";
        return "syllabus/"+branch.toLowerCase()+"syllabus.pdf";
    }

    private void viewInPdf() {
        Context context = getApplicationContext();
        Uri pdfUri = FileProvider.getUriForFile(context,getPackageName()+".fileprovider",rootFile);
        context.grantUriPermission(context.getPackageName(),pdfUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setDataAndType(pdfUri,"application/pdf")
                ;
        context.startActivity(intent);
        finish();
    }

    private boolean syllabusFileExist(File rootFile) {
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }

        return rootFile.exists();
    }
}
