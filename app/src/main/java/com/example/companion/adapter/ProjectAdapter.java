package com.example.companion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companion.R;
import com.example.companion.models.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    ArrayList<Project> listOfProjects;
    Calendar calendar;

    public  ProjectAdapter(ArrayList<Project> listOfProjects){
        this.listOfProjects = listOfProjects;
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
        holder.projectName.setText(listOfProjects.get(position).getProjectName());
        holder.projectDescription.setText(listOfProjects.get(position).getProjectDescription());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        holder.projectDueDate.setText(format.format(listOfProjects.get(position).getProjectDueDate().toDate()));
    }

    @Override
    public int getItemCount() {
        return listOfProjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView projectName ;
        TextView projectDescription;
        TextView projectDueDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.project_name_textview);
            projectDescription = itemView.findViewById(R.id.project_description_textview);
            projectDueDate = itemView.findViewById(R.id.project_due_date_textview);
        }
    }
}
