package com.example.hieu9.qr_reader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class QRscan extends AppCompatActivity {

    //Declare Varibles
    //Khai báo biến
    EditText name;
    EditText phone;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        //Initialize Variables
        //Khởi đầu biến
        name = (EditText)findViewById(R.id.Name);
        phone = (EditText)findViewById(R.id.Phone);
        email = (EditText)findViewById(R.id.Email);
    }

    public void Confirm(View view){
        String nameStr = name.getText().toString();
        String phoneStr = phone.getText().toString();
        String emailStr = email.getText().toString();

        Log.d("abc", nameStr + phoneStr + emailStr);

        if(!nameStr.isEmpty() && !phoneStr.isEmpty() && !emailStr.isEmpty()){
            Log.d("abc", nameStr + phoneStr + emailStr);
            Intent returnIntent = new Intent();

            returnIntent.putExtra("Name", nameStr);
            returnIntent.putExtra("Phone", phoneStr);
            returnIntent.putExtra("Email", emailStr);

            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        } else {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }
}
