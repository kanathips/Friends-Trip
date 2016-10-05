package com.tinyandfriend.project.friendstrip;

/**
 * Created by NewWy on 3/10/2559.
 */
class SignInInfo {


    private String email;
    private String password;

    public SignInInfo(String email, String password) throws IllegalArgumentException {
        setEmail(email);
        setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        if(!(password.isEmpty())) {
            this.password = password;
        }else{
            throw new IllegalArgumentException("Password is Empty or Null");
        }

    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) throws IllegalArgumentException{

        boolean valid = Validator.validateEmail(email);
        if(valid)
            this.email = email;
        else
            throw new IllegalArgumentException("Invalid Email Format");
    }
}
