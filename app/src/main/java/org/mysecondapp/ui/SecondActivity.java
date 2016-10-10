package org.mysecondapp.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import org.mysecondapp.R;
import org.mysecondapp.UserDBHelper;
import org.mysecondapp.UserData;

/**
 * Created by Shawn Li on 10/5/2016.
 */
public class SecondActivity extends AppCompatActivity {

    private UserDBHelper helper = new UserDBHelper(this);
    private UserData selectedUser = null;
    private final int REQUEST_ADD_OR_EDIT = 2;
    private Intent thisIntent;
    private boolean isAdmin;

    @TargetApi(16)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        thisIntent = getIntent();
        if (thisIntent.hasExtra("userData")) {
            selectedUser = thisIntent.getParcelableExtra("userData");
            if (thisIntent.getBooleanExtra("new", true))
                helper.addRecord(selectedUser);
            else
                helper.updateRecord(selectedUser);
        }
        isAdmin = thisIntent.getBooleanExtra("isAdmin", false);


        final Button editBtn = (Button) findViewById(R.id.editBtn);
        final Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
        Button newBtn = (Button) findViewById(R.id.newBtn);
        if (!isAdmin) {
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
            newBtn.setEnabled(false);
        }
        final TextView newTextView = (TextView) findViewById(R.id.newTextView);
        final ImageView im = (ImageView) findViewById(R.id.imageView);
        final ArrayList<UserData> list = helper.getAllRecords();
        final ListView newListView = (ListView) findViewById(R.id.newListView);
        final ArrayAdapter<UserData> adapter = new ArrayAdapter<UserData>(this, android.R.layout.simple_list_item_1, list);
        if (selectedUser != null) {
            newTextView.setText(selectedUser.detailedToString());
            newListView.setSelection(adapter.getPosition(selectedUser));
            try {
                im.setImageBitmap(selectedUser.getImage());
            } catch (Exception io) {
                Log.e(io.getClass().getName(), io.getMessage() + io.getStackTrace());
            }
        }
        else {
        editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }


        newListView.setAdapter(adapter);
        newListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isAdmin) {
                    editBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                }
                selectedUser = ((UserData) adapterView.getItemAtPosition(i));
                newTextView.setText(selectedUser.detailedToString());
                try {
                    im.setImageBitmap(selectedUser.getImage());
                } catch (Exception io) {
                    Log.e(io.getClass().getName(), io.getMessage() + io.getStackTrace());
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedUser != null) {
                    list.remove(selectedUser);
                    adapter.notifyDataSetChanged();
                    helper.deleteRecord(selectedUser.getID());
                    selectedUser = null;
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (selectedUser != null) {
                    Intent intSend = new Intent(getBaseContext(), MyActivity.class);
                    intSend.putExtra("userData", selectedUser);
                    intSend.putExtra("new", false);
                    startActivity(intSend);
                }
            }
        });
        thisIntent.removeExtra("userData"); // to prevent duplication of records should screen orientation change (Activity destroys and recreates itself)


            newBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intSend = new Intent(getBaseContext(), MyActivity.class);
                    intSend.putExtra("userData", (Parcelable) null);
                    intSend.putExtra("new", true);
                    startActivity(intSend);
                }
            });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
    }

}
