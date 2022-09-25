package com.example.tindertest.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tindertest.Main.Cards;
import com.example.tindertest.R;
import com.example.tindertest.databinding.DashoboardrvDesignBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {


    Context context;
    ArrayList<PostModel> postModelArrayList;
    PostModel postModel;
    public PostAdapter() {
    }

    public PostAdapter(Context context, ArrayList<PostModel> modelArrayList) {
        this.context = context;
        this.postModelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.dashoboardrv_design,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

         postModel= postModelArrayList.get(position);




        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPOpUp(v);
            }
        });

        if (postModel.getPostLike()>0){

            holder.binding.pointCount.setVisibility(View.VISIBLE);
            holder.binding.pointCount.setText(postModel.getPostLike()+" "+"Point");

        }

      //  holder.binding.commentText.setText(postModel.getCommentCount()+"");
        if (postModel.getPostImg().equals("none"))
        {
            holder.binding.postImg.setVisibility(View.GONE);
            holder.binding.postDescription.setText(""+postModel.getPostDescription());
        }else {
            if (postModel.getPostImg().equals("") || postModel.getPostImg().equals("none"))
            {
                holder.binding.postImg.setVisibility(View.GONE);
            }else {
                Glide.with(context).load(postModel.getPostImg()).into(holder.binding.postImg);
             //   Picasso.get().load(postModel.getPostImg()).placeholder(R.drawable.saju).into(holder.binding.postImg);
            }
            holder.binding.postDescription.setText(postModel.getPostDescription());
        }

/*
        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,CommentActivity.class);
                intent.putExtra("postedBy",postModel.getPostedBy());
                intent.putExtra("postId",postModel.getPostId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
*/

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(postModel.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    holder.binding.like.setImageResource(R.drawable.redheard);

                }else {

/*
                    holder.binding.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(postModel.getPostId())
                                    .child("likes")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(postModel.getPostId())
                                            .child("postLike")
                                            .setValue(postModel.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.like.setImageResource(R.drawable.redheard);

                                            Notification notification=new Notification();

                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setPostId(postModel.getPostId());
                                            notification.setNotificationBy(postModel.getPostedBy());
                                            notification.setType("like");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(postModel.getPostedBy())
                                                    .push()
                                                    .setValue(notification);

                                        }
                                    });
                                }
                            });
                        }
                    });
*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        FirebaseDatabase.getInstance().getReference().child("users")
                .child(postModel.getPostedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            Cards users=snapshot.getValue(Cards.class);

                            holder.binding.username.setText(users.getName());
                             holder.binding.about.setText(users.getBio());
                             Glide.with(context).load(users.getProfileImageUrl()).into(holder.binding.pProfile);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

    public class  viewHolder extends RecyclerView.ViewHolder {

        DashoboardrvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=DashoboardrvDesignBinding.bind(itemView);

        }
    }

    public void showPOpUp(View view){

        PopupMenu popupMenu=new PopupMenu(context,view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.one_point:
                        postCount(1);
                        Toast.makeText(context, "you give One Point", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.three_point:
                        postCount(3);
                        Toast.makeText(context, "you give Three point", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.five_point:
                        postCount(5);
                        Toast.makeText(context, "you give Five Point", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.point_menu);
        popupMenu.show();

    }
    public void postCount(int point){

/*
        binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,CommentActivity.class);
                intent.putExtra("postedBy",postModel.getPostedBy());
                intent.putExtra("postId",postModel.getPostId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
*/


        if (point==1){
            FirebaseDatabase.getInstance().getReference()
                    .child("posts")
                    .child(postModel.getPostId())
                    .child("likes")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(postModel.getPostId())
                                    .child("postLike")
                                    .setValue(postModel.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                          //  holder.binding.like.setImageResource(R.drawable.redheard);

                                        }
                                    });
                        }
                    });

        }else if (point==3){
            FirebaseDatabase.getInstance().getReference()
                    .child("posts")
                    .child(postModel.getPostId())
                    .child("likes")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(postModel.getPostId())
                                    .child("postLike")
                                    .setValue(postModel.getPostLike()+3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            //  holder.binding.like.setImageResource(R.drawable.redheard);

                                        }
                                    });
                        }
                    });

        }else if (point==5){
            FirebaseDatabase.getInstance().getReference()
                    .child("posts")
                    .child(postModel.getPostId())
                    .child("likes")
                    .child(FirebaseAuth.getInstance().getUid())
                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {


                            FirebaseDatabase.getInstance().getReference()
                                    .child("posts")
                                    .child(postModel.getPostId())
                                    .child("postLike")
                                    .setValue(postModel.getPostLike()+5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            //  holder.binding.like.setImageResource(R.drawable.redheard);

                                        }
                                    });
                        }
                    });

        }



    }
}
