package com.example.eventukm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrganiserEventManagementAdapterRecycler extends RecyclerView.Adapter<OrganiserEventManagementAdapterRecycler.EventViewHolder> {
    private Context context;
    private ArrayList<Events> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Events events);
    }

    public OrganiserEventManagementAdapterRecycler(Context context, ArrayList<Events> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.organiserprogramentry, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Events events = list.get(position);

        String imageURL = events.getImageURL();
        Picasso.get().load(imageURL).into(holder.programimage);
        holder.programname.setText(events.getName());
        holder.programfee.setText(events.getFee());
        holder.programdate.setText(events.getDate());
        holder.programmerit.setText(events.getMerit());
        holder.programtime.setText(events.getTime());
        holder.programlocation.setText(events.getLocation());
        holder.programdescription.setText(events.getDescription());
        holder.programtype.setText(events.getType());

        holder.itemView.setOnClickListener(view -> listener.onItemClick(events));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView programtime, programname, programfee, programdate, programmerit, programlocation, programdescription, programtype;

        ImageView programimage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            programtime = itemView.findViewById(R.id.tv_programtime);
            programname = itemView.findViewById(R.id.tv_programname_details);
            programfee = itemView.findViewById(R.id.tv_feeprogram);
            programdate = itemView.findViewById(R.id.tv_dateprogram);
            programmerit = itemView.findViewById(R.id.tv_programmerit);
            programlocation = itemView.findViewById(R.id.tv_programlocation);
            programdescription = itemView.findViewById(R.id.tv_programdescription);
            programtype = itemView.findViewById(R.id.tv_programtype);
            programimage = itemView.findViewById(R.id.imagePosterProgram);
        }
    }
}
