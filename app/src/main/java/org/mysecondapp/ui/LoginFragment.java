package org.mysecondapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.mysecondapp.R;

/**
 * Created by Shawn Li on 10/9/2016.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LoginActivity activity;
    private CheckBox showPassword;
    private CheckBox rememberMe;
    private EditText userEdit;
    private EditText pwdEdit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity)getActivity();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (rememberMe != null && rememberMe.isChecked()){
            savedInstanceState.putString("userName", userEdit.getText().toString());
            savedInstanceState.putString("password", pwdEdit.getText().toString());
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
           userEdit.setText(savedInstanceState.get("userName").toString());
            pwdEdit.setText(savedInstanceState.get("password").toString());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.login_screen, container, false);
        Button loginButton = (Button)view.findViewById(R.id.loginBtn);
        userEdit = (EditText)view.findViewById(R.id.loginUserName);
        pwdEdit = (EditText)view.findViewById(R.id.loginPassword);
        rememberMe = (CheckBox)view.findViewById(R.id.rememberMe);
        showPassword = (CheckBox)view.findViewById(R.id.showPassword);
        showPassword.setOnCheckedChangeListener(this);
        loginButton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.d("boolean b", Boolean.toString(b));

        if (b)
            pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }
    @Override
    public void onClick(View view) {
        activity.login(userEdit.getText().toString(), pwdEdit.getText().toString());
    }

}
