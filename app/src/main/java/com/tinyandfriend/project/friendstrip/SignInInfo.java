package com.tinyandfriend.project.friendstrip;

import android.text.TextUtils;

/**
 * Created by NewWy on 3/10/2559.
 */
public class SignInInfo {

    String email;
    String password;

    public SignInInfo(String email, String password) throws IllegalArgumentException {
        setEmail(email);
        setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(!(password.isEmpty() || password == null)) {
            this.password = password;
        }else{
            throw new IllegalArgumentException("Password is Empty or Null");
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException{

        boolean valid = !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(valid)
            this.email = email;
        else
            throw new IllegalArgumentException("Invalid Email Format");
    }
}
