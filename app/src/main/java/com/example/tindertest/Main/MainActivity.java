package com.example.tindertest.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tindertest.NotificationHelper;
import com.example.tindertest.R;
import com.example.tindertest.TopNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 1;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    ListView listView;
    List<Cards> rowItems;
    FrameLayout cardFrame, moreFrame;
    private Context mContext = MainActivity.this;
    private NotificationHelper mNotificationHelper;
    private Cards cards_data[];
   private PhotoAdapter arrayAdapter;

   DatabaseReference reference,requestRef,friendRef;
   DatabaseReference usersDb;
   FirebaseAuth firebaseAuth;
   String currentUId,currentStatus="nothing_happen";
   String username,profileUrl;

    View layoutParse;
    ImageView profile,amin1,amin2,anim3;
    Handler handlerImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUId=firebaseAuth.getCurrentUser().getUid();
        usersDb=FirebaseDatabase.getInstance().getReference().child("users");
        reference=FirebaseDatabase.getInstance().getReference().child("users").child(currentUId);
        requestRef=FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef=FirebaseDatabase.getInstance().getReference().child("Friends");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                   Cards cards=snapshot.getValue(Cards.class);
                   Glide.with(getApplicationContext()).load(cards.getProfileImageUrl()).apply(new RequestOptions().circleCrop()).into(profile);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        layoutParse=findViewById(R.id.layout2);
        profile=findViewById(R.id.profileImg);
        amin1=findViewById(R.id.profileAmin1);
        anim3=findViewById(R.id.prfileAmin3);
        amin2=findViewById(R.id.prfileAmin2);
        handlerImg=new Handler();

     //   Glide.with(getApplicationContext()).load(R.drawable.user1).apply(new RequestOptions().circleCrop()).into(profile);
       // starTask();


        cardFrame = findViewById(R.id.card_frame);
        moreFrame = findViewById(R.id.more_frame);

        mNotificationHelper = new NotificationHelper(this);


        setupTopNavigationView();

        rowItems = new ArrayList<Cards>();

      /*  Cards cards = new Cards("1", "Swati Tripathy", 21, "https://im.idiva.com/author/2018/Jul/shivani_chhabra-_author_s_profile.jpg", "Simple and beautiful Girl", "Acting", 200);
        rowItems.add(cards);
        cards = new Cards("2", "Ananaya Pandy", 20, "https://i0.wp.com/profilepicturesdp.com/wp-content/uploads/2018/06/beautiful-indian-girl-image-for-profile-picture-8.jpg", "cool Minded Girl", "Dancing", 800);
        rowItems.add(cards);
        cards = new Cards("3", "Anjali Kasyap", 22, "https://pbs.twimg.com/profile_images/967542394898952192/_M_eHegh_400x400.jpg", "Simple and beautiful Girl", "Singing", 400);
        rowItems.add(cards);
        cards = new Cards("4", "Preety Deshmukh", 19, "http://profilepicturesdp.com/wp-content/uploads/2018/07/fb-real-girls-dp-3.jpg", "dashing girl", "swiming", 1308);
        rowItems.add(cards);
        cards = new Cards("5", "Srutimayee Sen", 20, "https://dp.profilepics.in/profile_pictures/selfie-girls-profile-pics-dp/selfie-pics-dp-for-whatsapp-facebook-profile-25.jpg", "chulbuli nautankibaj ", "Drawing", 1200);
        rowItems.add(cards);
        cards = new Cards("6", "Dikshya Agarawal", 21, "https://pbs.twimg.com/profile_images/485824669732200448/Wy__CJwU.jpeg", "Simple and beautiful Girl", "Sleeping", 700);
        rowItems.add(cards);
        cards = new Cards("7", "Sudeshna Roy", 19, "https://talenthouse-res.cloudinary.com/image/upload/c_fill,f_auto,h_640,w_640/v1411380245/user-415406/submissions/hhb27pgtlp9akxjqlr5w.jpg", "Papa's Pari", "Art", 5000);
        rowItems.add(cards);
*/

    }

    private void checkRowItem() {
        if (rowItems.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        updateLocation();
                    } else {
                        Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateSwipeCard() {
        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                Toast.makeText(mContext, "Left", Toast.LENGTH_SHORT).show();
                checkRowItem();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;

                //check matches
                String uid = obj.getUserId();
                username=obj.getName();
                profileUrl=obj.getProfileImageUrl();
                friendRequest(uid);
             //   usersDb.child(uid).child("connections").child("yeps").child(currentUId).setValue(true);
               // isConnectionMatch(uid);
                Toast.makeText(mContext, "Right", Toast.LENGTH_SHORT).show();
                checkRowItem();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));

        mNotificationHelper.getManager().notify(1, nb.build());
    }


    public void DislikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }

    public void LikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();

            //check matches

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }

    /**
     * setup top tool bar
     */
    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationView tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
    public void onBackPressed() {

    }

    public void starTask(){

        runnableAnim.run();
        layoutParse.setVisibility(View.VISIBLE);
    }
    public void stopTask(){

        handlerImg.removeCallbacks(runnableAnim);
        layoutParse.setVisibility(View.GONE);
    }

    private Runnable runnableAnim=new Runnable() {
        @Override
        public void run() {

            anim3.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(300).withEndAction(new Runnable() {
                @Override
                public void run() {
                    amin1.setScaleX(1f);
                    amin1.setScaleY(1f);
                    amin1.setAlpha(1f);
                }
            });


            amin1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700).withEndAction(new Runnable() {
                @Override
                public void run() {
                    amin1.setScaleX(1f);
                    amin1.setScaleY(1f);
                    amin1.setAlpha(1f);
                }
            });

            amin2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    amin2.setScaleX(1f);
                    amin2.setScaleY(1f);
                    amin2.setAlpha(1f);
                }
            });


            handlerImg.postDelayed(runnableAnim,1500);

        }
    };



    @Override
    protected void onStart(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()){
                    rowItems.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                        Cards cards=dataSnapshot.getValue(Cards.class);
                        rowItems.add(cards);
                    }
                    if (rowItems.size()>=0){

                        arrayAdapter = new PhotoAdapter(MainActivity.this, R.layout.item, rowItems);
                        checkRowItem();
                        updateSwipeCard();
                    }

                }arrayAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onStart();


    }

    private void isConnectionMatch(final String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
/*
        usersDb.child(currentUId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                   sendMessageText = dataSnapshot.getValue().toString();
                else
                   sendMessageText = "";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        if(!currentUId.equals(userId)){
            currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(MainActivity.this, "" +
                                "New Connection", Toast.LENGTH_LONG).show();

                        String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                        Map mapLastTimeStamp = new HashMap<>();
                        long now  = System.currentTimeMillis();
                        String timeStamp = Long.toString(now);
                        mapLastTimeStamp.put("lastTimeStamp",timeStamp);

                        usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                        usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).updateChildren(mapLastTimeStamp);

                        usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                        usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).updateChildren(mapLastTimeStamp);

                  //      notification = " ";

                        DatabaseReference notificationID = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("notificationKey");
                        notificationID.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                  //  notification = snapshot.getValue().toString();
                                 //   Log.d("sendChat", notification);
                                 //   new SendNotification("You have a new match!", "", notification, null, null );
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }else {
                        Toast.makeText(mContext, "No value found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }

    public void friendRequest(String uId){


        if (currentStatus.equals("nothing_happen"))
        {

            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("status","pending");
            hashMap.put("name",username);
            hashMap.put("profile",profileUrl);

            requestRef.child(currentUId).child(uId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(mContext, "You Send Friend Request", Toast.LENGTH_SHORT).show();

                        currentStatus="I_send_pending";
                    }else {
                        Toast.makeText(mContext, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        if (currentStatus.equals("I_send_pending")|| currentStatus.equals("I_send_decline")){

            requestRef.child(currentUId).child(uId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(mContext, "Cancel Friend Request", Toast.LENGTH_SHORT).show();
                        currentStatus="nothing_happen";
                    }else {
                        Toast.makeText(mContext, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (currentStatus.equals("he_send_pending")){
            requestRef.child(currentUId).child(uId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    
                    if (task.isSuccessful()){

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("status","friend");
                        hashMap.put("username",username);
                        hashMap.put("profileUrl",profileUrl);

                        friendRef.child(currentUId).child(uId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    friendRef.child(uId).child(currentUId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(MainActivity.this, "Added friend", Toast.LENGTH_SHORT).show();
                                            currentStatus="friend";
                                        }
                                    });

                           }else {
                                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }else {
                        Toast.makeText(mContext, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
