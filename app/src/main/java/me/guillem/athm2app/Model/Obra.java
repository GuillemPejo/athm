package me.guillem.athm2app.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Obra implements Serializable {

    String adreça;
    String titol;
    String ref;
    String estat;
    String tecnic;
    String tip_p;
    String data_inici;
    String data_final;
    String prorroga;
    String caduc_permis;
    double lat;
    double lng;
    String key;

    public Obra(String adreça, String titol, String ref, String estat, String tecnic, String tip_p, String data_inici, String data_final, String prorroga, String caduc_permis, double lat, double lng, String key) {
        this.adreça = adreça;
        this.titol = titol;
        this.ref = ref;
        this.estat = estat;
        this.tecnic = tecnic;
        this.tip_p = tip_p;
        this.data_inici = data_inici;
        this.data_final = data_final;
        this.prorroga = prorroga;
        this.caduc_permis = caduc_permis;
        this.lat = lat;
        this.lng = lng;
        this.key = key;
    }

    public Obra(){
    }

    public String getAdreça() {
        return adreça;
    }

    public void setAdreça(String adreça) {
        this.adreça = adreça;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }


    public String getTecnic() {
        return tecnic;
    }

    public void setTecnic(String tecnic) {
        this.tecnic = tecnic;
    }

    public String getTip_p() {
        return tip_p;
    }

    public void setTip_p(String tip_p) {
        this.tip_p = tip_p;
    }

    public String getData_inici() {
        return data_inici;
    }

    public void setData_inici(String data_inici) {
        this.data_inici = data_inici;
    }

    public String getData_final() {
        return data_final;
    }

    public void setData_final(String data_final) {
        this.data_final = data_final;
    }

    public String getProrroga() {
        return prorroga;
    }

    public void setProrroga(String prorroga) {
        this.prorroga = prorroga;
    }

    public String getCaduc_permis() {
        return caduc_permis;
    }

    public void setCaduc_permis(String caduc_permis) {
        this.caduc_permis = caduc_permis;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
