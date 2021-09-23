package com.example.applicationcs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.Distribution;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class Retrievetotable extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recycler_view;
    HelperAdapter adapter;
    final String log = "Alert:";
    TextView foruse;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrievetotable);
        foruse=findViewById(R.id.foruse);
        String datedetail = getIntent().getStringExtra("datedetail");
        foruse.setText(datedetail);

        progressBar=findViewById(R.id.progress_bar);
        recycler_view = findViewById(R.id.recycler_view);
        setRecyclerView();

        refreshLayout=findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                foruse.setText(datedetail);

                recycler_view = findViewById(R.id.recycler_view);
                setRecyclerView();

                refreshLayout.setRefreshing(false);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private  void setRecyclerView(){
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelperAdapter(this,getList());
        recycler_view.setAdapter(adapter);

    }

    private List<HelperClass> getList(){
        List<HelperClass> helper_list = new ArrayList<>();
        String datedetail = foruse.getText().toString();
        DatabaseReference employeeDbRef = FirebaseDatabase.getInstance().getReference(datedetail);
        employeeDbRef.keepSynced(true);

        employeeDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                helper_list.clear();
                for (DataSnapshot empDatasnap : snapshot.getChildren()){
                    HelperClass helperclass = empDatasnap.getValue(HelperClass.class);
                    helper_list.add(new HelperClass(helperclass.getEmpid(),helperclass.getEmpname(),helperclass.getTemperature()));
                }

                adapter = new HelperAdapter(Retrievetotable.this,helper_list);
                recycler_view.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return helper_list;
    }
}