package me.guillem.athm2app.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import me.guillem.athm2app.MainActivity;
import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.Model.Visita;
import me.guillem.athm2app.R;

public class Utils {
    public static List<Obra> DataCache =new ArrayList<>();
    public static List<Visita> DataCacheVisits =new ArrayList<>();
    public static String searchString = "";

    public static void show(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean validar(EditText... editTexts){
        EditText nomtxt = editTexts[0];
        EditText descripciotxt = editTexts[1];
        EditText adreçatxt = editTexts[2];
        EditText msgbeacontxt = editTexts[3];
        EditText bId = editTexts[4];

        if(nomtxt.getText() == null || nomtxt.getText().toString().isEmpty()){
            nomtxt.setError("El nom és obligatòri!");
            return false;
        }
        if(descripciotxt.getText() == null || descripciotxt.getText().toString().isEmpty()){
            descripciotxt.setError("La descripcio és obligatòria!");
            return false;
        }
        if(adreçatxt.getText() == null || adreçatxt.getText().toString().isEmpty()){
            adreçatxt.setError("L'adreça és obligatòria!");
            return false;
        }
        if(msgbeacontxt.getText() == null || msgbeacontxt.getText().toString().isEmpty()){
            msgbeacontxt.setError("El missatge de la notifiació és obligatòri!");
            return false;
        }
        if(bId.getText() == null || bId.getText().toString().isEmpty()){
            bId.setError("L'ID del Beacon és obligatòri!");
            return false;
        }
        return true;

    }

    public static void openActivity(Context c,Class clazz){
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
    }

    public static void showInfoDialog(final AppCompatActivity activity, String titol, String msg) {
        new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.blau)
                .setButtonsColorRes(R.color.blau)
                .setIcon(R.drawable.ic_baseline_info_24)
                .setTitle(titol)
                .setMessage(msg)
                .setPositiveButton("NO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setNeutralButton("Anar a l'inici", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openActivity(activity, MainActivity.class);
                    }
                })
                .setNegativeButton("SI", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .show();
    }

    public static void sendObraToActivity(Context c, Obra comerce, Class clazz){
        Intent i=new Intent(c,clazz);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("OBRA_KEY", comerce);
        c.startActivity(i);
    }

    public static Obra receiveObra(Intent intent, Context c) {
        try {
            return (Obra) intent.getSerializableExtra("OBRA_KEY");
        } catch (Exception e) {
            e.printStackTrace();
            //show(c,"ERROR REBUDA DE L'OBRA: "+e.getMessage());
        }
        return null;
    }

    public static void showProgressBar(ProgressBar pb){
        pb.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar pb){
        pb.setVisibility(View.GONE);
    }

    public static DatabaseReference getDatabaseRefence() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static void showToastMessage(Context c, String message) {
        Toast toast = Toast.makeText(c, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

