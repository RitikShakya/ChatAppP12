package com.company.chatapp9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button sendbtn;
    EditText emailreset;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailreset = findViewById(R.id.emailreset);
        sendbtn = findViewById(R.id.sendbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailto = emailreset.getText().toString();

                resetemail(mailto);

            }
        });
    }

    public void resetemail(String email){


        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    sendbtn.setClickable(false);

                    Toast.makeText(ForgotPassword.this, "email send", Toast.LENGTH_SHORT).show();

                    finish();
                }else{
                    Toast.makeText(ForgotPassword.this, "error in send", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}