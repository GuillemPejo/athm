package me.guillem.athm2app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import me.guillem.athm2app.Model.Obra;
import me.guillem.athm2app.Model.RecyclerAdapterCardsHome;
import me.guillem.athm2app.Model.RecyclerAdapterCardsVisits;
import me.guillem.athm2app.Model.Visita;
import me.guillem.athm2app.R;
import me.guillem.athm2app.Utils.FirebaseCRUD;
import me.guillem.athm2app.Utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitsFragment extends Fragment {
    private Obra receivedObra;
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    RecyclerAdapterCardsVisits adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseCRUD recurs_crud = new FirebaseCRUD();

    public VisitsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisitsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitsFragment newInstance(String param1, String param2) {
        VisitsFragment fragment = new VisitsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receivedObra = null;
        receivedObra = Utils.receiveObra(getActivity().getIntent(), getContext());


        if (receivedObra != null) {
            String obra_key = receivedObra.getKey();

            rv = view.findViewById(R.id.rv_visites);
            layoutManager = new LinearLayoutManager(getContext());
            rv.setLayoutManager(layoutManager);
            adapter= new RecyclerAdapterCardsVisits(getContext() ,Utils.DataCacheVisits);
            rv.setAdapter(adapter);
            recurs_crud.selectVisit(getActivity(),Utils.getDatabaseRefence(),receivedObra.getKey(),rv,adapter);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visits, container, false);
    }



}