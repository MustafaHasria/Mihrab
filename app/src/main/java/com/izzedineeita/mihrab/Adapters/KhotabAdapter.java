package com.izzedineeita.mihrab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.Khotab;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class KhotabAdapter extends RecyclerView.Adapter<KhotabAdapter.KhotabViewHolder> {
    private static final int TYPE_HEADER = 0;
    private final OnRecycleViewItemClicked listener;
    private final ArrayList<Khotab> khotabList;


    public KhotabAdapter(Activity activity, ArrayList<Khotab> khotabList, OnRecycleViewItemClicked listener) {
        this.khotabList = khotabList;
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
    public KhotabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_row, parent, false);
        KhotabViewHolder contactViewHolder = new KhotabViewHolder(itemView, TYPE_HEADER);
        return contactViewHolder;

    }

    @Override
    public void onBindViewHolder(final KhotabViewHolder holder, final int i) {
        holder.tv_adsText.setText(khotabList.get(i).getTitle());
        holder.iv_delete.setOnClickListener(view -> listener.onItemClicked(view, i));
        holder.iv_edit.setOnClickListener(view -> listener.onItemClick(view, i));
        holder.tv_adsPeriod.setText(khotabList.get(i).getDateKhotab());
        holder.iv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onView(view, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return khotabList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class KhotabViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contact_card;
        int viewType;
        TextView tv_adsText;
        TextView tv_adsPeriod;
        TextView tv_adsDays;
        ImageView iv_delete, iv_edit, iv_view;

        KhotabViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            tv_adsText = (TextView) itemView.findViewById(R.id.tv_adsText);
            tv_adsPeriod = (TextView) itemView.findViewById(R.id.tv_adsPeriod);
            tv_adsDays = (TextView) itemView.findViewById(R.id.tv_adsDays);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            iv_view = (ImageView) itemView.findViewById(R.id.iv_view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    public interface OnRecycleViewItemClicked {
        void onItemClicked(View view, int position);

        void onItemClick(View view, int position);

        void onView(View view, int position);
    }

}
