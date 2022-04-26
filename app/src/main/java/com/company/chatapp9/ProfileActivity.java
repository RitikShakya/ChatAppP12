package com.company.chatapp9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ImageView imageupdate;
    EditText usernameupdate;
    Button updateprofile;

    boolean imageset=false;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imageuri;

    String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        imageupdate = findViewById(R.id.imageprofile);
        usernameupdate = findViewById(R.id.usernameprofile);
        updateprofile = findViewById(R.id.updatep);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        getuserinfo();

        imageupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagechoose();
            }
        });
        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();
            }
        });
    }

    public void updateProfile(){
      String newusername=  usernameupdate.getText().toString();
      databaseReference.child("Users").child(firebaseUser.getUid()).child("username").setValue(newusername);


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
                            databaseReference.child("Users").child(firebaseUser.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ProfileActivity.this,"Success in uploading file", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this," Failed in uploading file", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });
                }
            });
        }
        else{
           /// Toast.makeText(ProfileActivity.this," Failed im in uploading file", Toast.LENGTH_SHORT).show();


            databaseReference.child("Users").child(firebaseUser.getUid()).child("image").setValue(image);
        }
    }

    public void getuserinfo(){

        databaseReference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                image = snapshot.child("image").getValue().toString();


                usernameupdate.setText(username);
                if(image.equals("null"))
                {
                    imageupdate.setImageResource(R.drawable.img);
                }else{
                    Picasso.get().load(image).into(imageupdate);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            Picasso.get().load(imageuri).into(imageupdate);
            imageset=true;
        }else
        {
            imageset=false;
        }
    }
}