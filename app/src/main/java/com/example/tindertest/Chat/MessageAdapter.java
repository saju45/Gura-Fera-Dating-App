package com.example.tindertest.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.bumptech.glide.Glide;
import com.example.tindertest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder>{

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    Context context;
    ArrayList<MessageModel> list;
    String imageUrl;
    FirebaseUser user;

    public MessageAdapter(Context context, ArrayList<MessageModel> list, String imageUrl) {
        this.context = context;
        this.list = list;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new viewHolder(view);
        }else {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new viewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        MessageModel model=list.get(position);

        holder.message.setText(model.getMessage());
       // Glide.with(context).load(imageUrl).into(holder.profile_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView profile_image;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_m);
        }
    }

    @Override
    public int getItemViewType(int position) {

        user= FirebaseAuth.getInstance().getCurrentUser();

        if (list.get(position).getSender().equals(user.getUid())){

            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }

    }
}
