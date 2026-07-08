package com.skillprogress.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skillprogress.app.R;
import com.skillprogress.app.model.RiwayatMateri;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder> {

    private Context context;
    private List<RiwayatMateri> riwayatList;

    public RiwayatAdapter(Context context, List<RiwayatMateri> riwayatList) {
        this.context = context;
        this.riwayatList = riwayatList;
    }

    @NonNull
    @Override
    public RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_riwayat, parent, false);
        return new RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatViewHolder holder, int position) {
        RiwayatMateri item = riwayatList.get(position);
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvMateri.setText(item.getMateri());
        holder.tvPersen.setText(item.getPersentase() + "%");
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public static class RiwayatViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvMateri, tvPersen;

        public RiwayatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvMateri = itemView.findViewById(R.id.tvMateri);
            tvPersen = itemView.findViewById(R.id.tvPersen);
        }
    }
}
