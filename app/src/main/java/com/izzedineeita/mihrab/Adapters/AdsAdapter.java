package com.izzedineeita.mihrab.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Ads;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdsViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    private final OnRecycleViewItemClicked listener;
    Activity activity;
    ArrayList<Ads> adsList;


    public AdsAdapter(Activity activity, ArrayList<Ads> adsList, OnRecycleViewItemClicked listener) {
        this.activity = activity;
        this.adsList = adsList;
        this.listener = listener;
        SharedPreferences sp = activity.getSharedPreferences(Utils.PREFS, Context.MODE_PRIVATE);
        DataBaseHelper DBO = new DataBaseHelper(activity);
        try {
            DBO.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AdsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_row, parent, false);
        AdsViewHolder contactViewHolder = new AdsViewHolder(itemView, TYPE_HEADER);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdsViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        Ads ads = adsList.get(i);
        String days = "";
        holder.tv_adsDays.setText(days);
        holder.tv_adsText.setText(ads.getTitle());
        holder.tv_adsPeriod.setText("من " + ads.getStartDate() + " إلى " + ads.getEndDate());
        holder.iv_delete.setOnClickListener(view -> listener.onItemClicked(view, i));
        holder.iv_edit.setOnClickListener(view -> listener.onItemClick(view, i));
        holder.iv_view.setOnClickListener(view -> listener.onView(view, i));

    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contact_card;
        int viewType;
        TextView tv_adsText;
        TextView tv_adsPeriod;
        TextView tv_adsDays;
        ImageView iv_delete, iv_edit, iv_view;

        public AdsViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            tv_adsText = itemView.findViewById(R.id.tv_adsText);
            tv_adsPeriod = itemView.findViewById(R.id.tv_adsPeriod);
            tv_adsDays = itemView.findViewById(R.id.tv_adsDays);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_view = itemView.findViewById(R.id.iv_view);


        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    /**
     * Updates the adapter's data list and notifies of the change.
     *
     * @param newAdsList The new list of advertisements
     */
    public void updateData(ArrayList<Ads> newAdsList) {
        if (newAdsList != null) {
            this.adsList.clear();
            this.adsList.addAll(newAdsList);
            notifyDataSetChanged();
        }
    }

    /**
     * Clears all data from the adapter.
     */
    public void clearData() {
        this.adsList.clear();
        notifyDataSetChanged();
    }

    public interface OnRecycleViewItemClicked {
        void onItemClicked(View view, int position);

        void onItemClick(View view, int position);

        void onView(View view, int position);
    }

}
