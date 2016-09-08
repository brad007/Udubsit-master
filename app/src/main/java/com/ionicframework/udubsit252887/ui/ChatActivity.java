package com.ionicframework.udubsit252887.ui;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Comment;
import com.ionicframework.udubsit252887.models.Users;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mChatRecycler;
    private FirebaseRecyclerAdapter<Comment,
            ViewEventActivity.PlaceholderFragment.CommentHolder> mChatAdapter;
    private String recipientEmail;
    private EditText chatEditView;
    private ImageView chatSendView;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialiseScreen();
    }

    private void initialiseScreen() {
        mChatRecycler = (RecyclerView) findViewById(R.id.chat_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this);
        mChatRecycler.setLayoutManager(manager);
        chatEditView = (EditText) findViewById(R.id.chat_editview);
        chatSendView = (ImageView) findViewById(R.id.chat_send_image);
        chatSendView.setOnClickListener(this);

        recipientEmail = getIntent().getStringExtra(Constants.EMAIL_KEY);
        FirebaseDatabase.getInstance().getReference(Constants.MY_CHATS_KEY)
                .child(Utils.getUserEmail().replace(".", ","))
                .child(recipientEmail.replace(".", ","))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            UID = (String) dataSnapshot.getValue();
                            setupAdapter();
                        } else {
                            UID = Utils.getPushId();
                            FirebaseDatabase.getInstance().getReference(Constants.MY_CHATS_KEY)
                                    .child(Utils.getUserEmail().replace(".", ","))
                                    .child(recipientEmail.replace(".", ","))
                                    .setValue(UID);
                            FirebaseDatabase.getInstance().getReference(Constants.MY_CHATS_KEY)
                                    .child(recipientEmail.replace(".", ","))
                                    .child(Utils.getUserEmail().replace(".", ","))
                                    .setValue(UID);
                            setupAdapter();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setupAdapter() {
        Log.v("ChatActivity", "setupAdapter");
        mChatAdapter = new FirebaseRecyclerAdapter<Comment, ViewEventActivity.PlaceholderFragment.CommentHolder>
                (
                        Comment.class,
                        R.layout.item_layout_comment,
                        ViewEventActivity.PlaceholderFragment.CommentHolder.class,
                        FirebaseDatabase.getInstance().getReference(Constants.CHATS_KEY)
                                .child(UID)
                ) {
            @Override
            protected void populateViewHolder(final ViewEventActivity.PlaceholderFragment.CommentHolder viewHolder, Comment model, int position) {
                if (!model.getOwner().equals(Utils.getUserEmail())) {
                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(
                            Constants.USERS_KEY
                    ).child(model.getOwner().replace(".", ","));
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            viewHolder.setUsernameText(users.getUser());
                            if (users.getPhotoUri() != null) {
                                Glide.with(ChatActivity.this)
                                        .load(users.getPhotoUri())
                                        .into(viewHolder.commmentDisplay);

                            } else {
                                viewHolder.commmentDisplay.setImageResource(R.drawable.facebook_avatar);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    String name = "";
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    } else {
                        name = "Blank";
                    }
                    viewHolder.setUsernameText(name);
                    viewHolder.commmentDisplay.setImageResource(R.drawable.facebook_avatar);
                }
                viewHolder.setCommentText(model.getText());
                viewHolder.setTime(model.getTimeCreated());
            }
        };
        mChatRecycler.setAdapter(mChatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_send_image:
                sendChat();
                break;
        }
    }

    private void sendChat() {
        String text = chatEditView.getText().toString();
        Comment comment = new Comment(Utils.getUserEmail(), text, -1 * System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference(Constants.CHATS_KEY)
                .child(UID)
                .push()
                .setValue(comment);
    }
}
