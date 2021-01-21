package com.dod.materialsmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.status).setOnClickListener(btn);
        findViewById(R.id.management).setOnClickListener(btn);
    }

    View.OnClickListener btn = v -> {
        Intent intent;
        if(v.getId() == R.id.status){
            intent = new Intent(MainActivity.this, MaterialsStatusMain.class);
        }else {
            intent = new Intent(MainActivity.this, ManagementActivity.class);
        }

        startActivity(intent);
    };
}