package com.tinyandfriend.project.friendstrip.info;


import com.google.firebase.database.Exclude;
import com.tinyandfriend.project.friendstrip.Validator;

/**
 * Created by NewWy on 3/10/2559.
 */
public class SignUpInfo extends Object{

    @Exclude
    private String password;
    private String email;
    private String citizenId;
    private String displayName;
    private String fName;
    private String lName;
    private String phoneNumber;
    private String dateOfBirth;

    public SignUpInfo() {

    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password, String rePassword) throws IllegalArgumentException{
        if(password.equals(rePassword))
            this.password = password;
        else
            throw new IllegalArgumentException("Password confirm not match");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) throws IllegalArgumentException {
        boolean checkResult = Validator.validateCitizenId(citizenId);
        if(checkResult){
            this.citizenId = citizenId;
        }else{
            throw new IllegalArgumentException("Invalid Citizen Id");
        }
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        boolean valid = Validator.validateEmail(email);
        if(valid)
            this.email = email;
        else
            throw new IllegalArgumentException("Invalid Email Format");
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}

