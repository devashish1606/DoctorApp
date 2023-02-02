package com.example.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class PrevAppAdapter extends RecyclerView.Adapter<PrevAppAdapter.MyViewHolder> {

    Context context;
    ArrayList<person> list = new ArrayList<>();

    public PrevAppAdapter(Context context, ArrayList<person> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.prev_app_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        person p = list.get(position);
        holder.name.setText(p.getName());
        holder.mobile.setText(p.getMobile());
        holder.date.setText(p.getApp_data());
        holder.describeProblem.setText(p.getDescibeproblem());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,mobile,date,describeProblem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.firstname);
            mobile = itemView.findViewById(R.id.mobile);
            date = itemView.findViewById(R.id.appointdate);
            describeProblem = itemView.findViewById(R.id.describeProblem);


        }
    }
}
