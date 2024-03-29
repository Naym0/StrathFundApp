package com.example.strathfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText lemail, lpass, input;
    Button login;
    TextView forgotpass, fingerauth;
    private static final String TAG = "Login Activity!!!!!";
    FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mfirebaseAuth= FirebaseAuth.getInstance();
        login = findViewById(R.id.btnlogin);
        lemail = findViewById(R.id.emaillogin);
        lpass = findViewById(R.id.passlogin);
        forgotpass = findViewById(R.id.textView2);
        fingerauth = findViewById(R.id.loginfinger);

        FirebaseAuthSetup();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = lemail.getText().toString();
            String pass = lpass.getText().toString();

            if(email.isEmpty()){
                lemail.setError("Please enter a valid email address");
                lemail.requestFocus();
            }
            else if(pass.isEmpty()){
                lpass.setError("Please enter your password");
                lpass.requestFocus();
            }
            else if(email.isEmpty() && pass.isEmpty()){
                Toast.makeText(Login.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
            }
            else if(!(email.isEmpty() && pass.isEmpty())){
                mfirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        isEmailVerified();
                    }
                    else{
                        Toast.makeText(Login.this, "Login unsuccessful, Please try again", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
            else{
                Toast.makeText(Login.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
            }
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordLink();
            }
        });

        fingerauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Fingerprint_auth.class);
                startActivity(intent);
            }
        });

    }

    private void sendResetPasswordLink(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        builder.setMessage("Please fill in your email address to receive a reset link");

        input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
            String text = input.getText().toString().trim();
            if(text.isEmpty()){
                Toast.makeText(Login.this, "Email field must be filled in!", Toast.LENGTH_LONG).show();
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this, "Please check your email for the reset password link", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "sendResetPasswordLink: Reset link sent");
                    }
                    else{
                        Toast.makeText(Login.this, "Error, could not send reset link", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "SendResetPasswordLink: Error! Reset email NOT sent");
                    }
                    }
                });
            }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog,int which){
            dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }



    // FIREBASE SETUP CODE!



    private void FirebaseAuthSetup(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser mFirebaseUser = mfirebaseAuth.getCurrentUser();
            if(mFirebaseUser != null){
                Log.d(TAG, "........................................OnAuthStateChanged: SIGNED IN: " + mFirebaseUser.getEmail());
               // Toast.makeText(Login.this, "Successful log in", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this, Navdrawer.class);
                startActivity(i);
            }
            else{
                Log.d(TAG, "........................................OnAuthStateChanged: SIGNED OUT");
            }
            }
        };
    }

    private void isEmailVerified(){
        if(mfirebaseAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(Login.this, Navdrawer.class));
            Toast.makeText(Login.this, "Successful Login", Toast.LENGTH_SHORT).show();
            finish();
            finish();
        }
        else{
            Toast.makeText(Login.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
        }
    }
}
