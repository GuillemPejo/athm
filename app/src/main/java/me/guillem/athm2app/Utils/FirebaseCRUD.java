package me.guillem.athm2app.Utils;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import me.guillem.athm2app.R;
import static me.guillem.athm2app.Utils.Utils.DataCache;

import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;



public class FirebaseCRUD {

    public void insert(final AppCompatActivity a, final DatabaseReference mDatabaseRef, final ProgressBar pb, final Obra obra) {
        //Comprova que passis un Comerç vàlid. Si no, retorna false.
        if (obra == null) {
            Utils.showInfoDialog(a,"HA FALLAT LA VALIDACIÓ","Obra és null");
            return;
        } else {
            //En cas contrari, s'intenta desar les dades a la realtime database de Firebase
            Utils.showProgressBar(pb);

            mDatabaseRef.child("Obra").push().setValue(obra).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Utils.hideProgressBar(pb);

                            if(task.isSuccessful()){
                                //Utils.openActivity(a, ComercosActivity.class);
                                Utils.show(a,"Felicitats! S'ha afegit correctament");
                            }else{
                                Utils.showInfoDialog(a,"Opss.. Alguna cosa ha anat malament",task.getException().
                                        getMessage());
                            }
                        }
                    });
        }
    }

    public void select(final AppCompatActivity a, DatabaseReference db, final ProgressBar pb, final RecyclerView rv, final RecyclerAdapterCardsHome adapter) {
        Utils.showProgressBar(pb);

        db.child("Obra").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataCache.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Obtenim l'objecte comerç per tal d'omplir l'ArrayList
                        Obra obra = ds.getValue(Obra.class);
                        obra.setKey(ds.getKey());
                        DataCache.add(obra);
                    }

                    adapter.notifyDataSetChanged();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.hideProgressBar(pb);
                            rv.smoothScrollToPosition(DataCache.size());
                        }
                    });
                }else {
                    Utils.show(a,"No s'han trobat nous items");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
                Utils.hideProgressBar(pb);
                Utils.showInfoDialog(a,"Opss.. Alguna cosa ha anat malament",databaseError.getMessage());
            }
        });
    }

    public void update(final AppCompatActivity a,final DatabaseReference mDatabaseRef, final ProgressBar pb, final Obra updatedObra) {

        if(updatedObra == null){
            Utils.showInfoDialog(a,"HA FALLAT LA VALIDACIÓ","Comerç és null");
            return;
        }

        Utils.showProgressBar(pb);
        mDatabaseRef.child("Obra").child(updatedObra.getKey()).setValue(updatedObra)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utils.hideProgressBar(pb);

                        if(task.isSuccessful()){
                            Utils.show(a, updatedObra.getTitol() + " Actualitzat satisfactoriament.");
                            //Utils.openActivity(a, ComercosActivity.class);
                        }else {
                            Utils.showInfoDialog(a,"Opps.. Alguna cosa ha anat malament",task.getException().
                                    getMessage());
                        }
                    }
                });
    }

    public void delete(final AppCompatActivity a, final DatabaseReference mDatabaseRef, final ProgressBar pb, final Obra selectedObra) {
        Utils.showProgressBar(pb);
        final String selectedObraKey = selectedObra.getKey();
        mDatabaseRef.child("Obra").child(selectedObraKey).removeValue().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Utils.hideProgressBar(pb);

                        if(task.isSuccessful()){
                            Utils.show(a, selectedObra.getTitol() + " S'ha esborrat correctament.");
                            //Utils.openActivity(a, ComercosActivity.class);
                        }else{
                            Utils.showInfoDialog(a,"Opps.. Alguna cosa ha anat malament",task.getException().getMessage());
                        }
                    }
                });
    }
}