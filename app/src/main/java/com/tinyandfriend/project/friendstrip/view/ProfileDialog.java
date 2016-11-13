package com.tinyandfriend.project.friendstrip.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tinyandfriend.project.friendstrip.ConstantValue;
import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NewWy on 13/11/2559.
 */

public class ProfileDialog extends Dialog {


    private final Context context;
    private final String userUid;
    private final DatabaseReference reference;
    private ValueEventListener listener;

    public ProfileDialog(Context context, String userUid, DatabaseReference reference) {
        super(context);
        this.context = context;
        this.userUid = userUid;
        this.reference = reference;
        setup();
    }

    public void setup() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_dialog);
        final CircleImageView circleImageView = (CircleImageView) findViewById(R.id.user_profile_photo);
        final TextView userName_dialog = (TextView) findViewById(R.id.user_profile_name_dialog);
        final TextView userEmail_dialog = (TextView) findViewById(R.id.user_profile_email_dialog);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String currentUserUid = user.getUid();
            if (userUid.equals(currentUserUid)) {
                ImageView imageView = (ImageView) findViewById(R.id.add_friend);
                imageView.setVisibility(View.INVISIBLE);
            }
        }

        listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                    userName_dialog.setText(userInfo.getDisplayName());
                    userEmail_dialog.setText(userInfo.getEmail());

                    if (userInfo.getProfilePhoto() != null && !userInfo.getProfilePhoto().isEmpty()) {
                        Glide.with(context)
                                .load(userInfo.getProfilePhoto()).centerCrop()
                                .into(circleImageView);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        reference.child(ConstantValue.USERS_CHILD).child(userUid).addListenerForSingleValueEvent(listener);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        reference.removeEventListener(listener);
    }
}
