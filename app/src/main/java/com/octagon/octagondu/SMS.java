package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class SMS extends AppCompatActivity {
    private Button send;
    private EditText sms;
    private EditText mail;
    private Button choose;
    String selectedBusNumber;

    private final int CONA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        send = findViewById(R.id.send);
        sms = findViewById(R.id.sms);
        Spinner spinnerNumber = findViewById(R.id.spinnerNumber);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.devNumber, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumber.setAdapter(adapter);

        spinnerNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedBusNumber = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SMS.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                } else {
                    ActivityCompat.requestPermissions(SMS.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONA) {
            if (resultCode == RESULT_OK) {
                conTactPick(data);
            }
        }
    }

    private void conTactPick(Intent data) {
        Cursor cursor = null;

        try {
            String phoneno = null;
            Uri uri = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(uri, null, null, null);
            }
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            if (cursor.moveToFirst()) {
                phoneno = cursor.getString(phoneIndex);
                mail.setText(phoneno);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSms();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSms() {
        String ma = selectedBusNumber;
        if(Objects.equals(selectedBusNumber, "Sajid Hasan"))ma = "01316171496";
        else if(Objects.equals(selectedBusNumber, "Emon Khan"))ma = "01521762137";
        else if(Objects.equals(selectedBusNumber, "Atikur Hridoy"))ma = "01521797043";
        String content = sms.getText().toString();

        if (!ma.isEmpty() && !content.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(ma, null, content, null, null);

            Toast.makeText(this, "Sms send successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
