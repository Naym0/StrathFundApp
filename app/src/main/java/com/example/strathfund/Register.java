package com.example.strathfund;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText name, email, ID, pass, cpass, number;
    Button register;
    FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth= FirebaseAuth.getInstance();
        name = findViewById(R.id.nameText);
        email = findViewById(R.id.emailText);
        ID = findViewById(R.id.idnoText);
        pass = findViewById(R.id.passText);
        cpass = findViewById(R.id.confirmText);
        number = findViewById(R.id.passText);
    }
}
