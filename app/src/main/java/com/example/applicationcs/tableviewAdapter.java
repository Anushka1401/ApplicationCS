package com.example.applicationcs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class tableviewAdapter extends RecyclerView.Adapter<tableviewAdapter.ViewHolder> {

    Context context;
    List<tableviewClass> tableview_list;
    List<tableviewClass> tableview_listAll;
    private Activity activity;

    public tableviewAdapter(Context context, List<tableviewClass> tableview_list) {
        this.context = context;
        this.tableview_list = tableview_list;
        this.tableview_listAll = new ArrayList<>(tableview_list);
    }

    @NonNull
    @Override
    public tableviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tableview_itemlayout,parent,false);
        return new tableviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tableviewAdapter.ViewHolder holder, int position) {
        if (tableview_list != null && tableview_list.size()>0){
            tableviewClass helper = tableview_list.get(position);
            holder.date_tv.setText(helper.getdate());
            holder.id_tv.setText(helper.getEmpid());
            holder.name_tv.setText(helper.getEmpname());
            holder.temp_tv.setText(helper.getTemperature());

            int temp1=Integer.parseInt(helper.getTemperature());
            if (temp1>=99){
                holder.date_tv.setBackgroundResource(R.color.red);
                holder.id_tv.setBackgroundResource(R.color.red);
                holder.name_tv.setBackgroundResource(R.color.red);
                holder.temp_tv.setBackgroundResource(R.color.red);

                holder.date_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.id_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.name_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.temp_tv.setTextColor(context.getResources().getColor(R.color.white));
            }

        } else{
            return;
        }

    }

    @Override
    public int getItemCount() {
        return tableview_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_tv,id_tv,name_tv,temp_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_tv = itemView.findViewById(R.id.date2_tv);
            id_tv = itemView.findViewById(R.id.id2_tv);
            name_tv = itemView.findViewById(R.id.name2_tv);
            temp_tv = itemView.findViewById(R.id.temp2_tv);
        }
    }
}
