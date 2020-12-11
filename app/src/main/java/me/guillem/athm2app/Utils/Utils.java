package me.guillem.athm2app.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonAdapter;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import me.guillem.athm2app.MainActivity;
import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.Model.Visita;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Views.CRUDVisits;

public class Utils {
    public static List<Obra> DataCache =new ArrayList<>();
    public static List<Visita> DataCacheVisits =new ArrayList<>();
    public static String searchString = "";

    public static void show(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean validar(EditText... editTexts){
        EditText nomvisita = editTexts[0];
        EditText nomrespo = editTexts[1];
        EditText data = editTexts[2];
        EditText hora = editTexts[3];
        EditText descripcio = editTexts[4];

        if(nomvisita.getText() == null || nomvisita.getText().toString().isEmpty()){
            nomvisita.setError("El nom de la visita és obligatòri!");
            return false;
        }
        if(nomrespo.getText() == null || nomrespo.getText().toString().isEmpty()){
            nomrespo.setError("El nom del responsable és obligatori!");
            return false;
        }
        if(data.getText() == null || data.getText().toString().isEmpty()){
            data.setError("La data és important");
            return false;
        }
        if(hora.getText() == null || hora.getText().toString().isEmpty()){
            hora.setError("La hora és important");
            return false;
        }
        if(descripcio.getText() == null || descripcio.getText().toString().isEmpty()){
            descripcio.setError("La descripció és recomenable");
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

    public static void selectObra(Context c){

        List<String> obres = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        for(Obra obra : DataCache){
                obres.add(obra.getTitol());
                ids.add(obra.getKey());
            }

        ArrayAdapter<String> llistaobres = new ArrayAdapter<>(c,
                android.R.layout.simple_list_item_1,
                obres);
        new LovelyChoiceDialog(c)
                .setTopColorRes(R.color.blau)
                .setTitle("Tria una obra")
                .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                .setIcon(R.drawable.ic_baseline_new_obra)
                .setMessage("Selecciona la obra a la qual vols afegir-hi una visita")
                .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                .setItems(llistaobres, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        //categoriatxt.setText(item);
                        Toast.makeText(c, "Has clikao"+item, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(c, CRUDVisits.class);
                        i.putExtra("Obra", item);
                        i.putExtra("ids", ids.get(position));
                        c.startActivity(i);
                                                //intent
                        //bundle amb item string
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

