package com.Modele;

public abstract class Document {
    protected String nom;
    protected String description;
    protected int id;
    protected String type_de_document;

    public Document(int id,String nom, String description, String type_de_document) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.type_de_document = type_de_document;
    }

    public Document(String nom, String description, String type_de_document) {
        this.nom = nom;
        this.description = description;
        this.type_de_document = type_de_document;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_de_document() {
        return type_de_document;
    }
    public void setType_de_document(String type_de_document) {
        this.type_de_document = type_de_document;
    }

}
