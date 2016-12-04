package com.tinyandfriend.project.friendstrip.holder;

import android.view.View;
import android.widget.Button;

import com.tinyandfriend.project.friendstrip.R;
import com.tinyandfriend.project.friendstrip.info.NotificationType;

/**
 * Created by NewWy on 3/12/2559.
 */

public class NotiAcptFriendHolder extends NotificationHolder {

    public Button acceptButton;
    public Button denidedButton;

    public NotiAcptFriendHolder(View itemView) {
        super(itemView);
        setType(NotificationType.AcceptFriend);
        acceptButton = (Button)itemView.findViewById(R.id.accept_button);
        denidedButton = (Button)itemView.findViewById(R.id.denied_button);
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public void setAcceptButton(Button acceptButton) {
        this.acceptButton = acceptButton;
    }

    public Button getDenidedButton() {
        return denidedButton;
    }

    public void setDenidedButton(Button denidedButton) {
        this.denidedButton = denidedButton;
    }
}
