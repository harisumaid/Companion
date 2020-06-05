package com.example.companion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companion.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CheckBackPaperAdapter extends RecyclerView.Adapter<CheckBackPaperAdapter.MyViewHolder> {

    private ArrayList<String> checkSubjects;
    public CheckBackPaperAdapter(ArrayList<String> checkSubjects) {
        this.checkSubjects = checkSubjects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View card_for_check_paper =  inflater.inflate(R.layout.card_for_check_paper,parent,false);
        return new MyViewHolder(card_for_check_paper);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(checkSubjects.get(position));
    }

    @Override
    public int getItemCount() {
        return checkSubjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView textView ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.check_back_paper_subject);
        }
    }
}
