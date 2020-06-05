package com.example.companion.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.DialogFragment;

import com.example.companion.R;
import com.example.companion.models.Assignment;
import com.example.companion.models.BookIssue;
import com.example.companion.models.Project;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddProjectDialog extends DialogFragment {
    private Calendar calendar;
    private TextInputEditText projectDescription;
    private TextInputEditText projectDueDate;
    private TextInputEditText projectName;
    private TextInputLayout projectDescriptionLayout;
    private TextInputLayout projectNameLayout;
    private TextInputLayout projectDueDateLayout;
    private ContentLoadingProgressBar progressBar;

    String mainTitle;
    String collectionName;

    public AddProjectDialog(String mainTitle,String collectionName){
        this.mainTitle = mainTitle;
        this.collectionName = collectionName;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_project,null);
        calendar = Calendar.getInstance();

        projectName = view.findViewById(R.id.textInput_projectName);
        projectNameLayout = view.findViewById(R.id.inputLayout_projectName);
        projectDescription = view.findViewById(R.id.textInput_project_description);
        projectDescriptionLayout = view.findViewById(R.id.input_layout_project_description);
        projectDueDate = view.findViewById(R.id.textInput_project_due_date);
        projectDueDateLayout = view.findViewById(R.id.input_layout_project_due_date);
        progressBar = view.findViewById(R.id.project_progressbar);
        progressBar.hide();

        projectDueDate.setOnClickListener(dateClickListener());

        projectDueDateLayout.setHint(mainTitle + " Due Date");
        projectNameLayout.setHint(mainTitle+" Name");
        projectDescriptionLayout.setHint(mainTitle+ " Description");

        projectDueDate.addTextChangedListener(myTextWatcher(projectDueDateLayout));
        projectName.addTextChangedListener(myTextWatcher(projectNameLayout));
        projectDescription.addTextChangedListener(myTextWatcher(projectDescriptionLayout));

        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(getActivity())
                        .setView(view)
                        .setTitle("ADD "+mainTitle.toUpperCase())
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

        return builder.create();
    }


    private TextWatcher myTextWatcher(final TextInputLayout inputLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private View.OnClickListener dateClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Choose date");
                builder.setSelection(Calendar.getInstance().getTimeInMillis());
                MaterialDatePicker<Long> picker = builder.build();
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Log.d("datepicker", "onPositiveButtonClick: "+selection);
                        calendar.setTimeInMillis(selection);
                        Date date = calendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        projectDueDate.setText(format.format(date));
                    }
                });
                picker.show(getFragmentManager(),picker.toString());

            }
        };

    }

    private boolean projectNameFilledOrNot() {
        return !projectName.getText().toString().isEmpty();
    }

    private boolean projectDueDateFilledOrNot() {
        return !projectDueDate.getText().toString().isEmpty();
    }

    private boolean projectDescriptionFilledOrNot() {
        return !projectDescription.getText().toString().isEmpty();
    }



    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();

        if(d != null)
        {
            final Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setClickable(false);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = true;

                    //if bookName is empty show inputLayout error
                    if(!projectNameFilledOrNot()){
                        projectNameLayout.setError("Required");
                        wantToCloseDialog = false;
                    }

                    //if dateOfIssue is empty show as error
                    if (!projectDueDateFilledOrNot()){
                        projectDueDateLayout.setError("Required");
                        wantToCloseDialog = false;
                    }


                    if (!projectDescriptionFilledOrNot()){
                        projectDescriptionLayout.setError("Required");
                        wantToCloseDialog = false;
                    }


                    if (wantToCloseDialog){
                        positiveButton.setClickable(true);
                    }

                    //Do stuff, possibly set wantToCloseDialog to true then...
                    if(wantToCloseDialog){
                        progressBar.show();
                        String projectNameString = projectName.getText().toString();
                        String projectDescriptionString = projectDescription.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        Date dDate = null;
                        try {
                            dDate = format.parse(projectDueDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                        if(mainTitle.equals("project")){
                            Project project = new Project(projectNameString,projectDescriptionString,dDate);
                            db.collection("user-details")
                                    .document(mAuth.getCurrentUser().getEmail())
                                    .collection(collectionName)
                                    .add(project)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressBar.hide();
                                            d.dismiss();
                                        }
                                    });

                        } else {
                            Assignment assignment = new Assignment(projectNameString,projectDescriptionString,dDate);
                            db.collection("user-details")
                                    .document(mAuth.getCurrentUser().getEmail())
                                    .collection(collectionName)
                                    .add(assignment)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressBar.hide();
                                            d.dismiss();
                                        }
                                    });

                        }

                        // add to database now

                    }

                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }



}
