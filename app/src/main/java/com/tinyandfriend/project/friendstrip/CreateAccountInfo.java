package com.tinyandfriend.project.friendstrip;

import android.text.TextUtils;


/**
 * Created by NewWy on 3/10/2559.
 */
public class CreateAccountInfo {

    String email;
    String password;
    String citizenId;
    String username;

    public CreateAccountInfo() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        boolean valid = !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(valid)
            this.email = email;
        else
            throw new IllegalArgumentException("Invalid Email Format");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

