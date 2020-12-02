package me.guillem.athm2app.Views;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.guillem.athm2app.R;

/**
 * * Created by Guillem on 02/12/20.
 */
public class CRUDVisits extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title= extras.getString("Obra");
            TextView t1 = findViewById(R.id.titol_obra);
            t1.setText("Nova visita a l'obra "+title);
        }

    }
}
