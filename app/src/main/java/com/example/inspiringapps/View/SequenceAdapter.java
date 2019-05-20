package com.example.inspiringapps.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.inspiringapps.Model.Sequence;
import com.example.inspiringapps.R;
import java.util.ArrayList;


public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.ViewHolder> {

    ArrayList<Sequence> sequences;

    public SequenceAdapter(ArrayList<Sequence> sequences) {
        this.sequences = sequences;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String sequence = sequences.get(position).getSequence();
        String occurance = sequences.get(position).getOccurance();

        holder.sequence.setText(sequence);
        holder.occurance.setText(occurance);

    }


    @Override
    public int getItemCount() {
        if(sequences !=null) {
            return sequences.size();
        }else{
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sequence;
        TextView occurance;

        public ViewHolder(View itemView) {
            super(itemView);
            sequence =itemView.findViewById(R.id.sequence);
            occurance =itemView.findViewById(R.id.occurance);
        }
    }
}
