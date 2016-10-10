package org.mysecondapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import org.mysecondapp.R;
import org.mysecondapp.UserAuth;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

/**
 * Created by Shawn Li on 10/8/2016.
 */
public class LoginActivity extends AppCompatActivity {


    private Realm realm;
    private boolean userIsAdmin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_tabs);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        FragmentTabHost host = (FragmentTabHost)findViewById(R.id.loginOrRegister);
        host.setup(this, getSupportFragmentManager(), R.id.loginOrRegister);
        TabHost.TabSpec tabLogin = host.newTabSpec("Login").setIndicator("Login");
        host.addTab(tabLogin, LoginFragment.class, null);
        TabHost.TabSpec tabRegister = host.newTabSpec("Register").setIndicator("Register");
        host.addTab(tabRegister, RegisterFragment.class, null);
        host.setCurrentTab(0);

    }


    public void onDestroy() {
        super.onDestroy();
       realm.close();
    }
     private boolean authenticate(String userName, String password) {
        RealmQuery<UserAuth> query = realm.where(UserAuth.class);
        query.equalTo("userName", userName);
        RealmResults<UserAuth> results = query.findAll();

        for (UserAuth user : results) {
            Log.d("u " + user.getUserName(), Boolean.toString(user.isAdmin()));
            if (user.getUserName().equals(userName)) {
                userIsAdmin = user.isAdmin();
                return user.authenticate(password);
            }
        }
        return false;
    }
    private void addNew(String userName, String password, boolean isAdmin) {
        try {
            realm.beginTransaction();
            realm.copyToRealm(new UserAuth(userName, password, isAdmin));
            realm.commitTransaction();
        } catch (RealmException re) {
            Toast.makeText(this, "Username already taken!", Toast.LENGTH_SHORT).show();
        }
    }
    private void authStart(String userName, boolean isAdmin) {
        Intent intent = new Intent(getBaseContext(), Welcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userName", userName);
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }
    void login(String userName, String password) {
            if (authenticate(userName, password)) {
                authStart(userName, userIsAdmin);
            }
            else
                Toast.makeText(getBaseContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    void register(String userName, String password, boolean isAdmin) {
           addNew(userName, password, isAdmin);
            authStart(userName, isAdmin);
    }
}
