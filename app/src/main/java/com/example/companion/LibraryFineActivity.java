package com.example.companion;

import android.content.res.Resources;
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
import com.example.companion.fragments.AddBookDialog;
import com.example.companion.models.BookIssue;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LibraryFineActivity extends AppCompatActivity {
    ArrayList<BookIssue> listOfBook;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_fine);
        listOfBook = new ArrayList<BookIssue>();
        RecyclerView library_recycler_view = findViewById(R.id.library_recycler_view);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("user-details")
                .document(mAuth.getCurrentUser().getEmail())
                .collection("library-details");

        library_recycler_view.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new LibraryAdapter(listOfBook);

        library_recycler_view.setAdapter(mAdapter);

        ListenerRegistration registration = addUpdateListener(query,mAdapter);

    }

    private ListenerRegistration addUpdateListener(Query query, final RecyclerView.Adapter mAdapter) {
        return query.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("Realtime-Listener", "listen:error", e);
                            return;
                        }
                        ArrayList<BookIssue> listenerList = new ArrayList<>();
                        for (DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                // notify listener
                                BookIssue bookIssue = documentChange.getDocument().toObject(BookIssue.class);
                                listenerList.add(bookIssue);
                            }
                        }
                        if(!listenerList.isEmpty()) {
                            int startIndex = listOfBook.size()-1;
                            listOfBook.addAll(listenerList);
//                            mAdapter.notifyItemRangeInserted(startIndex,listenerList.size());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

    public void addBookToRemote(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("library_add_book");
        if(prev!=null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment dialogFragment = new AddBookDialog();
        dialogFragment.setShowsDialog(true);
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.MaterialDialog);
        dialogFragment.show(ft,"library_add_book");
        Log.d("Library", "addBookToRemote: "+"book added");
    }

}
