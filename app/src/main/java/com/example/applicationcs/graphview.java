package com.example.applicationcs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link graphview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class graphview extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    BarChart barChart;
    final String log = "Alert:";
    String output;
    String startdate,enddate;
    List<String> graphview_listID, graphview_listNAME,graphview_listDATE;
    List<Float> graphview_listTEMP;
    List<tableviewClass> graphlistALL;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public graphview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment graphview.
     */
    // TODO: Rename and change types and number of parameters
    public static graphview newInstance(String param1, String param2) {
        graphview fragment = new graphview();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View gview = inflater.inflate(R.layout.fragment_graphview, container, false);
        graphview_listID = new ArrayList<>();
        graphview_listNAME = new ArrayList<>();
        graphview_listDATE = new ArrayList<>();
        graphview_listTEMP = new ArrayList<>();
        barChart=gview.findViewById(R.id.barChart);
        graphlistALL = new ArrayList<>();
        // Inflate the layout for this fragment
        return gview;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_sort);
        item.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_dateasc:
            { graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.DateAscComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listNAME.add(graphlistALL.get(i).getEmpname());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listNAME.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);



                Toast.makeText(getContext(),"Date Asc",Toast.LENGTH_LONG).show();
                return true; }

            case R.id.menu_datedesc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.DateDescComparator);

                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listNAME.add(graphlistALL.get(i).getEmpname());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listNAME.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);
                Toast.makeText(getContext(),"Date Desc",Toast.LENGTH_LONG).show();
                return true;}

            case R.id.menu_nameasc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.EmpNameAscComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listNAME.add(graphlistALL.get(i).getEmpname());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listNAME.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);

                Toast.makeText(getContext(),"Name Asc",Toast.LENGTH_LONG).show();
                return true;}

            case R.id.menu_namedesc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.EmpNameDescComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listNAME.add(graphlistALL.get(i).getEmpname());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listNAME.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);

                Toast.makeText(getContext(),"Name Desc",Toast.LENGTH_LONG).show();
                return true;}

            case R.id.menu_idasc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.EmpIdAscComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listID.add(graphlistALL.get(i).getEmpid());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listID.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);

                Toast.makeText(getContext(),"ID Asc",Toast.LENGTH_LONG).show();
                return true;}

            case R.id.menu_iddesc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.EmpIdDescComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listID.add(graphlistALL.get(i).getEmpid());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listID.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);

                Toast.makeText(getContext(),"ID Desc",Toast.LENGTH_LONG).show();
                return true;}

            case R.id.menu_tempasc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.TempAscComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listNAME.add(graphlistALL.get(i).getEmpname());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listNAME.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);

                Toast.makeText(getContext(),"Temp Asc",Toast.LENGTH_LONG).show();
                return true;}

            case R.id.menu_tempdesc:
            {   graphview_listNAME.clear();
                graphview_listTEMP.clear();
                graphview_listDATE.clear();
                graphview_listID.clear();
                Collections.sort(graphlistALL, tableviewClass.TempDescComparator);
                int i;
                for (i=0; i<graphlistALL.size(); i++){
                    graphview_listNAME.add(graphlistALL.get(i).getEmpname());
                    graphview_listTEMP.add(Float.parseFloat(graphlistALL.get(i).getTemperature()));
                    graphview_listDATE.add(graphlistALL.get(i).getdate());
                }

                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<String> xAxisLabel2 = new ArrayList<>();
                ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
                for( i=0; i < graphview_listTEMP.size(); i++) {
                    BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
                    yVals.add(entry);
                    xAxisLabel.add(graphview_listNAME.get(i));
                    xAxisLabel2.add(graphview_listDATE.get(i));

                }
                // Set the value formatter

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        output=String.valueOf(xAxisLabel.get((int) value))+" : "+String.valueOf(xAxisLabel2.get((int) value));
                        Log.e(log,output);
                        return output;
                    }

                });
                xAxis.setGranularityEnabled(true);

                MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
                newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green),
                        ContextCompat.getColor(getContext(), R.color.bleu),
                        ContextCompat.getColor(getContext(), R.color.red)});
                newSet.setValueTextColor(Color.BLACK);
                newSet.setValueTextSize(16f);


                BarData data = new BarData(newSet);
                barChart.setData(data);

                barChart.getDescription().setText("Records");
                barChart.animateY(2000);
                Toast.makeText(getContext(),"Temp Desc",Toast.LENGTH_LONG).show();
                return true;}
        }
        return super.onOptionsItemSelected(item);
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
                        graphview_listID.add(tableviewclass.getEmpid());
                        graphview_listNAME.add(tableviewclass.getEmpname());
                        graphview_listTEMP.add(Float.parseFloat(tableviewclass.getTemperature()));
                        graphview_listDATE.add(tableviewclass.getdate());

                        graphlistALL.add(new tableviewClass(tableviewclass.getdate(),tableviewclass.getEmpid(),tableviewclass.getEmpname(),tableviewclass.getTemperature()));

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

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        final ArrayList<String> xAxisLabel2 = new ArrayList<>();
        getList();
        for(int i=0; i < graphview_listTEMP.size(); i++) {
            BarEntry entry = new BarEntry(i,graphview_listTEMP.get(i));
            Log.e(log,graphview_listDATE.get(i));
            yVals.add(entry);
            xAxisLabel.add(graphview_listNAME.get(i));
            xAxisLabel2.add(graphview_listDATE.get(i));

        }
        // Set the value formatter

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                output=xAxisLabel.get((int) value)+" : "+xAxisLabel2.get((int) value);
                //return xAxisLabel.get((int) value);
                return output;
            }

        });
        xAxis.setGranularityEnabled(true);

        MyBarDataSet newSet = new MyBarDataSet(yVals, "DataSet");
        newSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green2),
                ContextCompat.getColor(getContext(), R.color.bleu2),
                ContextCompat.getColor(getContext(), R.color.red2)});
        newSet.setValueTextColor(Color.BLACK);
        newSet.setValueTextSize(16f);


        BarData data = new BarData(newSet);
        barChart.setData(data);
        barChart.getDescription().setText("Records");
        barChart.animateY(2000);

        {graphlistALL.clear();
            graphview_listTEMP.clear();
            graphview_listNAME.clear();
            graphview_listID.clear();
            graphview_listDATE.clear();
            barChart.refreshDrawableState();}
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    }