package me.guillem.athm2app.Model;

import java.util.ArrayList;

public class Visita {

    String data_visita;
    String hora_visita;
    String responsable;
    String nom_visit;
    String descripcio;
    String key;
    ArrayList<String> media = new ArrayList<String>();

    public Visita(String data_visita, String hora_visita, String responsable, String nom_visit, String descripcio, ArrayList<String> media) {
        this.data_visita = data_visita;
        this.hora_visita = hora_visita;
        this.responsable = responsable;
        this.nom_visit = nom_visit;
        this.descripcio = descripcio;
        this.media = media;
    }
    public Visita(){

    }


    public String getData_visita() {
        return data_visita;
    }

    public void setData_visita(String data_visita) {
        this.data_visita = data_visita;
    }

    public String getHora_visita() {
        return hora_visita;
    }

    public void setHora_visita(String hora_visita) {
        this.hora_visita = hora_visita;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getNom_visit() {
        return nom_visit;
    }

    public void setNom_visit(String nom_visit) {
        this.nom_visit = nom_visit;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public ArrayList<String> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<String> media) {
        this.media = media;
    }


}
