package com.dod.materialsmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dod.materialsmanagement.data.Products;

public class ProductsStatusDetail extends AppCompatActivity {

    private String productName;
    private Products vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_status_detail);

        productName = getIntent().getStringExtra("name");
        vo = (Products) getIntent().getSerializableExtra("vo");
    }
}