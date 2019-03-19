package com.plusbueno.plusbueno.presenter;

import com.plusbueno.plusbueno.parser.LoginParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogInPresenterTest {

    private String authKey;
    private String targetkey= "tbdtarget";
    private String userName = "as";
    private String password = "123";
    LoginParser parser;


    @Before
    public void setUp() throws Exception {
        parser = new LoginParser();
        String authKey = parser.doLogin(userName, password);
    }

    @Test
    public void tryLogIn() {
        assertEquals(authKey, targetkey);
    }

    @After
    public void tearDown() throws Exception {
        parser = null;
        assertNull(parser);
    }

}