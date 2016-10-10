package org.mysecondapp.ui;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.mysecondapp.R;
import org.mysecondapp.UserData;


public class MyActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 2;
    private boolean isNewRecord;
    private UserData intentPass;
    private Bitmap pic;
    private boolean userIsAdmin;
    // https://developer.android.com/training/camera/photobasics.html
    @Override
    @TargetApi(16)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            pic = (Bitmap) extras.get("data");
            ImageButton cameraButton = (ImageButton)findViewById(R.id.cameraButton);
            cameraButton.setImageDrawable(null);
            cameraButton.setImageBitmap(pic);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Intent intent = getIntent();
        intentPass = intent.getParcelableExtra("userData");
        isNewRecord = intent.getBooleanExtra("new", true);
        userIsAdmin = intent.getBooleanExtra("isAdmin", true);
        if (intentPass != null ) {

            ((EditText)findViewById(R.id.nameEdit)).setText(intentPass.getName());
            ((EditText)findViewById(R.id.dobEdit)).setText(intentPass.getDob());
           ((EditText)findViewById(R.id.addressEdit)).setText(intentPass.getAddress());
            ((EditText)findViewById(R.id.emailEdit)).setText(intentPass.getEmail());
            ((EditText)findViewById(R.id.phoneEdit)).setText(intentPass.getPhone());
            String address = intentPass.getAddress();
            Spinner sp = (Spinner)findViewById(R.id.spinner);
            sp.setSelection(((ArrayAdapter<String>)sp.getAdapter()).getPosition(address));
            String selectedGender = intentPass.getGender();
            RadioButton female = (RadioButton)findViewById(R.id.female);
            RadioButton male = (RadioButton)findViewById(R.id.male);
            if (selectedGender.equals("Male")) {
                male.setChecked(true);
                female.setChecked(false);
            }
            else {
                male.setChecked(false);
                female.setChecked(true);
            }
            pic = intentPass.getImage();

        }
        // for more info, https://android-arsenal.com/details/1/118
        ImageButton calendarButton = (ImageButton) findViewById(R.id.calendarButton);
        calendarButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.calendar));
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                EditText mResultText = (EditText) findViewById(R.id.dobEdit);
                                    mResultText.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear+1, dayOfMonth));
                                }
                            }
                        )
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Yes")
                        .setCancelText("No")
                        .setThemeDark();
                cdp.show(getSupportFragmentManager(), "fragment_date_picker_name");
            }
        });
        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        if (pic != null)
            cameraButton.setImageBitmap(pic);
        else
            cameraButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.camera));
        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // https://developer.android.com/training/camera/photobasics.html
                // http://stackoverflow.com/questions/19648957/take-photo-w-camera-intent-and-display-in-imageview-or-textview
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
        });
        Button okButton = (Button) findViewById(R.id.OKButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.nameEdit)).getText().toString();
                Intent intent = new Intent(getBaseContext(), SecondActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                UserData data = new UserData();
                if (name.isEmpty())
                    Toast.makeText(getBaseContext(), "Contact name required", Toast.LENGTH_SHORT).show();
                else {
                    String dob = ((EditText) findViewById(R.id.dobEdit)).getText().toString();
                    String address = ((EditText) findViewById(R.id.addressEdit)).getText().toString();
                    String email = ((EditText) findViewById(R.id.emailEdit)).getText().toString();
                    String phone = ((EditText) findViewById(R.id.phoneEdit)).getText().toString();
                    String country = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
                    String gender = (((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId() == R.id.male) ? "Male" : "Female";
                    data.setAll(name, phone, email, address, dob, pic, country, gender);
                    intent.putExtra("userData", data);
                    intent.putExtra("new", isNewRecord);
                    intent.putExtra("isAdmin", userIsAdmin);
                    startActivity(intent);

                }

            }
        });


    }

}