package com.dod.materialsmanagement.data;

import java.io.Serializable;

public class Materials implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int ea;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEa() {
        return ea;
    }

    public void setEa(int ea) {
        this.ea = ea;
    }

    @Override
    public String toString() {
        return "Materials{" +
                "name='" + name + '\'' +
                ", ea=" + ea +
                '}';
    }
}
