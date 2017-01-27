package org.mysecondapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.mysecondapp.R;

/**
 * Created by Shawn Li on 10/9/2016.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener{
    private LoginActivity activity;
    private EditText userEdit;
    private EditText pwdEdit;
    private RadioGroup radioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.register_screen, container, false);
        userEdit = (EditText) view.findViewById(R.id.registerUser);
        pwdEdit = (EditText) view.findViewById(R.id.registerPassword);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        Button registerButton = (Button)view.findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        String userName = userEdit.getText().toString();
        if (isValid(userName)) {

            if (radioGroup.getCheckedRadioButtonId() == R.id.administrator)
                activity.register(userName, pwdEdit.getText().toString(), true);
            else if (radioGroup.getCheckedRadioButtonId() == R.id.user)
                activity.register(userName, pwdEdit.getText().toString(), false);
            else
                Toast.makeText(activity.getBaseContext(), "Please specify administrator or user", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(activity.getBaseContext(), "Username must contain only letters and/or digits", Toast.LENGTH_SHORT).show();
    }
    private boolean isValid(String str) // letters and numbers only
    {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c))
                return false;
        }
        return !str.isEmpty(); // do not allow empty strings for usernames!
    }
}
