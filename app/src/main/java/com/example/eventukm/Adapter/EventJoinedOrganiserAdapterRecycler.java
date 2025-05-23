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

public class EventJoinedOrganiserAdapterRecycler extends RecyclerView.Adapter<EventJoinedOrganiserAdapterRecycler.ViewHolder> {

    private Context context;
    private ArrayList<UserEvent> participantList;
    private OnItemClickListener onItemClickListener;

    public EventJoinedOrganiserAdapterRecycler(Context context, ArrayList<UserEvent> participantList) {
        this.context = context;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserEvent userEvent = participantList.get(position);
        holder.name.setText(userEvent.getName());
        holder.email.setText(userEvent.getEmail());
        holder.phone.setText(userEvent.getPhone());
        holder.matrix.setText(userEvent.getMatrix());
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, email, phone, matrix;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.participant_name);
            email = itemView.findViewById(R.id.participant_email);
            phone = itemView.findViewById(R.id.participant_phone);
            matrix = itemView.findViewById(R.id.participant_matrix);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(participantList.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(UserEvent userEvent);
    }
}
