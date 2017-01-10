package com.tinyandfriend.project.friendstrip;

import android.text.TextUtils;

/**
 * Created by NewWy on 4/10/2559.
 */
public class Validator {
    public static boolean validateEmail(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validateCitizenId(String citizenId){
        if(citizenId.matches("^[0-9]{13}+")){

            char[] idDigit = citizenId.toCharArray();
            int givenVerify =  Integer.parseInt(""+idDigit[12]);
            int calVerify;
            int sum = 0;

            for(int i = 1; i <= 12; i++){
                sum += (14 - i) * Integer.parseInt(""+idDigit[i-1]);
            }
            sum %= 11;
            if(sum <= 1){
                calVerify = 1 - sum;
            }else{
                calVerify = 11 - sum;
            }
            return givenVerify == calVerify;
        }
        return false;
    }
}
