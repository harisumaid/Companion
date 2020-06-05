package com.example.companion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companion.adapter.LibraryAdapter;
import com.example.companion.adapter.ProjectAdapter;
import com.example.companion.fragments.AddBookDialog;
import com.example.companion.fragments.AddProjectDialog;
import com.example.companion.models.BookIssue;
import com.example.companion.models.Project;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity {

    ArrayList<Project> listOfProjects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        listOfProjects = new ArrayList<Project>();
        RecyclerView project_recycler_view = findViewById(R.id.project_recycler_view);
        ExtendedFloatingActionButton fab =  findViewById(R.id.project_fab);
        fab.setText("ADD PROJECT");



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("user-details")
                .document(mAuth.getCurrentUser().getEmail())
                .collection("project-details");

        project_recycler_view.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new ProjectAdapter(listOfProjects);

        project_recycler_view.setAdapter(mAdapter);

        ListenerRegistration registration = addUpdateListener(query,mAdapter);

    }

    private ListenerRegistration addUpdateListener(Query query,final RecyclerView.Adapter mAdapter) {
        return query.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("Realtime-Listener", "listen:error", e);
                            return;
                        }
                        ArrayList<Project> listenerList = new ArrayList<>();
                        for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                // notify listener
                                Project project = documentChange.getDocument().toObject(Project.class);
                                listenerList.add(project);
                            }
                        }
                        if(!listenerList.isEmpty()) {
                            int startIndex = listOfProjects.size()-1;
                            listOfProjects.addAll(listenerList);
//                            mAdapter.notifyItemRangeInserted(startIndex,listenerList.size());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

    public void addProjectToRemote(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("project_add_project");
        if(prev!=null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = new AddProjectDialog("Project","project-details");
        dialogFragment.setShowsDialog(true);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.MaterialDialog);
        dialogFragment.show(ft,"project_add_project");
        Log.d("Project", "addBookToRemote: "+"project added");
    }
}
