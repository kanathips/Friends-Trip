package com.tinyandfriend.project.friendstrip;


/**
 * Created by NewWy on 3/10/2559.
 */
class SignUpInfo {

    private String email;
    private String password;
    private String citizenId;
    private String username;
    private String displayName;
    private String fName;
    private String lName;
    private String phoneNumber;
    private String dateOfBirth;

    public SignUpInfo() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password, String rePassword) throws IllegalArgumentException{
        if(rePassword.equals(password))
            this.setPassword(password);
        else
            throw new IllegalArgumentException("Password And Confirm Password are Miss Match");
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void setPassword(String password) {
        this.password = password;
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

