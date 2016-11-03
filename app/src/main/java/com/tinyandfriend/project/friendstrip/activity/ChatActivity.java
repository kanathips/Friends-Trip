package com.tinyandfriend.project.friendstrip.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.ChatMessage;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Uri userPhotoUrl;
    private String username;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid();
        username = user.getDisplayName();
        userPhotoUrl = user.getPhotoUrl();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.message_recyclerview);
        messageEditText = (EditText) findViewById(R.id.message_editText);
        sendButton = (Button) findViewById(R.id.send_button);

        loadData(recyclerView, reference, userUid);
    }

    private ValueEventListener loadData(final RecyclerView recyclerView, final DatabaseReference reference, String userUid) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tripId = dataSnapshot.getValue(String.class);
                    setupView(messageEditText, sendButton, reference, tripId);
                    setupChat(recyclerView, reference, tripId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.child("users").child(userUid).child("tripId").addListenerForSingleValueEvent(listener);
        return listener;
    }

    private FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> getChatAdapter(DatabaseReference reference) {
        FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> chatAdapter;
        chatAdapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(
                ChatMessage.class,
                R.layout.chat_item,
                MessageViewHolder.class,
                reference.child("chatMessage")) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, ChatMessage model, int position) {
                viewHolder.messageTextView.setText(model.getText());
                viewHolder.messengerTextView.setText(model.getName());
                if (model.getPhotoUrl() == null) {
                    viewHolder.messengerImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(ChatActivity.this,
                                            R.drawable.ic_account_circle_black_24dp));
                } else {
                    Glide.with(ChatActivity.this)
                            .load(model.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }
        };
        return chatAdapter;
    }


    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }

    private void setupChat(RecyclerView recyclerView, DatabaseReference reference, String tripId) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference chatReference = reference.child("tripRoom").child(tripId);

        final FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> chatAdapter = getChatAdapter(chatReference);
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = chatAdapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    linearLayoutManager.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
    }

    private void setupView(final EditText messageEditText, final Button sendButton, final DatabaseReference reference, String tripId) {

        final DatabaseReference chatReference = reference.child("tripRoom").child(tripId).child("chatMessage");

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chatMessage = new
                        ChatMessage(messageEditText.getText().toString(),
                        username,
                        userPhotoUrl);
                chatReference.push().setValue(chatMessage);
                messageEditText.setText("");
            }
        });
    }

}
