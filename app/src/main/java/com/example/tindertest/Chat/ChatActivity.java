package com.example.tindertest.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tindertest.Matches.Matched_Activity;
import com.example.tindertest.R;
import com.google.firebase.auth.FirebaseAuth;
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

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mSendEditText;
    private ImageButton backImg;

    private ImageButton mSendBtn;
    private String notification;
    private String currentUsrId,matchId,chatId;
    private String matchName,matchGive,matchNeed,matchBudget,matchProfile;
    private String lastMessage,lastTimeStamp;
    private String message,createdByUsr,isSeen,messageId,currentUsername;
    private boolean currentUserBoolean;
    ValueEventListener seenListener;
    DatabaseReference mDatabaseUser,mDatabaseChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


      // matchId=getIntent().getExtras().getString("matchId");
       // matchName=getIntent().getExtras().getString("matchName");
      //  matchNeed=getIntent().getExtras().getString("need");
      //  matchGive=getIntent().getExtras().getString("give");
     //   matchBudget=getIntent().getExtras().getString("budget");
     //   matchProfile=getIntent().getExtras().getString("profile");

        currentUsrId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUsrId).child("connections").child("matches").child(matchId);

        mDatabaseChat=FirebaseDatabase.getInstance().getReference().child("Chat");

        //getChatId();

        recyclerView=findViewById(R.id.chatRv);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        mChatAdapter=new ChatAdapter(getDataSetChat(),ChatActivity.this);//match
        recyclerView.setAdapter(mChatAdapter);

        mSendEditText=findViewById(R.id.message);

      //  backImg=findViewById(R.id.chatBack);
        mSendBtn=findViewById(R.id.send);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //seenMessage(message);
                message=mSendEditText.getText().toString();
                sendMessage();
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                if (bottom< oldBottom){
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                        }
                    },100);
                }
            }
        });

       // Toolbar toolbar=findViewById(R.id.chatTollbar);
        //setSupportActionBar(toolbar);


    /*    backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MatchedActivity.class));
                finish();
                return;
            }
        });*/


     /*   DatabaseReference current=FirebaseDatabase.getInstance().getReference().child("users")
                .child(matchId).child("connections").child("matches").child(currentUsrId);

        Map lastSeen=new HashMap();
        lastSeen.put("lastSeen","false");

        current.updateChildren(lastSeen);*/

    }

    @Override
    protected void onPause() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users").child(currentUsrId);
        Map onChat=new HashMap();
        onChat.put("onChat","None");
        reference.updateChildren(onChat);
        super.onPause();
    }

    @Override
    protected void onStop() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users").child(currentUsrId);
        Map onChat=new HashMap();
        onChat.put("onChat","None");
        reference.updateChildren(onChat);
        super.onStop();
    }

/*
    public void seenMessage(final String text){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users").child(matchId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    if (snapshot.child("onChat").exists()){
                        notification=snapshot.child("notificationKey").getValue().toString();
                    }else {
                        notification="";
                    }

                    if (snapshot.child("onChat").getValue().toString().equals(currentUsrId)){

                       // new SendNotification(text,"New message from : "+currentUsername,notification,
                              //  "activityToBeOpened","MatchesActivity");
                    }else {
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users")
                                .child(matchId).child("connections").child("matches").child(currentUsrId);

                        Map sendInfo=new HashMap();
                        sendInfo.put("lastSend","false");
                        reference.updateChildren(sendInfo);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        TextView mMatchNameTextView=(TextView) findViewById(R.id.chatTollbar);
        mMatchNameTextView.setText(matchName);
        return true;
    }

    public void ShowProfile(View view){
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView=inflater.inflate(R.layout.item_profile,null);

        TextView name=(TextView) popUpView.findViewById(R.id.name);
        ImageView image=(ImageView) popUpView.findViewById(R.id.image);
        TextView budget=(TextView) popUpView.findViewById(R.id.budgetId);
        ImageView needImage=(ImageView) popUpView.findViewById(R.id.needImage);
        ImageView giveImage=(ImageView) popUpView.findViewById(R.id.giveImage);

        name.setText(matchName);
        budget.setText(matchBudget);


        switch (matchProfile){
            case "default":
                Glide.with(this).load(R.drawable.programmer).into(image);
                break;
            default:
                Glide.with(this).load(matchProfile).into(image);
                break;
        }


        int width= LinearLayout.LayoutParams.MATCH_PARENT;
        int height=LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable=true;
        final PopupWindow popupWindow=new PopupWindow(popUpView,width,height,focusable);

        hideSoftKeyboard();
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void hideSoftKeyboard(){

        InputMethodManager imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        if (imm.isAcceptingText()){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }

    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.umMatch)
        {
            new AlertDialog.Builder(ChatActivity.this)
                    .setTitle("Unmatch")
                    .setMessage("Are you sure you want to Unmatch?")
                    .setPositiveButton("Unmatch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMatch(matchId);
                            Intent intent=new Intent(ChatActivity.this, Matched_Activity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(ChatActivity.this, "Unmatch successful", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Dismiss",null)
                    .setIcon(R.drawable.danger)
                    .show();
        }else if (item.getItemId()==R.id.viewProfile)
        {
            ShowProfile(findViewById(R.id.image));
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMatch(String matchId) {

        DatabaseReference match_in_UserId_dbReference=FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUsrId).child("connections").child("matches").child(matchId);
        DatabaseReference userId_in_matchId_dbReference=FirebaseDatabase.getInstance().getReference().child("users")
                .child(matchId).child("connections").child("matches").child(currentUsrId);

        DatabaseReference yeps_in_matchId_dbReference=FirebaseDatabase.getInstance().getReference().child("users")
                .child(matchId).child("connections").child("yeps").child(currentUsrId);

        DatabaseReference yeps_in_UserId_dbReference=FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUsrId).child("connections").child("yeps").child(matchId);

        DatabaseReference matchId_chat_databaseReference=FirebaseDatabase.getInstance().
                getReference()
                .child("chat")
                .child(chatId);

        matchId_chat_databaseReference.removeValue();
        match_in_UserId_dbReference.removeValue();
        userId_in_matchId_dbReference.removeValue();
        yeps_in_matchId_dbReference.removeValue();
        yeps_in_UserId_dbReference.removeValue();

    }*/


    public void sendMessage(){

        DatabaseReference newMessageDb=mDatabaseChat.push();

        Map newMessage=new HashMap();

        newMessage.put("createdByUser",currentUsrId);
        newMessage.put("text",message);
        newMessage.put("timeStamp",lastTimeStamp);
        newMessage.put("seen","false");

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users").child(currentUsrId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    if (snapshot.child("name").exists())
                    {
                        currentUsername=snapshot.child("name").getValue().toString();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lastMessage=message;//sendMessage
        lastTimeStamp=lastTimeStamp;//timeStamp

      //  upDateLastMessage();
       // seenMessage(message);
        newMessageDb.setValue(newMessage);
        mSendEditText.setText("");
    }


/*
    public void upDateLastMessage(){

        DatabaseReference currentDb=FirebaseDatabase.getInstance().getReference()
                .child("users").child(currentUsrId).child("connections").child("matches").child(matchId);

        DatabaseReference matchDb=FirebaseDatabase.getInstance().getReference()
                .child("users").child(matchId).child("connections").child("matches").child(currentUsrId);

        Map lastMessageMap=new HashMap();
        lastMessageMap.put("lastMessage",lastMessage);

        Map timeStampMap=new HashMap();
        timeStampMap.put("lastTimeStamp",lastTimeStamp);

        Map lastSeenMap=new HashMap();
        lastSeenMap.put("lastSeen","true");

        currentDb.updateChildren(lastSeenMap);
        currentDb.updateChildren(lastMessageMap);
        currentDb.updateChildren(timeStampMap);


        matchDb.updateChildren(lastMessageMap);
        matchDb.updateChildren(timeStampMap);


    }
*/

    public void getChatId(){

        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists())
                {
                    chatId=snapshot.getValue().toString();
                    mDatabaseChat=mDatabaseChat.child(chatId);
                    getChatMessage();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getChatMessage(){

        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()){

                    message=null;
                    messageId=null;
                    createdByUsr=null;
                    isSeen=null;

                    if (snapshot.child("text").getValue()!=null)
                    {
                        message=snapshot.child("text").getValue().toString();

                    }
                    if (snapshot.child("createdByUser").getValue()!=null)
                    {
                        createdByUsr=snapshot.child("createdByUser").getValue().toString();

                    }
                    if (snapshot.child("seen").getValue()!=null)
                    {
                        isSeen=snapshot.child("seen").getValue().toString();

                    }else {
                        isSeen="true";
                    }
                    messageId=snapshot.getKey().toString();

                    if (message!=null && createdByUsr!=null){
                        currentUserBoolean=false;
                        if (createdByUsr.equals(currentUsrId))
                        {
                            currentUserBoolean=true;
                        }
                        ChatObject newMessage=null;

                        if (isSeen.equals("false")){
                            if (!currentUserBoolean){
                                isSeen="true";

                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Chat")
                                        .child(chatId).child(messageId);

                                Map seenInfo=new HashMap();
                                seenInfo.put("seen","true");

                                reference.updateChildren(seenInfo);

                                newMessage=new ChatObject(message,currentUserBoolean,true);


                            }else {
                                newMessage=new ChatObject(message,currentUserBoolean,false);

                            }
                        }else {
                            newMessage=new ChatObject(message,currentUserBoolean,true);

                            DatabaseReference userInChat=FirebaseDatabase.getInstance().getReference().child("Chat").child(matchId);
                            resultsChat.add(newMessage);
                            mChatAdapter.notifyDataSetChanged();

                            if (recyclerView.getAdapter()!=null && resultsChat.size()>0)
                                recyclerView.smoothScrollToPosition(resultsChat.size()-1);
                            else
                                Toast.makeText(ChatActivity.this, "Chat Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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
    }

    private ArrayList<ChatObject> resultsChat=new ArrayList<>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }
}