package com.example.cerdastb.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cerdastb.Interface.ItemClickListener;
import com.example.cerdastb.R;

public class ListMateriHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView TxtJudul;
    public LinearLayout BackColor;

    private ItemClickListener itemClickListener;


    public ListMateriHolder(@NonNull View itemView) {
        super(itemView);

        TxtJudul = itemView.findViewById(R.id.Judul_Cview);
        BackColor = itemView.findViewById(R.id.backColor);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false );
    }
}
