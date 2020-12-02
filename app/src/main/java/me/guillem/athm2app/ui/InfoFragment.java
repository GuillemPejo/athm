package me.guillem.athm2app.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.Utils;
import me.guillem.athm2app.Views.DetailObra;


public class InfoFragment extends Fragment {

    private Obra receivedObra;
    TextView resp, ref, state, tpermis, prorroga, dini, dfin;
    ImageView aniView;

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
        aniView = (ImageView) v.findViewById(R.id.circle);
        FrameLayout timeline = (FrameLayout) v.findViewById(R.id.barra);

        receiveAndShowData();
        timeBarManager();

        //Bundle b3 = getArguments();
        //Obra obra = (Obra) b3.getSerializable("key");
                // Inflate the layout for this fragment
        return v;
    }

    private void timeBarManager(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dinici = LocalDate.parse(receivedObra.getData_inici(), dtf);
        LocalDate dfinal = LocalDate.parse(receivedObra.getData_final(), dtf);
        LocalDate today = LocalDate.now();


        if (dfinal.isBefore(today)){
            System.out.println("Entra");
            ObjectAnimator animation = ObjectAnimator.ofFloat(aniView, "translationY", 800);
            animation.setDuration(2000);
            animation.start();
        }

        if(dinici.isAfter(today)){
            ObjectAnimator animation = ObjectAnimator.ofFloat(aniView, "translationY", 0);
            animation.setDuration(2000);
            animation.start();
        }

        if(dinici.isBefore(today) && dfinal.isAfter(today)){
            long duracio_obra = ChronoUnit.DAYS.between(dinici, dfinal);
            System.out.println("Days: " + duracio_obra);
            long dia_avui = ChronoUnit.DAYS.between(dinici, today);
            System.out.println("Days: " + dia_avui);
            long posicio = (800 / duracio_obra) * dia_avui;
            System.out.println("posicio: " + posicio);
            ObjectAnimator animation = ObjectAnimator.ofFloat(aniView, "translationY", posicio);
            animation.setDuration(2000);
            animation.start();
        }

    }

    private void receiveAndShowData() {
        receivedObra = Utils.receiveObra(getActivity().getIntent(), getContext());

        if (receivedObra != null) {
            resp.setText(receivedObra.getTecnic());
            ref.setText(receivedObra.getRef());
            state.setText(receivedObra.getEstat());
            tpermis.setText(receivedObra.getTip_p());
            prorroga.setText(receivedObra.getProrroga());
            dini.setText(receivedObra.getData_inici());
            dfin.setText(receivedObra.getData_final());





        }
    }
}