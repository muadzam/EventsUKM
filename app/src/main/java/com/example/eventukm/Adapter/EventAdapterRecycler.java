package com.example.eventukm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapterRecycler extends RecyclerView.Adapter<EventAdapterRecycler.EventViewHolder> {
    private Context context;
    private ArrayList<Events> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public EventAdapterRecycler(Context context, ArrayList<Events> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.programentry, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Events events = list.get(position);

        String imageURL = events.getImageURL();
        Picasso.get().load(imageURL).into(holder.programimage);
        holder.programtype.setText(events.getType());
        holder.programname.setText(events.getName());
        holder.programfee.setText(events.getFee());
        holder.programdate.setText(events.getDate());
        holder.programmerit.setText(events.getMerit());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilteredList(ArrayList<Events> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView programtype, programname, programfee, programdate, programmerit;
        ImageView programimage;
        Button btninfo;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            programtype = itemView.findViewById(R.id.tv_typeprogram);
            programname = itemView.findViewById(R.id.tv_programname_details);
            programfee = itemView.findViewById(R.id.tv_feeprogram);
            programimage = itemView.findViewById(R.id.img_programimage);
            programdate = itemView.findViewById(R.id.tv_dateprogram);
            programmerit = itemView.findViewById(R.id.tv_meritprogram);
            btninfo = itemView.findViewById(R.id.btn_info);

            btninfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public Events getEventAt(int position) {
        return list.get(position);
    }
}
