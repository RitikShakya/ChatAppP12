package com.company.chatapp9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.chatapp9.Adapters.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChatActivity extends AppCompatActivity {

    ImageView backbtn;
    TextView headertext;
    EditText editText;
    FloatingActionButton fab;
    RecyclerView rvchat;

    String username, othername;
    FirebaseDatabase firebaseDatabase;
    Intent intent;
    DatabaseReference databaseReference;

    MessageAdapter messageAdapter;

    List<ModalClass> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        backbtn = findViewById(R.id.backbtn);
        headertext = findViewById(R.id.headertext);
        editText = findViewById(R.id.editTextTextMultiLine);
        fab = findViewById(R.id.floatingActionButton);
        rvchat = findViewById(R.id.recyclerViewchat);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();


        rvchat.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

         intent = getIntent();
         username = intent.getStringExtra("username");
        othername = intent.getStringExtra("othername");


        headertext.setText(othername + username );

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyChatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message= editText.getText().toString();
                if(!message.equals("")){


                sendMessage(message,othername,username);
                editText.setText("");}
            }
        });

       getMessage(username);

    }
    public  void  sendMessage(String message,String othername, String username){


        String key = databaseReference.child("Messages").child(username).child(othername).push().getKey();
        Map<String,Object> messagegmap = new HashMap<>();
        messagegmap.put("message",message);
        messagegmap.put("from", username);

        databaseReference.child("Messages").child(username).child(othername).child(key).setValue(messagegmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReference.child("Messages").child(othername).child(username).child(key).setValue(messagegmap);
                        }
                    }
                });

    }

    public  void  getMessage(String username){
        databaseReference.child("Messages").child(username).child(othername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModalClass modalClass =snapshot.getValue(ModalClass.class);
                list.add(modalClass);
                messageAdapter.notifyDataSetChanged();
                rvchat.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        messageAdapter = new MessageAdapter(list,username);
        rvchat.setAdapter(messageAdapter);
    }
}