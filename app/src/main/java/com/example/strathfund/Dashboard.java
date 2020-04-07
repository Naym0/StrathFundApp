package com.example.strathfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getUserDetails();
    }

    private void getUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            String uid = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String number = user.getPhoneNumber();

            String properties = "uid " + uid + "\n" + "name " + name + "\n" +"email " + email + "\n" + "number " + number;

            Log.d(TAG, "getUserDetails: properties " + properties);
        }
    }
}
