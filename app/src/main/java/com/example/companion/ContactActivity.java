package com.example.companion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
    }

        public void callForPerson1(View view) {
            TextView phone_no = findViewById(R.id.phone_no_person1);
            String phone_no_person1 = phone_no.getText().toString();
            Uri no = Uri.parse("tel:"+phone_no_person1);
            Intent callIntent = new Intent(Intent.ACTION_DIAL,no);
            startActivity(callIntent);
        }

        public void callForPerson2(View view) {
            TextView phone_no = findViewById(R.id.phone_no_person2);
            String phone_no_person2 = phone_no.getText().toString();
            Uri no = Uri.parse("tel:"+phone_no_person2);
            Intent callIntent = new Intent(Intent.ACTION_DIAL,no);
            startActivity(callIntent);
        }

        public void emailForPerson1(View view) {
            TextView email_address = findViewById(R.id.email_address_person1);
            String email_address_person1 = email_address.getText().toString();

            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:"));
            mailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{email_address_person1});
            if(mailIntent.resolveActivity(getPackageManager())!=null){
                startActivity(mailIntent);
            }
        }

        public void emailForPerson2(View view) {
            TextView email_address = findViewById(R.id.email_address_person2);
            String email_address_person2 = email_address.getText().toString();
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:"));
            mailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{email_address_person2});
            if(mailIntent.resolveActivity(getPackageManager())!=null){
                startActivity(mailIntent);
            }
        }

}
