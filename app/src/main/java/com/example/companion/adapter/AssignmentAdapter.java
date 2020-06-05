package com.example.companion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companion.R;
import com.example.companion.models.Assignment;
import com.example.companion.models.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.MyViewHolder> {
    ArrayList<Assignment> listOfAssignment;
    Calendar calendar;

    public  AssignmentAdapter(ArrayList<Assignment> listOfAssignment){
        this.listOfAssignment = listOfAssignment;
        calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View card_for_project = inflater.inflate(R.layout.card_for_project,parent,false);

        return new MyViewHolder(card_for_project);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.assignmentName.setText(listOfAssignment.get(position).getAssignmentName());
        holder.assignmentDescription.setText(listOfAssignment.get(position).getAssignmentDescription());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        holder.assignmentDueDate.setText(format.format(listOfAssignment.get(position).getAssignmentDueDate().toDate()));
    }

    @Override
    public int getItemCount() {
        return listOfAssignment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView assignmentName ;
        TextView assignmentDescription;
        TextView assignmentDueDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            assignmentName = itemView.findViewById(R.id.project_name_textview);
            assignmentDescription = itemView.findViewById(R.id.project_description_textview);
            assignmentDueDate = itemView.findViewById(R.id.project_due_date_textview);
        }
    }
}
