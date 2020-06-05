package com.example.companion.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.companion.R;
import com.example.companion.models.BookIssue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddBookDialog extends DialogFragment {

    private Calendar calendar;
    private TextInputEditText issueDate;
    private TextInputEditText dueDate;
    private TextInputEditText bookName;
    private TextInputLayout issueDateLayout;
    private TextInputLayout bookNameLayout;
    private ContentLoadingProgressBar progressBar;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_book,null);
        calendar = Calendar.getInstance();

        bookName = view.findViewById(R.id.textInput_bookname);
        bookNameLayout = view.findViewById(R.id.inputLayout_bookname);
        issueDate = view.findViewById(R.id.textInput_issue_date);
        issueDateLayout = view.findViewById(R.id.input_layout_issue_date);
        dueDate = view.findViewById(R.id.textInput_due_date);
        progressBar = view.findViewById(R.id.library_progressbar);
        progressBar.hide();

        issueDate.setOnClickListener(dateClickListener());

        issueDate.addTextChangedListener(myTextWatcher(issueDateLayout));
        bookName.addTextChangedListener(myTextWatcher(bookNameLayout));

        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(getActivity())
                            .setView(view)
                            .setTitle("ADD BOOK")
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
                    if(!bookNameFilledOrNot()){
                        bookNameLayout.setError("Required");
                        wantToCloseDialog = false;
                    }

                    //if dateOfIssue is empty show as error
                    if (!dateFilledOrNot()){
                        issueDateLayout.setError("Required");
                        wantToCloseDialog = false;
                    }


                    if (wantToCloseDialog){
                        positiveButton.setClickable(true);
                    }

                    //Do stuff, possibly set wantToCloseDialog to true then...
                    if(wantToCloseDialog){
                        progressBar.show();
                        String bookNameString = bookName.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        Date iDate = null;
                        Date dDate = null;
                        try {
                            iDate = format.parse(issueDate.getText().toString());
                            dDate = format.parse(dueDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        BookIssue bookIssue = new BookIssue(bookNameString,iDate,dDate);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        db.collection("user-details")
                                .document(mAuth.getCurrentUser().getEmail())
                                .collection("library-details")
                                .add(bookIssue)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressBar.hide();
                                        d.dismiss();
                                    }
                                });

                        // add to database now

                    }

                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }

    private boolean bookNameFilledOrNot() {
        return !bookName.getText().toString().isEmpty();
    }

    private boolean dateFilledOrNot() {
        return !issueDate.getText().toString().isEmpty();
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
                        issueDate.setText(format.format(date));
                        calendar.add(Calendar.DAY_OF_MONTH,14);
                        dueDate.setText(format.format(calendar.getTime()));

                    }
                });
                picker.show(getFragmentManager(),picker.toString());

            }
        };

    }
}
