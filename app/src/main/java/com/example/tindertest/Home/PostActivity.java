package com.example.tindertest.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tindertest.Main.Cards;
import com.example.tindertest.R;
import com.example.tindertest.databinding.ActivityPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    ActivityPostBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    StorageReference storageReference;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String postedBy;
    int IMAGE_REQUEST_CODE = 101;
    private Uri imageUri;

    String time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        database=FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("posts");
        postedBy=auth.getUid();

        calendar= Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");
        time=simpleDateFormat.format(calendar.getTime());

        dialog=new ProgressDialog(getApplicationContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);



        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(postedBy)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){

                                    Cards cards=snapshot.getValue(Cards.class);
                                    binding.name.setText(cards.getName());
                                    Glide.with(getApplicationContext()).load(cards.getProfileImageUrl()).into(binding.profile);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        binding.addPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String description = binding.addPostDescription.getText().toString();

                if (!description.isEmpty()) {
                    binding.postBtn.setEnabled(true);
                    binding.postBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_btn));
                    binding.postBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

                }else if (imageUri!=null){

                    binding.postBtn.setEnabled(true);
                    binding.postBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_btn));
                    binding.postBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

                }else {
                    binding.postBtn.setEnabled(false);
                    binding.postBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_bg_btn));
                    binding.postBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.galleryimgId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postType="";
                String postText=binding.addPostDescription.getText().toString();
                if(postText.isEmpty() && imageUri==null){
                    Toast.makeText(getApplicationContext(), "At Least Image Or Text Need To Add", Toast.LENGTH_SHORT).show();
                }else{
                    if(postText.isEmpty() && imageUri!=null){
                        postType=PostModel.POST_TYPE_IMAGE;
                    }else if(!postText.isEmpty() && imageUri==null){
                        postType=PostModel.POST_TYPE_TEXT;
                    }else if(!postText.isEmpty() && imageUri!=null){
                        postType=PostModel.POST_TYPE_IMAGEANDTEXT;
                    }
                    saveData(postType);
                }

            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.postImg.setVisibility(View.VISIBLE);
            binding.postImg.setImageURI(imageUri);

            binding.postBtn.setEnabled(true);
            binding.postBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_btn));
            binding.postBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.white));

        }


    }
    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    public void saveData(String postType){

        String messsage=binding.addPostDescription.getText().toString();
        String postId=databaseReference.push().getKey();
        if(postType.equals(PostModel.POST_TYPE_TEXT)){
            PostModel postModel = new PostModel(postId,"none",postedBy,messsage,time);
            databaseReference.child(postId).setValue(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Post Upload Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PostActivity.this,HomeActivity.class));
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), "Post Upload Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getApplicationContext(), "successfully post", Toast.LENGTH_LONG).show();

                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri loadUri=uriTask.getResult();

                    //String postMessage=messsage.isEmpty()?messsage:"";
                    String postMessage=binding.addPostDescription.getText().toString();


                    PostModel postModel = new PostModel(postId,loadUri.toString(),postedBy,postMessage,time);

                    databaseReference.child(postId).setValue(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Post Upload Successful.", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(PostActivity.this,HomeActivity.class));
                                 finish();

                            }else{
                                Toast.makeText(getApplicationContext(), "Post Upload Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


    }

}