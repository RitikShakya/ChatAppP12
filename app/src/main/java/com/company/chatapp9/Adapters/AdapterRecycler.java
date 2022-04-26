package com.company.chatapp9.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.company.chatapp9.MyChatActivity;
import com.company.chatapp9.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;



public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {

    List<String> userlist;
    Context context;
    String username;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public AdapterRecycler(List<String> userlist, Context context, String username) {
        this.userlist = userlist;
        this.context = context;
        this.username = username;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        databaseReference.child("Users").child(userlist.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String othername = snapshot.child("username").getValue().toString();
                String imageurl = snapshot.child("image").getValue().toString();

                holder.username.setText(othername);

                if(imageurl.equals("null")) {
                    holder.profileimage.setImageResource(R.drawable.img);
                }
                else{
                    Picasso.get().load(imageurl).into(holder.profileimage);
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, MyChatActivity.class);

                        intent.putExtra("username",username);
                        intent.putExtra("othername", othername);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profileimage;
        CardView cardView;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileimage = itemView.findViewById(R.id.imagecard);
            cardView = itemView.findViewById(R.id.card_user);
            username = itemView.findViewById(R.id.usernamecard);
        }
    }
}
