package com.example.tindertest.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tindertest.Main.Cards;
import com.example.tindertest.R;
import com.example.tindertest.TopNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Activity extends AppCompatActivity {
    private static final String TAG = "Profile_Activity";
    private static final int ACTIVITY_NUM = 0;
    static boolean active = false;

    private Context mContext = Profile_Activity.this;
    private ImageView imagePerson;
    private TextView name;
    ImageButton edit,setting;

    View layoutParse;
    ImageView profile,amin1,amin2,anim3;
    Handler handlerImg;

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: create the page");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        setupTopNavigationView();

        name = findViewById(R.id.profile_name);
        edit=findViewById(R.id.edit_profile);
        setting=findViewById(R.id.settings);

        firebaseAuth=FirebaseAuth.getInstance();
        userId=firebaseAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (snapshot.child("name")!=null){

                        String nameDb=snapshot.child("name").getValue().toString();
                        name.setText(nameDb);
                    }

                    if (snapshot.child("profileImageUrl")!=null){

                        Glide.with(getApplicationContext()).load(snapshot.child("profileImageUrl").getValue()).apply(new RequestOptions().circleCrop()).into(profile);

                    }

                    //   Cards cards=snapshot.getValue(Cards.class);
                    // Toast.makeText(mContext, "Name"+cards.getName(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        layoutParse=findViewById(R.id.layout1);
        profile=findViewById(R.id.profileImg);
        amin1=findViewById(R.id.profileAmin1);
        anim3=findViewById(R.id.prfileAmin3);
        amin2=findViewById(R.id.prfileAmin2);
        handlerImg=new Handler();

      Glide.with(getApplicationContext()).load(R.drawable.user1).apply(new RequestOptions().circleCrop()).into(profile);
        //starAnim();



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile_Activity.this,EditProfileActivity.class));
                finish();
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Setting your profile", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume to the page");

    }


    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationView tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    public void starAnim(){

        runnableAnim.run();
        layoutParse.setVisibility(View.VISIBLE);
    }
    public void stopAnim(){

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

}
