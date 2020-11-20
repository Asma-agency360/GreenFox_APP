package com.example.aptech.greenfox;

public class waiterStore {
    String acc_name;
    String acc_pass;
    String acc_email;

    public waiterStore()
    {
    }

    public waiterStore(String name_waiter, String email_waiter, String pass_waiter)
    {
       acc_name=name_waiter;
       acc_email=email_waiter;
       acc_pass=pass_waiter;
    }

    public String getAcc_email() {
        return acc_email;
    }

    public void setAcc_email(String acc_email) {
        this.acc_email = acc_email;
    }

    public String getAcc_pass() {
        return acc_pass;
    }

    public void setAcc_pass(String acc_pass) {
        this.acc_pass = acc_pass;
    }

    public String getAcc_name() {
        return acc_name;
    }

    public void setAcc_name(String acc_name) {
        this.acc_name = acc_name;
    }
}
