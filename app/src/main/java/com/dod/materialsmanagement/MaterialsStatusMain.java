package com.dod.materialsmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dod.materialsmanagement.adapter.StatusMaterialsAdapter;
import com.dod.materialsmanagement.adapter.StatusProductsAdapter;
import com.dod.materialsmanagement.data.Materials;
import com.dod.materialsmanagement.data.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialsStatusMain extends AppCompatActivity {

    FirebaseFirestore db;

    RecyclerView recyclerView;
    List<Materials> materialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials_status_main);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        findViewById(R.id.tab_m).setOnClickListener(tab);
        findViewById(R.id.tab_p).setOnClickListener(tab);

        findViewById(R.id.tab_m).performClick();

        findViewById(R.id.order_status).setOnClickListener(v -> {
            Intent intent = new Intent(MaterialsStatusMain.this, OrderStatus.class);
            startActivity(intent);
        });
    }

    View.OnClickListener tab = v -> {
        if(v.getId() == R.id.tab_m){
            getMaterials();
        }else {
            getProducts();
        }
    };

    //Extra
    private void getMaterials(){
        db.collection("ExtraMaterials")
                .document("Fl8qcQYG4HjqU6L9BZOs")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        Map<String, Object> result = document.getData();

                        materialList = new ArrayList<>();

                        for(Map.Entry<String, Object> entry : result.entrySet()){
                            Materials mVo = new Materials();
                            mVo.setName(entry.getKey());
                            mVo.setEa((int)entry.getValue());
                            materialList.add(mVo);
                        }

                        settingMaterialList();
                    }else {
                        Toast.makeText(MaterialsStatusMain.this,
                                "자재 조회 실패", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void settingMaterialList(){
        StatusMaterialsAdapter adapter = new StatusMaterialsAdapter(materialList
                , MaterialsStatusMain.this);
        recyclerView.setAdapter(adapter);
    }

    //Blueprint
    private void getBluePrint(List<Products> productsList){
        db.collection("Blueprint")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, List<Materials>> blueprint = new HashMap<>();
                        for(QueryDocumentSnapshot document : task.getResult()){
                            List<Materials> list = new ArrayList<>();
                            Map<String, Object> result = document.getData();

                            for(Map.Entry<String, Object> entry : result.entrySet()){
                                Materials mVo = new Materials();
                                mVo.setName(entry.getKey());
                                mVo.setEa((int)entry.getValue());
                                list.add(mVo);
                            }

                            blueprint.put(document.getId(), list);
                        }

                        calculateProducts(productsList, blueprint);
                    }else {
                        Toast.makeText(MaterialsStatusMain.this,
                                "설계도 조회 실패", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void calculateProducts(List<Products> pList, Map<String, List<Materials>> blueprint){

    }

    private void getProducts(){
        db.collection("Products")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Products> list = new ArrayList<>();
                        for(QueryDocumentSnapshot document : task.getResult()){
                            Products pVo = new Products();
                            pVo.setName(document.getId());
                            pVo.setType(document.get("type").toString());
                            pVo.setStroke(Integer.parseInt(document.get("stroke").toString()));
                            list.add(pVo);
                        }

                        getBluePrint(list);
                    }else {
                        Toast.makeText(MaterialsStatusMain.this,
                                "제품 조회 실패", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void settingProductList(List<Products> list){
        StatusProductsAdapter adapter = new StatusProductsAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }
}