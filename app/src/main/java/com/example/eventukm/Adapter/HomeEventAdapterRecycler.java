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

public class HomeEventAdapterRecycler extends RecyclerView.Adapter<HomeEventAdapterRecycler.EventViewHolder> {
    private Context context;
    private ArrayList<Events> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HomeEventAdapterRecycler(Context context, ArrayList<Events> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.homeprogramentry, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Events events = list.get(position);

        String imageURL = events.getImageURL();
        Picasso.get().load(imageURL).into(holder.programimage);
        holder.programname.setText(events.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView programname;
        ImageView programimage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            programname = itemView.findViewById(R.id.tv_programname_details);
            programimage = itemView.findViewById(R.id.img_programimage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }
    }
}
