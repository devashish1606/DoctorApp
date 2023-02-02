package com.example.doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PreviousPriscriptionAdapter extends RecyclerView.Adapter<PreviousPriscriptionAdapter.ViewHolder>{

    Context context;
    ArrayList<PreviousPrescriptionModel> prescriptionModelArrayList;


    public PreviousPriscriptionAdapter(Context context, ArrayList<PreviousPrescriptionModel> prescriptionModelArrayList) {
        this.context = context;
        this.prescriptionModelArrayList = prescriptionModelArrayList;
    }

    @NonNull
    @Override
    public PreviousPriscriptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_prescription_items, parent, false);

        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull PreviousPriscriptionAdapter.ViewHolder holder, int position) {
        PreviousPrescriptionModel p = prescriptionModelArrayList.get(position);
//        holder.suggestedBy.setText(prescriptionModelArrayList.get(position).getSuggested_by());
        Picasso.get().load(prescriptionModelArrayList.get(position).getImage_url()).into(holder.itemImagePrescription);
        holder.suggestedBy.setText("Suggested By:- "+p.getSuggested_by());
//        holder.timestampItem.setText(p.getTimestamp().toString())
        String url = p.getImage_url();

        holder.itemImagePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PresShowActivity.class);
                intent.putExtra("img",url);
                intent.putExtra("SuggestedBy",p.getSuggested_by());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return prescriptionModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImagePrescription;
        private TextView suggestedBy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImagePrescription = itemView.findViewById(R.id.itemImageGrid);
            suggestedBy = itemView.findViewById(R.id.suggestedByItem);
//            timestampItem = itemView.findViewById(R.id.timestampItem);




        }
    }

}
