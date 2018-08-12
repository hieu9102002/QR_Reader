package com.example.hieu9.qr_reader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Declaring Variables
    //Khai báo biến
    Button btStartScan;
    String id;

    int counter = 0;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    ArrayAdapter adapter;

    List<String> idList;

    ListView listView;

    //On Create
    //Khi chạy ứng dụng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize temporary memory for unsynced ID
        //Khai báo bộ nhớ tạm cho ID chưa đưa lên server
        sharedPref = getSharedPreferences("ID", MODE_PRIVATE);
        editor = sharedPref.edit();

        int counterSaved = sharedPref.getInt("Counter", 0);
        counter = counterSaved;

        //Initialize QR button
        //Khai báo nút QR
        btStartScan = (Button)findViewById(R.id.btn_scan);

        //Initialize ListView for unsynced ID
        //Khai báo bảng hiển thị các ID chưa đưa lên server
        idList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, idList);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //Start QR Scan
        //Bắt đầu scan mã QR
        btStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });

    }

    //QR Scan Function
    //Hàm scan QR
    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    //Return QR Scan result and Info result
    //Hàm trả kết quả QR và Thông tin
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            } else {
                parseResult(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String name = data.getStringExtra("Name");
                String phone = data.getStringExtra("Phone");
                String email = data.getStringExtra("Email");

                parseResult(name, phone, email);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            }
        }
    }

    //QR Result parse
    //Hàm xử lý kết quả QR
    private void parseResult(String scanCode) {
        id = scanCode;

        //Get ID into temp memory for sync
        //Đưa id vào bộ nhớ tạm chưa đẩy lên server
        String counterNum = Integer.toString(counter);
        editor.putString(counterNum, id);
        Log.d("abc", Integer.toString(counter) + ' ' + id);
        counter++;
        editor.putInt("Counter", counter);
        editor.apply();

        //Update id on ListView
        //Cập nhật ID trên bảng hiển thị
        idList.add(counterNum + ' ' + id);
        adapter.notifyDataSetChanged();
    }

    private void parseResult(String name, String phone, String email) {
        //Get Info into temp memory for sync
        //Đưa thông tin vào bộ nhớ tạm chưa đẩy lên server
        String counterNum = Integer.toString(counter);
        String condensedInfo = name + ' ' + phone + ' ' + email;
        editor.putString(counterNum, condensedInfo);
        Log.d("abc", Integer.toString(counter) + ' ' + condensedInfo);
        counter++;
        editor.putInt("Counter", counter);
        editor.apply();

        //Update info on ListView
        //Cập nhật thông tin trên bảng hiển thị
        idList.add(counterNum + ' ' + condensedInfo);
        adapter.notifyDataSetChanged();
    }

    //Update button press
    //Bấm nút Update
    public void Update(View view){
        //TODO: Push data to server
        if(sharedPref.getString("0", null) == null || sharedPref.getString("0", null).isEmpty()){
            Toast.makeText(this, "No Data to push", Toast.LENGTH_SHORT).show();
        }
        else {
            for(int i = 0; i <= counter; i++){
                String j = Integer.toString(i);
                String data = sharedPref.getString(j, null);
            }
        }

        //Delete Temporary Memory
        //Xóa bộ nhớ tạm
        counter = 0;
        editor.clear();
        editor.putInt("Counter", 0);
        editor.apply();

        //Delete ListView
        //Xóa bảng hiển thị
        idList.clear();
        adapter.notifyDataSetChanged();
    }

    //Info button press
    //Bấm nút Info
    public void InfoActivity(View view){
        Intent intent = new Intent(this, QRscan.class);
        startActivityForResult(intent, 1);
    }


}