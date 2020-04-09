package com.example.strathfund;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText rname, remail, rID, rpass, rcpass, rnumber;
    Button register;
    FirebaseAuth mfirebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;
    RadioGroup radiogroup;
    RadioButton radiobutton;
    static String DOMAIN_NAME = "strathmore.edu";
    private static final String TAG = "Register Activity!!!!!!";

//    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//            .setTimestampsInSnapshotsEnabled(true)
//            .build();
//    firestore.setFirestoreSettings(settings);

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
                final String name = rname.getText().toString();
                final String ID = rID.getText().toString().trim();
                final String number = rnumber.getText().toString().trim();
                radiogroup = findViewById(R.id.radio);
                final String email = remail.getText().toString().trim();
                String pass = rpass.getText().toString().trim();
                String cpass = rcpass.getText().toString().trim();
                String domain = email.substring(email.indexOf("@") + 1).toLowerCase();

                if(email.isEmpty()){
                    remail.setError("Please enter a valid email address");
                    remail.requestFocus();
                }
                else if(pass.isEmpty()){
                    rpass.setError("Please enter your password");
                    rpass.requestFocus();
                }
                else if(name.isEmpty()){
                    rname.setError("Please enter your password");
                    rname.requestFocus();
                }
                else if(ID.isEmpty()){
                    rID.setError("Please enter your password");
                    rID.requestFocus();
                }
                else if(number.isEmpty()){
                    rpass.setError("Please enter your password");
                    rpass.requestFocus();
                }
                else if(cpass.isEmpty()){
                    rnumber.setError("Please confirm your password");
                    rnumber.requestFocus();
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

                                userID = mfirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = db.collection("User").document(userID);
                                Map<String, Object> User = new HashMap<>();
                                User.put("name", name);
                                User.put("email", email);
                                User.put("ID", ID);
                                if (radiogroup.getCheckedRadioButtonId() == R.id.radioFemale){
                                    User.put("gender", "female");
                                } else {
                                    User.put("gender", "male");
                                }
                                User.put("number", number);
                                documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "..................................ONSUCCESS: Data Stored successfully for user " +userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "..................................ONFAILURE: " + e.toString());
                                    }
                                });
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
                    Log.d(TAG, ".................................SENDVERIFICATIONEMAIL: Email sent to: " + mFirebaseUser.getEmail());
                    startActivity(new Intent(Register.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Register.this, "Error, could not send verification email", Toast.LENGTH_LONG).show();
                    Log.d(TAG, ".................................SENDVERIFICATIONEMAIL: Email NOT sent: " + mFirebaseUser.getEmail());
                }
            }
        });
    }

//    public void ResendEmail(String email, String password){
//        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
//        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Log.d(TAG, "onComplete: reauthentication success");
//                    SendVerificationEmail();
//                    FirebaseAuth.getInstance().signOut();
//                }
//            }
//        });
//    }
}
