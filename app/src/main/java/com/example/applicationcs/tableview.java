package com.example.applicationcs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tableview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tableview extends Fragment implements View.OnTouchListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private tableviewListener listener;
    String startdate,enddate;
    List<tableviewClass> tableview_list;
    Menu menu;
    FloatingActionButton fab;
    ViewGroup _root;
    private int _xDelta;
    private int _yDelta;
    GestureDetector gestureDetector;

    // TODO: Rename and change types of parameters
    SwipeRefreshLayout refreshLayout;
    ProgressBar progressBar;
    RecyclerView recycler_view;
    tableviewAdapter adapter;
    final String log = "Alert:";

    public tableview() {
        // Required empty public constructor
    }

    public interface  tableviewListener{
        void onInputtableviewSent(CharSequence input);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tableview.
     */
    // TODO: Rename and change types and number of parameters
    public static tableview newInstance(String param1, String param2) {
        tableview fragment = new tableview();
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
                             Bundle savedInstanceState){

        View layoutview = inflater.inflate(R.layout.fragment_tableview, container, false);
        tableview_list = new ArrayList<>();
        refreshLayout=layoutview.findViewById(R.id.refreshLayout);
        _root = (ViewGroup)layoutview.findViewById(R.id.linearLayout2);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(150, 150);
        layoutParams.leftMargin = 900;
        layoutParams.topMargin = 1200;
        layoutParams.bottomMargin = 10;
        layoutParams.rightMargin = 50;

        fab = (FloatingActionButton) layoutview.findViewById(R.id.fab);
        fab.show();
        fab.setLayoutParams(layoutParams);

        gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                //generate data
                StringBuilder data = new StringBuilder();
                data.append("DATE,EMP_ID,E_NAME,TEMP °F");
                for(int i = 0; i<tableview_list.size(); i++){
                    data.append("\n"+tableview_list.get(i).getdate()+","
                            +tableview_list.get(i).getEmpid()+","
                            +tableview_list.get(i).getEmpname()+","
                            +tableview_list.get(i).getTemperature());
                }

                try{
                    //saving the file into device
                    FileOutputStream out = getContext().openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    Context context = getContext();
                    File filelocation = new File(getContext().getFilesDir(), "data.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.applicationcs.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });

        fab.setOnTouchListener(this);

        // Inflate the layout for this fragment
        return layoutview;
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        if (gestureDetector.onTouchEvent(event)) {
            // single tap
            return true;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        _root.invalidate();
        return true;
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
                Collections.sort(tableview_list, tableviewClass.DateAscComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"Date Asc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_datedesc:
                Collections.sort(tableview_list, tableviewClass.DateDescComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"Date Desc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_nameasc:
                Collections.sort(tableview_list, tableviewClass.EmpNameAscComparator);
                if(adapter!=null){
                    adapter.notifyDataSetChanged();}
                Toast.makeText(getContext(),"Name Asc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_namedesc:
                Collections.sort(tableview_list, tableviewClass.EmpNameDescComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"Name Desc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_idasc:
                Collections.sort(tableview_list, tableviewClass.EmpIdAscComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"ID Asc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_iddesc:
                Collections.sort(tableview_list, tableviewClass.EmpIdDescComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"ID Desc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_tempasc:
                Collections.sort(tableview_list, tableviewClass.TempAscComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"Temp Asc",Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_tempdesc:
                Collections.sort(tableview_list, tableviewClass.TempDescComparator);
                if (adapter!=null) {
                    adapter.notifyDataSetChanged(); }
                Toast.makeText(getContext(),"Temp Desc",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void setRecyclerView() throws ParseException {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new tableviewAdapter(getContext(),getList());
        recycler_view.setAdapter(adapter);

    }

    private List<tableviewClass> getList() throws ParseException {
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
                    //tableview_list.clear();
                    for (DataSnapshot empDatasnap : snapshot.getChildren()){
                        tableviewClass tableviewclass = empDatasnap.getValue(tableviewClass.class);
                        tableview_list.add(new tableviewClass(tableviewclass.getdate(),tableviewclass.getEmpid(),tableviewclass.getEmpname(),tableviewclass.getTemperature()));
                    }

                    adapter = new tableviewAdapter(getContext(),tableview_list);
                    recycler_view.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        return tableview_list;
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


    private void sendMessageToActivity() {
        /*EditText etMessage = (EditText) getView().findViewById(R.id.editText);
        Events.FragmentActivityMessage fragmentActivityMessageEvent =
                new Events.FragmentActivityMessage(String.valueOf(etMessage.getText()));

        GlobalBus.getBus().post(fragmentActivityMessageEvent);*/
    }

    @Subscribe
    public void getMessage(Events.ActivityFragmentMessage activityFragmentMessage) throws ParseException {
        Log.e(log, "called");

        String both_together=activityFragmentMessage.getMessage();
        startdate=both_together.substring(0,10);
        enddate=both_together.substring(10);
        Toast.makeText(getActivity(), "Starting:" +startdate+" Ending:"+enddate, Toast.LENGTH_SHORT).show();

        TableLayout tableLayout = getView().findViewById(R.id.tblLayoutContent2);
        tableLayout.setVisibility(View.VISIBLE);
        tableview_list.clear();
        progressBar = getView().findViewById(R.id.progress_bar);
        recycler_view = getView().findViewById(R.id.recycler_view2);
        setRecyclerView();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tableview_list.clear();
            recycler_view = getView().findViewById(R.id.recycler_view2);
                try {
                    setRecyclerView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

}