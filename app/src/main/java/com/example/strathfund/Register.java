package com.example.strathfund;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    EditText rname, remail, rID, rpass, rcpass, rnumber;
    Button register;
    FirebaseAuth mfirebaseAuth;
    static String DOMAIN_NAME = "strathmore.edu";
    private static final String TAG = "Register Activity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mfirebaseAuth= FirebaseAuth.getInstance();
        register = findViewById(R.id.btnregister);
        rname = findViewById(R.id.nameText);
        remail = findViewById(R.id.emailText);
        rID = findViewById(R.id.idnoText);
        rpass = findViewById(R.id.passText);
        rcpass = findViewById(R.id.confirmText);
        rnumber = findViewById(R.id.numberText);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = remail.getText().toString();
                String pass = rpass.getText().toString();
                String cpass = rcpass.getText().toString();
                String domain = email.substring(email.indexOf("@") + 1).toLowerCase();

                if(email.isEmpty()){
                    remail.setError("Please enter a valid email address");
                    remail.requestFocus();
                }
                else if(pass.isEmpty()){
                    rpass.setError("Please enter your password");
                    rpass.requestFocus();
                }
                else if(cpass.isEmpty()){
                    rcpass.setError("Please confirm your password");
                    rcpass.requestFocus();
                }
                else if(email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(Register.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!(cpass.equals(pass))){
                    rcpass.setError("Please confirm that both passwords match");
                    rcpass.requestFocus();
                }
                else if(!(domain.equals(DOMAIN_NAME))){
                    remail.setError("Please use a valid Strathmore email address");
                    remail.requestFocus();
                }
                else if(!(email.isEmpty() && pass.isEmpty())){
                    mfirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendVerificationEmail();
                            }
                            else{
                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // FIREBASE SETUP CODE!

    public void SendVerificationEmail(){
        mfirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseUser mFirebaseUser = mfirebaseAuth.getCurrentUser();
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "Registration successful. Please check your email to verify account", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "SendVerificationEmail: Email sent to: " + mFirebaseUser.getEmail());
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Register.this, "Error, could not send verification email", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "SendVerificationEmail: Email NOT sent: " + mFirebaseUser.getEmail());
                }
            }
        });
    }

    public void ResendEmail(String email, String password){
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: reauthentication success");
                    SendVerificationEmail();
                    FirebaseAuth.getInstance().signOut();
                }
            }
        });
    }
}
