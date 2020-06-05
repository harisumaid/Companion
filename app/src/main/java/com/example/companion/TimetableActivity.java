package com.example.companion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

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

public class TimetableActivity extends AppCompatActivity {

    File rootFile;
    ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_viewer);
        File parentFile = new File(getApplicationContext().getExternalFilesDir(null),"timetable");
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        rootFile = new File(parentFile,"timetable.jpg");
        progressBar = findViewById(R.id.progress_in_loading);
        progressBar.show();
        if(isNotOnLocalStorage()){
            fetchFromRemote();
        } else {
            fetchFromLocal();
        }
    }

    private void fetchFromLocal() {

//        this was for showing downloaded image in image view


//        Bitmap myBitmap = BitmapFactory.decodeFile(rootFile.getAbsolutePath());
//        ImageView timetable_imageview = findViewById(R.id.timetable_imageview);
//        timetable_imageview.setImageBitmap(myBitmap);
//        ContentLoadingProgressBar progressBar = findViewById(R.id.progress_in_loading);
//        progressBar.hide();

//        this for showing the image in an intent

        Context context = getApplicationContext();
        Uri pdfUri = FileProvider.getUriForFile(context,getPackageName()+".fileprovider",rootFile);
        context.grantUriPermission(context.getPackageName(),pdfUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setDataAndType(pdfUri,"image")
                ;
        context.startActivity(intent);
        finish();
    }

    private void fetchFromRemote() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        String pathDetails = createUrlFromDetails();

        StorageReference pathReference = storageReference.child(pathDetails);

        pathReference.getFile(rootFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        fetchFromLocal();
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
        String sem = prefs.getString("sem",null)+"/";
        return "timetable/"+branch.toLowerCase()+"sem"+sem+"schedule.jpg";
    }

    private boolean isNotOnLocalStorage() {
        return !rootFile.exists();
    }
}
