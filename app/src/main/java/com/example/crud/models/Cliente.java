package com.example.crud.models;

import java.io.Serializable;

public class Cliente implements Serializable {

    private int id;
    private String name;
    private String sexo;
    private String uf;
    private boolean vip;

    public Cliente(int id, String nome, String sexo, String uf, boolean vip) {
        this.id = id;
        this.name = nome;
        this.sexo = sexo;
        this.uf = uf;
        this.vip = vip;
    }

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.name;
    }

    public String getSexo() {
        return this.sexo;
    }

    public String getUf() {
        return this.uf;
    }

    public boolean getVip() {
        return this.vip;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((Cliente) o).id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public void adicionarCliente(Cliente cliente){
    }




}





