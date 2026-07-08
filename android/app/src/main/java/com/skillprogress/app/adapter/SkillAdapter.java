package com.skillprogress.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skillprogress.app.DetailSkillActivity;
import com.skillprogress.app.R;
import com.skillprogress.app.model.Skill;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private Context context;
    private List<Skill> skillList;

    public SkillAdapter(Context context, List<Skill> skillList) {
        this.context = context;
        this.skillList = skillList;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        Skill skill = skillList.get(position);
        holder.tvNamaSkill.setText(skill.getNamaSkill());
        holder.tvMateriHariIni.setText(skill.getMateriHariIni() == null || skill.getMateriHariIni().isEmpty()
                ? "Belum ada materi hari ini" : skill.getMateriHariIni());
        holder.tvStatusPersen.setText(skill.getStatusPaham() + "%");

        holder.itemView.post(() -> {
            int maxWidth = ((View) holder.progressFill.getParent()).getWidth();
            ViewGroup.LayoutParams params = holder.progressFill.getLayoutParams();
            params.width = (int) (maxWidth * (skill.getStatusPaham() / 100f));
            holder.progressFill.setLayoutParams(params);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailSkillActivity.class);
            intent.putExtra("skill_id", skill.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaSkill, tvMateriHariIni, tvStatusPersen;
        View progressFill;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaSkill = itemView.findViewById(R.id.tvNamaSkill);
            tvMateriHariIni = itemView.findViewById(R.id.tvMateriHariIni);
            tvStatusPersen = itemView.findViewById(R.id.tvStatusPersen);
            progressFill = itemView.findViewById(R.id.progressFill);
        }
    }
}
