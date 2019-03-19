package com.plusbueno.plusbueno.presenter;

import com.plusbueno.plusbueno.networking.DoLogInTask;
import com.plusbueno.plusbueno.parser.LoginParser;
import com.plusbueno.plusbueno.parser.exception.LoginFailedException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;
import com.plusbueno.plusbueno.view.LogInView;

public class LogInPresenter implements Presenter {

    private LogInView view;

    public LogInPresenter(LogInView view) {
        this.view = view;
    }

    public void tryLogIn(String userName, String password) {
        startLogIn(userName, password);
    }

    private void startLogIn(String userName, String password) {
        new DoLogInTask(this).execute(userName, password);
    }

    public void endLogIn(String result) {
        if (result.equals("Network error")) {
            view.networkError();
        } else if (result.equals("Login failed")) {
            view.loginFailed();
        } else if (result.startsWith("Error:")) {
            view.loginFailedCustom(result);
        }
        else {
            view.customerHome();
        }

//        view.customerHome();
    }
}
