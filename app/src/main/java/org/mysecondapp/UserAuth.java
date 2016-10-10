package org.mysecondapp;

import android.util.Log;
import android.widget.Toast;

import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shawn Li on 10/8/2016.
 */

public class UserAuth extends RealmObject {

    // data copied to realm without requiring objects to be POJO :)
    @PrimaryKey
    private String userName;

    private String zxcvbNm;
    private boolean adminEnabled;

    public String getUserName() {
        return this.userName;
    }
    public UserAuth() {

    }
    public UserAuth(String userName, String password, boolean isAdmin){
        this.userName = userName;
        this.zxcvbNm = password;
        this.adminEnabled = isAdmin;
    }
    public boolean isAdmin() {
        return this.adminEnabled;
    }
    public boolean authenticate(String password) {
        return this.zxcvbNm.equals(password);
    }
    public boolean setCredentials(String oldPassword, String newPassword) {
        if (authenticate(oldPassword)) {
            this.zxcvbNm = newPassword;
            return true;
        }
        else
            return false;
    }
}
