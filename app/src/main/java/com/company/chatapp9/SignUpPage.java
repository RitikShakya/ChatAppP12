package com.company.chatapp9;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;
import java.util.UUID;

public class SignUpPage extends AppCompatActivity {
    ImageView image;

    EditText email,password;
    Button signin,signup;
    EditText username;
    boolean imageset=false;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imageuri;

    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        email = findViewById(R.id.emailsignup);
        password =findViewById(R.id.passwordsignup);
        username = findViewById(R.id.username);
        image = findViewById(R.id.signuphead);
        signin = findViewById(R.id.signinbtn);
        signup = findViewById(R.id.signupbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagechoose();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String useremail=email.getText().toString();

                String userpass = password.getText().toString();
                String user= username.getText().toString();
                signupwithemail(useremail,userpass,user);
            }
        });
    }
    public void signupwithemail(String email,String password,String user){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    databaseReference.child("Users").child(firebaseAuth.getUid()).child("username").setValue(user);


                    if(imageset){

                        UUID uuid = UUID.randomUUID();
                        String image_name = "image/*"+uuid+".jpg";

                        storageReference.child(image_name).putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                StorageReference mystorageReference = firebaseStorage.getReference(image_name);

                                mystorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String filepath = uri.toString();
                                        databaseReference.child("Users").child(firebaseAuth.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SignUpPage.this,"Success in uploading file", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUpPage.this," Failed in uploading file", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUpPage.this," Profile not added", Toast.LENGTH_SHORT).show();


                        databaseReference.child("Users").child(firebaseAuth.getUid()).child("image").setValue("null");
                    }


                    signup.setClickable(false);
                    Toast.makeText(SignUpPage.this, "succes ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(SignUpPage.this,"failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void imagechoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&requestCode==1&& data!=null) {


            imageuri =data.getData();
            Picasso.get().load(imageuri).into(image);
            imageset=true;
        }else
        {
            imageset=false;
        }
    }
}