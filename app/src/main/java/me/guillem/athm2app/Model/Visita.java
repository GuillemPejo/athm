package me.guillem.athm2app.Model;

import java.util.ArrayList;
import java.util.Date;

public class Visita {

    String key;
    String data_visita;
    String hora_visita;
    String responsable;
    String nom_visit;
    String descripcio;
    //ArrayList<String> media = new ArrayList<String>();

    public Visita(String data_visita, String hora_visita, String responsable, String nom_visit, String descripcio, String key) {
        this.data_visita = data_visita;
        this.hora_visita = hora_visita;
        this.responsable = responsable;
        this.nom_visit = nom_visit;
        this.descripcio = descripcio;
        //this.media = media;
        this.key = key;
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

/*    public ArrayList<String> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<String> media) {
        this.media = media;
    }*/


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
