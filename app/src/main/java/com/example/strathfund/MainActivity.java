package com.example.strathfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        if (v.getId() == R.id.btnregistermain){
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.btnloginmain){
            startActivity(new Intent(this, Login.class));
        }
    }

}
