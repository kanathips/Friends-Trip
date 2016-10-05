package com.tinyandfriend.project.friendstrip;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void citizenId_isCorrect() throws Exception{

        String[] falseArray = {"", "1","a", "#", " ", ".", "11", "a1", "1a", "0000000000000000000"
                                , "195990050374/", "1959900503742", "3074345313715"
                                , " 3066515535753", "306651a5535753 ", "306651 5535753"};

        String[] trueArray = {"1959900503741", "3074345313711", "3066515535753", "5833131732150"
                                ,"4242273737255", "1426354045652", "6064607757330"};

        for(String s: falseArray){
            assertFalse(Validator.validateCitizenId(s));
        }

        for (String s: trueArray){
            assertTrue(Validator.validateCitizenId(s));
        }
    }
}