package com.company.chatapp9.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.chatapp9.ModalClass;
import com.company.chatapp9.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {

    List<ModalClass> list;
    String username;
    boolean status;
    int send ; int recieve;

    public MessageAdapter(List<ModalClass> list, String username) {
        this.list = list;
        this.username = username;

        status =false;
        send =1;
        recieve =2;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==send) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_send, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_recieve, parent, false);
        }return new viewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
holder.textView.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            if(status){
                textView = itemView.findViewById(R.id.textViewsend);

            }else
            {
                textView = itemView.findViewById(R.id.textViewrecieve);
            }

        }
    }


    @Override
    public int getItemViewType(int position) {

        if(list.get(position).getFrom().equals("shakya4")){

            status =true;
            return send;
        }else
        {
            status=false;
            return recieve;

        }


    }
}
