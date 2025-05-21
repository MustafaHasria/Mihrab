package com.izzedineeita.mihrab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.izzedineeita.mihrab.R;
import com.izzedineeita.mihrab.database.DataBaseHelper;
import com.izzedineeita.mihrab.model.AzkarModel;
import com.izzedineeita.mihrab.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;


public class AzkarAdapter extends RecyclerView.Adapter<AzkarAdapter.AdsViewHolder> {
    private static final int TYPE_HEADER = 0;
    private  OnRecycleViewItemClicked listener;
    private Activity activity;
    private ArrayList<AzkarModel> adsList;


    public AzkarAdapter(Activity activity, ArrayList<AzkarModel> adsList, OnRecycleViewItemClicked listener) {
        this.activity = activity;
        this.adsList = adsList;
        this.listener=listener;
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
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.azkar_card, parent, false);
        AdsViewHolder contactViewHolder = new AdsViewHolder(itemView, TYPE_HEADER);
        return contactViewHolder;

    }

    @Override
    public void onBindViewHolder(final AdsViewHolder holder, final int i) {
        holder.tv_azkar_text.setText(adsList.get(i).getTextAzakar());
        holder.iv_delete.setOnClickListener(view -> listener.onItemClicked(view, i));
        holder.iv_edit.setOnClickListener(view -> listener.onItemClick(view,i));

    }

    @Override
    public int getItemCount() {
        return adsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class AdsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout contact_card;
        int viewType;

        TextView tv_azkar_text;
        AppCompatImageView iv_delete,iv_edit;
        AdsViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            tv_azkar_text = itemView.findViewById(R.id.tv_azkar_text);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    public interface OnRecycleViewItemClicked{
         void onItemClicked(View view, int position);
         void onItemClick(View view, int position);
    }

}
