package com.example.rananoyon.rickshawfairsylhet;

/**
 * Created by Md Rana Mahmud on 10/25/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class FairViewHolder extends RecyclerView.ViewHolder {
    public TextView slView;
    public TextView descView;
    public TextView fairView;
    public FairViewHolder(View itemView) {
        super(itemView);
        slView = itemView.findViewById(R.id.slView);

        descView = itemView.findViewById(R.id.descView);

        fairView = itemView.findViewById(R.id.fairView);
    }
}

