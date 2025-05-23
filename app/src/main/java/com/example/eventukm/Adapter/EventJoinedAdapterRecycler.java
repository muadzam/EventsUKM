package com.example.eventukm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.R;

import java.util.ArrayList;

public class EventJoinedAdapterRecycler extends RecyclerView.Adapter<EventJoinedAdapterRecycler.EventViewHolder> {
    private Context context;
    private ArrayList<UserEvent> list;
    private OnItemClickListener onItemClickListener;

    public EventJoinedAdapterRecycler(Context context, ArrayList<UserEvent> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(UserEvent userEvent);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.joinprogramentry, parent, false);
        return new EventViewHolder(v, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        UserEvent userEvent = list.get(position);
        holder.programname.setText(userEvent.getEventName());
        holder.programfee.setText(userEvent.getEventFee());
        holder.programdate.setText(userEvent.getEventDate());
        holder.programmerit.setText(userEvent.getEventMerit());
        holder.programtime.setText(userEvent.getEventTime());
        holder.programlocation.setText(userEvent.getEventLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView programtime, programname, programfee, programdate, programmerit, programlocation;

        public EventViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            programtime = itemView.findViewById(R.id.tv_programtime);
            programname = itemView.findViewById(R.id.tv_programname_details);
            programfee = itemView.findViewById(R.id.tv_feeprogram);
            programdate = itemView.findViewById(R.id.tv_dateprogram);
            programmerit = itemView.findViewById(R.id.tv_meritprogram);
            programlocation = itemView.findViewById(R.id.tv_locationprogram);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(list.get(position));
                }
            });
        }
    }
}
