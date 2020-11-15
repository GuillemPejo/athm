package me.guillem.athm2app.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import me.guillem.athm2app.Obra;
import me.guillem.athm2app.R;
import me.guillem.athm2app.RecyclerAdapterCardsMap;


public class InfoFragment extends Fragment {

    Obra obra;
    TextView resp, ref, state, tpermis, prorroga, dini,dfin;

    public InfoFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_info, container, false);

        resp = (TextView) v.findViewById(R.id.responsable);
        ref = (TextView) v.findViewById(R.id.referencia);
        state = (TextView) v.findViewById(R.id.estat);
        tpermis = (TextView) v.findViewById(R.id.t_permis);
        prorroga = (TextView) v.findViewById(R.id.prorroga);
        dini = (TextView) v.findViewById(R.id.data_inici);
        dfin = (TextView) v.findViewById(R.id.data_fi);
        ImageView aniView = (ImageView)v.findViewById(R.id.circle);
        FrameLayout timeline = (FrameLayout)v.findViewById(R.id.barra);



        Bundle b3=getArguments();
        Obra obra = (Obra) b3.getSerializable("key");
        resp.setText(obra.getTecnic());
        ref.setText(obra.getRef());
        state.setText(obra.getEstat());
        tpermis.setText(obra.getTip_p());
        prorroga.setText(obra.getProrroga());
        dini.setText(obra.getData_inici());
        dfin.setText(obra.getData_final());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date1 = LocalDate.parse(obra.getData_inici(), dtf);
        LocalDate date2 = LocalDate.parse(obra.getData_final(),dtf);
        long duracio_obra = ChronoUnit.DAYS.between(date1, date2);
        System.out.println ("Days: " + duracio_obra);
        LocalDate today = LocalDate.now();
        long dia_avui = ChronoUnit.DAYS.between(date1, today);
        System.out.println ("Days: " + dia_avui);
        long posicio = (320/duracio_obra)*dia_avui;
        System.out.println ("posicio: " + posicio);
        ObjectAnimator animation = ObjectAnimator.ofFloat(aniView, "translationY", posicio);
        animation.setDuration(2000);
        animation.start();



        // Inflate the layout for this fragment
        return v;
    }
}