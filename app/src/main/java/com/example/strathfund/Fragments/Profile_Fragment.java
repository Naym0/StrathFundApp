package com.example.strathfund.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.strathfund.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile_Fragment extends Fragment {
    TextView name, name1, email, ID, gender, number;
    private static final String TAG = "Profile Activity!!!!!";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DocumentReference ref = db.collection("User").document(userID);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        //GETTING USER DETAILS TO DISPLAY THEM
        name = view.findViewById(R.id.profile_name);
        name1 = view.findViewById(R.id.profile_name1);
        email = view.findViewById(R.id.profile_email);
        ID = view.findViewById(R.id.profile_ID);
        gender = view.findViewById(R.id.profile_gender);
        number = view.findViewById(R.id.profile_number);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "............................DocumentSnapshot data: \n" + document.getData());
                        name.setText(document.getString("name"));
                        name1.setText(document.getString("name"));
                        email.setText(document.getString("email"));
                        ID.setText(document.getString("ID"));
                        gender.setText(document.getString("gender"));
                        number.setText(document.getString("number"));
                    } else {
                        Log.d(TAG, "............................No such document");
                    }
                }
                else{
                    Log.d(TAG, "..........................ERROR: " +task.getException().getMessage());
                }
            }
        });

        return view;
    }
}
