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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HelperAdapter extends RecyclerView.Adapter<HelperAdapter.ViewHolder> implements Filterable {

    Context context;
    List<HelperClass> helper_list;
    List<HelperClass> helper_listAll;
    private Activity activity;

    public HelperAdapter(Context context, List<HelperClass> helper_list) {
        this.context = context;
        this.helper_list = helper_list;
        this.helper_listAll = new ArrayList<>(helper_list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (helper_list != null && helper_list.size()>0){
            HelperClass helper = helper_list.get(position);
            holder.id_tv.setText(helper.getEmpid());
            holder.name_tv.setText(helper.getEmpname());
            holder.temp_tv.setText(helper.getTemperature());

            int temp1=Integer.parseInt(helper.getTemperature());
            if (temp1>=99){
                holder.id_tv.setBackgroundResource(R.color.red);
                holder.name_tv.setBackgroundResource(R.color.red);
                holder.temp_tv.setBackgroundResource(R.color.red);

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
        return helper_list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter= new Filter() {

        //runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<HelperClass> filteredlist = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filteredlist.addAll(helper_listAll);
            } else {
                for (HelperClass helper: helper_listAll){
                    if (helper.getEmpname().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredlist.add(helper);
                    }
                    if (helper.getEmpid().contains(charSequence.toString()) ){
                        filteredlist.add(helper);
                    }

                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }

        //runs on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          helper_list.clear();
          helper_list.addAll((Collection<? extends HelperClass>) results.values);
          notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_tv,name_tv,temp_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_tv = itemView.findViewById(R.id.id_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            temp_tv = itemView.findViewById(R.id.temp_tv);
        }
    }
}
