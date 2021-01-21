package com.dod.materialsmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dod.materialsmanagement.adapter.OrderReadyAdapter;
import com.dod.materialsmanagement.data.Materials;
import com.dod.materialsmanagement.data.Products;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatus extends AppCompatActivity {

    FirebaseFirestore db;

    public static Context context;
    public List<Materials> extraMaterials;
    public List<Products> orderReadyProducts;
    public Map<String, List<Materials>> orderReadyMaterials;
    public Map<String, List<Materials>> bluePrints;
    public List<Materials> orderAllMaterials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        context = this;

        db = FirebaseFirestore.getInstance();
        orderReadyMaterials = new HashMap<>();
        extraMaterials = new ArrayList<>();
        orderReadyProducts = new ArrayList<>();
        bluePrints = new HashMap<>();
        orderAllMaterials = new ArrayList<>();

        extraMaterials = getExtraMaterials();
        orderReadyProducts = getOrderReadyProducts();
        orderAllMaterials = getOrderAllMaterials();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        RecyclerView pRecycle = findViewById(R.id.p_recycle);
        RecyclerView mRecycle = findViewById(R.id.m_recycle);
        pRecycle.setLayoutManager(manager);
        mRecycle.setLayoutManager(manager);

        OrderReadyAdapter pAdapter = new OrderReadyAdapter(orderReadyProducts, true, this);
        OrderReadyAdapter mAdapter = new OrderReadyAdapter(orderAllMaterials, false, this);

        pRecycle.setAdapter(pAdapter);
        mRecycle.setAdapter(mAdapter);
    }

    private List<Materials> getOrderAllMaterials(){
        List<Materials> list = new ArrayList<>();

        for(int i=0;i<orderReadyProducts.size();i++){
            list.addAll(orderReadyMaterials.get(orderReadyProducts.get(i).getName()));
        }

        return list;
    }

    private List<Materials> getExtraMaterials(){
        List<Materials> list = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection("ExtraMaterials").get();

        while (true){
            if(task.isComplete()){
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Materials vo = new Materials();
                        vo.setName(document.get("name").toString());
                        vo.setEa(Integer.parseInt(document.get("ea").toString()));
                        list.add(vo);
                    }
                }else {
                    Toast.makeText(this, "자재 수량 조회 실패", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }

        return list;
    }

    private List<Products> getOrderReadyProducts(){
        List<Products> list = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection("OrderReady").get();

        while (true){
            if(task.isComplete()){
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Products vo = new Products();
                        vo.setName(document.get("name").toString());
                        vo.setEa(Integer.parseInt(document.get("ea").toString()));
                        vo.setType(document.get("type").toString());
                        vo.setStroke(Integer.parseInt(document.get("ea").toString()));
                        bluePrints.put(vo.getName(), getBlueprint(document.get("name").toString()));
                        vo.setMaterials(bluePrints.get(vo.getName()));
                        list.add(vo);
                    }
                }else {
                    Toast.makeText(this, "발주 대기 조회 실패", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }

        return list;
    }

    private List<Materials> getBlueprint(String name){
        List<Materials> list = new ArrayList<>();
        Task<DocumentSnapshot> task = db.collection("Blueprint")
                .document(name).get();

        while (true){
            if(task.isComplete()){
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> map = document.getData();

                    for(String materialName : map.keySet()){
                        Materials vo = new Materials();
                        vo.setName(materialName);
                        vo.setEa((Integer) map.get(materialName));
                        list.add(vo);
                    }
                }else {
                    Toast.makeText(this, "발주 대기 조회 실패", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }

        if(!list.isEmpty()){
            orderReadyMaterials.put(name, list);
        }
        return list;
    }

    private void calculatorMaterials(String materialName, int calculateEa){
        //ea = Minus 가능
        Materials vo = null;
        int position = -1;
        for(int i=0;i<extraMaterials.size();i++){
            if(extraMaterials.get(i).getName().equals(materialName)){
                vo = extraMaterials.get(i);
                position = i;
                break;
            }
        }

        assert vo != null;
        vo.setEa(vo.getEa() + calculateEa);

        extraMaterials.set(position, vo);
    }

    private void orderCancel(String name, int ea){
        for(int i=0;i<orderReadyProducts.size();i++){
            if(orderReadyProducts.get(i).getName().equals(name)){
                Products vo = orderReadyProducts.get(i);
                List<Materials> bluePrint = vo.getMaterials();
                List<Materials> orderReadyMaterial = orderReadyMaterials.get(name);
                if(vo.getEa() == ea){
                    vo.setEa(vo.getEa() - ea);
                    orderReadyProducts.set(i, vo);

                    for(int j=0;j<bluePrint.size();j++){
                        for(int k=0;k<orderReadyMaterial.size();k++){
                            if(bluePrint.get(j).getName().equals(orderReadyMaterial.get(k).getName())){
                                Materials bluePrintVo = bluePrint.get(j);
                                Materials deleteVo = orderReadyMaterial.get(k);
                                if(bluePrintVo.getEa() == deleteVo.getEa()){
                                    orderReadyMaterial.remove(k);
                                }else {
                                    deleteVo.setEa(deleteVo.getEa() - (ea * bluePrintVo.getEa()));
                                    orderReadyMaterial.set(k, deleteVo);
                                }
                            }
                        }
                    }

                    orderReadyMaterials.put(name, orderReadyMaterial);
                }else {
                    orderReadyProducts.remove(i);
                    orderReadyMaterials.remove(name);
                }
                break;
            }
        }
    }

    public void onToggleClicked(View v){
        boolean on = ((ToggleButton)v).isChecked();
        NestedScrollView scrollView = findViewById(R.id.scrollView);
        if(on){
            scrollView.scrollTo(0, findViewById(R.id.product_title).getTop());
        }else{
            scrollView.scrollTo(0, findViewById(R.id.material_title).getTop());
        }
    }
}