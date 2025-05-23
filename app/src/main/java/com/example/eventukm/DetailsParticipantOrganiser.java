package com.example.eventukm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.Adapter.EventJoinedOrganiserAdapterRecycler;
import com.example.eventukm.Adapter.UserEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsParticipantOrganiser extends AppCompatActivity {

    private static final String TAG = "DetailsParticipant";
    private RecyclerView recyclerView;
    private EventJoinedOrganiserAdapterRecycler adapterRecycler;
    private ArrayList<UserEvent> participantList;
    private DatabaseReference databaseReference;
    private ImageView btn_back;
    private String programName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_participant_organiser);

        recyclerView = findViewById(R.id.participant_recycler);
        btn_back = findViewById(R.id.btn_back_profile);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        participantList = new ArrayList<>();
        adapterRecycler = new EventJoinedOrganiserAdapterRecycler(this, participantList);
        recyclerView.setAdapter(adapterRecycler);

        Intent intent = getIntent();
        programName = intent.getStringExtra("programName");

        databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("UserEventInfos");

        loadParticipants();

        adapterRecycler.setOnItemClickListener(userEvent -> {
            Intent detailsIntent = new Intent(DetailsParticipantOrganiser.this, DetailsParticipant.class);
            detailsIntent.putExtra("programName", userEvent.getEventName());
            detailsIntent.putExtra("programDate", userEvent.getEventDate());
            detailsIntent.putExtra("programTime", userEvent.getEventTime());
            detailsIntent.putExtra("programFee", userEvent.getEventFee());
            detailsIntent.putExtra("programMerit", userEvent.getEventMerit());
            detailsIntent.putExtra("programLocation", userEvent.getEventLocation());
            detailsIntent.putExtra("receiptURL", userEvent.getReceiptURL());
            detailsIntent.putExtra("name", userEvent.getName());
            detailsIntent.putExtra("email", userEvent.getEmail());
            detailsIntent.putExtra("matrix", userEvent.getMatrix());
            detailsIntent.putExtra("phone", userEvent.getPhone());
            startActivity(detailsIntent);
        });

        btn_back.setOnClickListener(v -> finish());
    }

    private void loadParticipants() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot participantSnapshot : eventSnapshot.getChildren()) {
                        UserEvent userEvent = participantSnapshot.getValue(UserEvent.class);
                        if (userEvent != null && programName.equals(userEvent.getEventName())) {
                            participantList.add(userEvent);
                            Log.d(TAG, "UserEvent: " + userEvent.getName());
                        }
                    }
                }
                adapterRecycler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailsParticipantOrganiser.this, "Failed to load participants", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }
}
