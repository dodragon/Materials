package com.dod.materialsmanagement.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.dod.materialsmanagement.OrderStatus;
import com.dod.materialsmanagement.R;
import com.dod.materialsmanagement.data.Materials;
import com.dod.materialsmanagement.data.Products;

import java.util.List;

public class OrderReadyProduct extends DialogFragment implements View.OnClickListener {

    Products vo;
    List<Materials> bluePrint;
    Context context;

    TextView eaTv;

    public OrderReadyProduct(Products vo, List<Materials> bluePrint, Context context) {
        this.vo = vo;
        this.context = context;
        this.bluePrint = bluePrint;
    }

    public static OrderReadyProduct getInstance(Products vo, List<Materials> bluePrint, Context context){
        OrderReadyProduct v = new OrderReadyProduct(vo, bluePrint, context);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.order_ready_dialog, null);

        eaTv = view.findViewById(R.id.ea);

        ((TextView)view.findViewById(R.id.name)).setText(vo.getName());
        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.add).setOnClickListener(addOrMinus);
        view.findViewById(R.id.minus).setOnClickListener(addOrMinus);

        builder.setView(view);
        return builder.create();
    }

    View.OnClickListener addOrMinus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int addOrMinus;
            if(v.getId() == R.id.add){
                addOrMinus = 1;
            }else {
                addOrMinus = -1;
            }

            int ea = Integer.parseInt(eaTv.getText().toString()) + addOrMinus;

            if(ea > 0){
                eaTv.setText(String.valueOf(ea));

                List<Materials> materials = vo.getMaterials();
                for(int i=0;i<materials.size();i++){
                    for(int j=0;j<bluePrint.size();j++){
                        if(materials.get(i).getName().equals(bluePrint.get(j).getName())){
                            Materials mVo = new Materials();
                            mVo.setName(materials.get(i).getName());
                            mVo.setEa(materials.get(i).getEa() + (bluePrint.get(j).getEa()
                                    * addOrMinus));
                            materials.set(i, mVo);
                            break;
                        }
                    }
                }

                ((OrderStatus)OrderStatus.context).orderReadyMaterials.put(vo.getName(), materials);
                List<Products> pVo = ((OrderStatus)OrderStatus.context).orderReadyProducts;
                for(int i=0;i<pVo.size();i++){
                    if(pVo.get(i).getName().equals(vo.getName())){
                        Products newPvo = ((OrderStatus)OrderStatus.context).orderReadyProducts.get(i);
                        newPvo.setEa(newPvo.getEa() - 1);
                        ((OrderStatus)OrderStatus.context).orderReadyProducts.set(i, newPvo);
                        break;
                    }
                }
            }else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("제품 수량");
                dialog.setMessage("제품 수량이 0입니다. 삭제하시겠습니까?");
                dialog.setPositiveButton("네"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((OrderStatus)OrderStatus.context).orderReadyMaterials.remove(vo.getName());
                                ((OrderStatus)OrderStatus.context).bluePrints.remove(vo.getName());
                                List<Products> pVo = ((OrderStatus)OrderStatus.context).orderReadyProducts;
                                for(int i=0;i<pVo.size();i++){
                                    if(pVo.get(i).getName().equals(vo.getName())){
                                        ((OrderStatus)OrderStatus.context).orderReadyProducts.remove(i);
                                        break;
                                    }
                                }

                                dialog.dismiss();
                                OrderReadyProduct.this.dismiss();
                            }
                        });
                dialog.setNegativeButton("아니오", (dialog1, which)
                        -> dialog1.dismiss());
                dialog.create();
                dialog.show();
            }
        }
    };

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
