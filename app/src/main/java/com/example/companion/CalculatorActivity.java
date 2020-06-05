package com.example.companion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.companion.constant.Branch_Credits;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity {

    ArrayList<TextInputEditText> editTextList = new ArrayList<TextInputEditText>();
    ArrayList<TextInputLayout> inputLayoutList = new ArrayList<TextInputLayout>();
    ArrayList<Integer> credits = new ArrayList<Integer>();
    TextView calculatedResult;
    SharedPreferences prefs;
    int sem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        calculatedResult = findViewById(R.id.calculator_textview_result);
        prefs = getApplicationContext().getSharedPreferences("Login Credentials",0);
        String semPref = prefs.getString("sem",null);
        sem = Integer.parseInt(semPref);

        editTextList.clear();
        inputLayoutList.clear();
        getCredits();
        createList();
        errorWatcher();
        removeSomeSemester();
        if(sem == 1){
            whyAreYouHere();
        }
    }

    private void whyAreYouHere() {
        calculatedResult.setVisibility(View.VISIBLE);
        calculatedResult.setText("Why are you here!!!!");
    }

    private void getCredits() {
        String branch = prefs.getString("branch",null);
        if(branch.equals("CSE")){
            credits = Branch_Credits.cse_credits();
        } else if(branch.equals("CIVIL")){
            credits = Branch_Credits.civil_credits();
        } else if(branch.equals("EE")){
            credits = Branch_Credits.ee_credits();
        } else if(branch.equals("IT")){
            credits = Branch_Credits.it_credits();
        } else if(branch.equals("MECH")){
            credits = Branch_Credits.mech_credits();
        }

        //TODO : add all credits list in Branch_Credits class and use those here
    }

    private void removeSomeSemester() {

        for (int i =sem-1;i<8;i++){
            inputLayoutList.get(i).setVisibility(View.GONE);
        }
    }

    private void createList() {
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem1));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem2));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem3));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem4));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem5));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem6));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem7));
        editTextList.add((TextInputEditText) findViewById(R.id.calculator_editText_sem8));

        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem1));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem2));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem3));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem4));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem5));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem6));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem7));
        inputLayoutList.add((TextInputLayout) findViewById(R.id.layout_sem8));

    }

    private void errorWatcher() {

        for (int i = 0;i<8;i++){
            editTextList.get(i).addTextChangedListener(customListener(inputLayoutList.get(i)));
        }

    }

    private TextWatcher customListener(final TextInputLayout inputLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout.setError(null);
                MaterialButton calculateButton = findViewById(R.id.calculator_button_calculate);

                calculatedResult.setVisibility(View.GONE);

                if(!s.toString().equals("")&& Double.parseDouble(s.toString()) >10) {
                    inputLayout.setError("S.G.P.A can't be greater than 10.0");
                    calculateButton.setClickable(false);
                } else {
                    calculateButton.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void calculateCGPA(View view) {

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        checkAllValueEntered();
    }

    private void checkAllValueEntered() {

        boolean permissionToCalculate = true;
        double cgpaDenominator = 0;
        double cgpaNumerator = 0;
        double currentSgpa;

        for (int i =0;i<sem-1;i++){
            Log.d("calculate", "checkAllValueEntered: "+editTextList.get(i).getText().toString());
                if(editTextList.get(i).getText().toString().isEmpty()){
                    permissionToCalculate = false;
                    inputLayoutList.get(i).setError("Field can't be empty");
                    Log.d("calculate", "checkAllValueEntered: "+"No Fields Could Be Empty");
                } else {
                    currentSgpa = Integer.parseInt(editTextList.get(i).getText().toString());
                    cgpaNumerator += currentSgpa*credits.get(i) ;
                    cgpaDenominator += credits.get(i);

                }
        }
        // cgpa = sum of (each sem sgpa * each sem credits ) / total credits;
        if(permissionToCalculate){
            double finalCgpa = cgpaNumerator/cgpaDenominator;
            calculatedResult.setVisibility(View.VISIBLE);
            Log.d("calculate", "checkAllValueEntered: " + "C.G.P.A  :  "+finalCgpa);
            calculatedResult.setText("C.G.P.A  :  "+finalCgpa);

            //TODO: if time remains then needs to store sem credits in firebase
            //      so that the user need not to memorize to each sem's credits
            //      and would be fetched each time the user wants to calculate or update it
        }
    }
}
