package org.mysecondapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.mysecondapp.R;

/**
 * Created by Shawn Li on 10/8/2016.
 */

public class WelcomeActivity extends AppCompatActivity{

    private boolean userIsAdmin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        Intent intent = getIntent();
        TextView welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText(getString(R.string.welcome, intent.getStringExtra("userName")));
        userIsAdmin = intent.getBooleanExtra("isAdmin", false);
        Button addButton = (Button)findViewById(R.id.addButton);
        if (!userIsAdmin)
            addButton.setEnabled(false);
        else
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getBaseContext(), MyActivity.class);
                newIntent.putExtra("isAdmin", userIsAdmin);
                startActivity(newIntent);
            }
        });
        Button viewButton = (Button)findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getBaseContext(), SecondActivity.class);
                newIntent.putExtra("isAdmin", userIsAdmin);
                startActivity(newIntent);
            }
        });
    }
}
