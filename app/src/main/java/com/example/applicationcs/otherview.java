package com.example.applicationcs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link otherview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class otherview extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String log = "Alert:";
    String startdate,enddate;
    int count1=0, count2=0, count3=0;
    List<String> pie_listID, pie_listNAME,pie_listDATE;
    List<Float> pie_listTEMP;
    List<tableviewClass> pielistALL;
    PieChart pieChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public otherview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pieview.
     */
    // TODO: Rename and change types and number of parameters
    public static otherview newInstance(String param1, String param2) {
        otherview fragment = new otherview();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View pview = inflater.inflate(R.layout.fragment_otherview, container, false);
        pie_listID = new ArrayList<>();
        pie_listNAME = new ArrayList<>();
        pie_listDATE = new ArrayList<>();
        pie_listTEMP = new ArrayList<>();
        pieChart=pview.findViewById(R.id.piechart);
        pielistALL = new ArrayList<>();
        return pview;
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setClickListener(view);
    }

    public void setClickListener(final View view) {
        /*Button btnSubmit = (Button) view.findViewById(R.id.submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                sendMessageToActivity();
            }
        });*/
    }

    private void getList() throws ParseException {
        //List<tableviewClass> tableview_list = new ArrayList<>();
        DatabaseReference employeeDbRef;
        String pathdate;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date start = sdf.parse(startdate);
        Date end = sdf.parse(enddate);
        Calendar cStart = Calendar.getInstance(); cStart.setTime(start);
        Calendar cEnd = Calendar.getInstance(); cEnd.setTime(end);


        cEnd.add(Calendar.DAY_OF_MONTH,1);
        while (cStart.before(cEnd)) {
            pathdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(cStart.getTime());

            //add one day to date
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            //Log.e(log,pathdate);

            employeeDbRef = FirebaseDatabase.getInstance().getReference(pathdate);
            employeeDbRef.keepSynced(true);
            employeeDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot empDatasnap : snapshot.getChildren()){
                        tableviewClass tableviewclass = empDatasnap.getValue(tableviewClass.class);
                        pie_listID.add(tableviewclass.getEmpid());
                        pie_listNAME.add(tableviewclass.getEmpname());
                        pie_listTEMP.add(Float.parseFloat(tableviewclass.getTemperature()));
                        pie_listDATE.add(tableviewclass.getdate());

                        pielistALL.add(new tableviewClass(tableviewclass.getdate(),tableviewclass.getEmpid(),tableviewclass.getEmpname(),tableviewclass.getTemperature()));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        //return graphview_list;
    }

    @Subscribe
    public void getMessage(Events.ActivityFragmentMessage activityFragmentMessage) throws ParseException {
        Log.e(log, "called now");

        String both_together=activityFragmentMessage.getMessage();
        startdate=both_together.substring(0,10);
        enddate=both_together.substring(10);
        Toast.makeText(getActivity(), "Starting:" +startdate+" Ending:"+enddate, Toast.LENGTH_SHORT).show();

        getList();
        ArrayList<PieEntry> emps = new ArrayList<>();
        for (int i=0; i<pielistALL.size(); i++){
            if (Float.parseFloat(pielistALL.get(i).getTemperature())>99){
                count1++;
            }
            if (Float.parseFloat(pielistALL.get(i).getTemperature())==99){
                count2++;
            }
            if (Float.parseFloat(pielistALL.get(i).getTemperature())<99){
                count3++;
            }

        }
        emps.add(new PieEntry(count1, "Fever \nAlert\n(above 99)"));
        emps.add(new PieEntry(count2, "99°F"));
        emps.add(new PieEntry(count3, "Normal \nRange\n(below 99)"));

        PieDataSet pieDataSet = new PieDataSet(emps,"Employees");
        pieDataSet.setColors(Color.rgb(237,101,91), Color.rgb(96,136,168), Color.rgb(139,195,74));
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setText("Overview of Temperature readings in the selected range");
        pieChart.setCenterText("Employees' Temperatures");
        pieChart.animate();

        pielistALL.clear();
        pie_listTEMP.clear();
        pie_listNAME.clear();
        pie_listID.clear();
        pie_listDATE.clear();
        count1=0;
        count2=0;
        count3=0;
        pieChart.refreshDrawableState();

    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

}