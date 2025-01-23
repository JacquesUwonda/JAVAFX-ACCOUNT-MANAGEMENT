package com.example.comptebank.Controllers;

import java.util.Date;

public class Operation {
    private  int id;
    private int code;
    private double montant;
    private String typeOperation;
    private String proprietaire;
    private Date dateOperation;

    // Constructeur
    public Operation(int id,int code, double montant, String typeOperation, String proprietaire, Date dateOperation) {
        this.id=id;
        this.code = code;
        this.montant = montant;
        this.typeOperation = typeOperation;
        this.proprietaire = proprietaire;
        this.dateOperation = dateOperation;
    }

    // Getters et setters
    public int getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }
}
