package com.example.tindertest.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tindertest.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<ChatObject> list;
    private Context context;

    public ChatAdapter(List<ChatObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
       ChatViewHolder viewHolder=new ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {


        holder.mMessage.setText(list.get(position).getMessage());

        if (list.get(position).getCurrentUser()){
            GradientDrawable shape=new GradientDrawable();
            shape.setCornerRadius(20);
            shape.setCornerRadii(new float[]{25,25,3,25,25,25,25,25});
            shape.setColor(Color.parseColor("#5fc9f8"));
            holder.mContainer.setGravity(Gravity.END);
            holder.mMessage.setBackground(shape);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));

        }else {

            GradientDrawable shape=new GradientDrawable();
            shape.setCornerRadius(20);
            shape.setCornerRadii(new float[]{25,25,3,25,25,25,25,25});
            shape.setColor(Color.parseColor("#53d769"));
            holder.mContainer.setGravity(Gravity.START);
            holder.mMessage.setBackground(shape);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
