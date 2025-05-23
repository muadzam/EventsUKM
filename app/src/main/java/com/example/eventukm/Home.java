package com.example.eventukm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.Adapter.Events;
import com.example.eventukm.Adapter.HomeEventAdapterRecycler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    ImageView btn_profile, img_c1, img_c2, btn_search, btn_signout, btn_ticket;
    private DatabaseReference databaseReference;
    private HomeEventAdapterRecycler adapterRecycler;
    private RecyclerView recyclerView;
    private ArrayList<Events> list;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.program_recycler);  // Ensure the ID matches
        btn_profile = findViewById(R.id.btn_profile);
        btn_search = findViewById(R.id.btn_search);
        btn_signout = findViewById(R.id.btn_signout);
        btn_ticket = findViewById(R.id.btn_ticket);
        img_c1 = findViewById(R.id.img_category1);
        img_c2 = findViewById(R.id.img_category2);

        databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Events");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterRecycler = new HomeEventAdapterRecycler(this, list);
        recyclerView.setAdapter(adapterRecycler);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Events events = dataSnapshot.getValue(Events.class);
                    list.add(events);
                }
                adapterRecycler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        adapterRecycler.setOnItemClickListener(new HomeEventAdapterRecycler.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Events selectedEvent = list.get(position);
                Intent intent = new Intent(Home.this, ProgramDetail.class);
                intent.putExtra("programName", selectedEvent.getName());
                intent.putExtra("programType", selectedEvent.getType());
                intent.putExtra("programDate", selectedEvent.getDate());
                intent.putExtra("programTime", selectedEvent.getTime());
                intent.putExtra("programFee", selectedEvent.getFee());
                intent.putExtra("programLocation", selectedEvent.getLocation());
                intent.putExtra("programDescription", selectedEvent.getDescription());
                intent.putExtra("programMerit", selectedEvent.getMerit());
                intent.putExtra("programImage", selectedEvent.getImageURL());
                intent.putExtra("programQR", selectedEvent.getQrimageURL());
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(view -> startActivity(new Intent(Home.this, UserProfile.class)));

        btn_search.setOnClickListener(view -> startActivity(new Intent(Home.this, EventsCatalog.class)));

        btn_signout.setOnClickListener(v -> startActivity(new Intent(Home.this, Login.class)));

        btn_ticket.setOnClickListener(v -> startActivity(new Intent(Home.this, EventJoined.class)));

        img_c1.setOnClickListener(view -> startActivity(new Intent(Home.this, EventsCatalog.class)));

        img_c2.setOnClickListener(view -> startActivity(new Intent(Home.this, EventsCatalog.class)));
    }
}
