package com.dod.materialsmanagement.data;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private int stroke;
    private int ea;
    private List<Materials> materials;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getEa() {
        return ea;
    }

    public void setEa(int ea) {
        this.ea = ea;
    }

    public List<Materials> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Materials> materials) {
        this.materials = materials;
    }

    @Override
    public String toString() {
        return "Products{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stroke=" + stroke +
                ", ea=" + ea +
                ", materials=" + materials +
                '}';
    }
}
