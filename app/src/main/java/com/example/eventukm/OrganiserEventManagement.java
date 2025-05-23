package com.example.eventukm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.Adapter.Events;
import com.example.eventukm.Adapter.OrganiserEventManagementAdapterRecycler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganiserEventManagement extends AppCompatActivity implements OrganiserEventManagementAdapterRecycler.OnItemClickListener {

    private RecyclerView recyclerView;
    private OrganiserEventManagementAdapterRecycler adapter;
    private ArrayList<Events> eventList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ImageView btn_back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_event_management);

        btn_back = findViewById(R.id.btn_back_profile);
        recyclerView = findViewById(R.id.recycler_view_participants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new OrganiserEventManagementAdapterRecycler(this, eventList, this);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String organiserId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
                    .child("UserInfos").child(organiserId).child("registeredevent");

            loadEvents();
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrganiserEventManagement.this, UserProfile.class));
            }
        });


    }

    private void loadEvents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Events events = eventSnapshot.getValue(Events.class);
                    eventList.add(events);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrganiserEventManagement.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Events events) {
        Intent intent = new Intent(OrganiserEventManagement.this, DetailsParticipantOrganiser.class);
        intent.putExtra("programName", events.getName());
        startActivity(intent);
    }

}
