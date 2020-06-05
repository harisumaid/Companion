package com.example.companion.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companion.R;
import com.example.companion.models.BookIssue;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {
    ArrayList<BookIssue> listOfBook;
    Calendar calendar;
    public LibraryAdapter(ArrayList<BookIssue> listOfBook){
        //Constructor for initializing data member;
        this.listOfBook = listOfBook;
        calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View card_for_library = inflater.inflate(R.layout.card_for_library,parent,false);

        return new MyViewHolder(card_for_library);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bookName.setText(listOfBook.get(position).getBookName());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        holder.issueDate.setText(format.format(listOfBook.get(position).getIssueDate().toDate()));
        holder.dueDate.setText(format.format(listOfBook.get(position).getDueDate().toDate()));
        long diffInMillis;
        long diff;
        diffInMillis = Math.abs(listOfBook.get(position).getDueDate().toDate().getTime() - calendar.getTime().getTime());
        diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if(listOfBook.get(position).getDueDate().toDate().compareTo(calendar.getTime())< 0) {

            Log.d("library", "onBindViewHolder: " + diff);
            holder.libraryFine.setText("\u20B9 "+ diff * 2);
        } else {
            holder.libraryFine.setText(diff + " days left");
        }
    }

    @Override
    public int getItemCount() {
        return listOfBook.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookName ;
        TextView issueDate;
        TextView dueDate;
        TextView libraryFine;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name_textview);
            issueDate = itemView.findViewById(R.id.issue_date_textview);
            dueDate = itemView.findViewById(R.id.due_date_textview);
            libraryFine = itemView.findViewById(R.id.late_fine_textview_1);
        }
    }
}
