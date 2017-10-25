package com.example.rananoyon.rickshawfairsylhet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Md Rana Mahmud on 10/25/2017.
 */

public class FairAdapter extends RecyclerView.Adapter<FairViewHolder> {

    List<Fair> list = Collections.emptyList();
    Context context;

    public FairAdapter(List<Fair> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public FairViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fair_item,parent,false);
        FairViewHolder holder = new FairViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FairViewHolder holder, int position) {
        holder.slView.setText(list.get(position).getNumber());
        holder.descView.setText(list.get(position).getLocation());
        holder.fairView.setText(list.get(position).getFair());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
