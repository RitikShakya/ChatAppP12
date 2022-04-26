package com.company.chatapp9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginPage extends AppCompatActivity {

    EditText email,password;

    TextView forgotpass;


    Button SignIn, Signup;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email = findViewById(R.id.emailsignin);
        password =findViewById(R.id.passwordsignin);

        forgotpass = findViewById(R.id.forgotpasstext);


        Signup = findViewById(R.id.signin);
        SignIn = findViewById(R.id.signup);


        firebaseAuth = FirebaseAuth.getInstance();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail = email.getText().toString();
                String userpass = password.getText().toString();


                signinwithemail(useremail,userpass);

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public void signinwithemail(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(LoginPage.this, "Succes Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPage.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginPage.this,"Failed Wrong username or pass", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){

            Intent intent = new Intent(LoginPage.this,MainActivity.class);
            startActivity(intent);
        }
    }

}


