package com.example.eventukm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.Adapter.EventJoinedAdapterRecycler;
import com.example.eventukm.Adapter.UserEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventJoined extends AppCompatActivity {

    private static final String TAG = "EventJoined";
    private ImageView btn_back;
    private DatabaseReference databaseReference;
    private EventJoinedAdapterRecycler adapterRecycler;
    private RecyclerView recyclerView;
    private ArrayList<UserEvent> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_joined);

        recyclerView = findViewById(R.id.joinedprogramrecyler);
        btn_back = findViewById(R.id.btn_back_profile);

        databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("UserEventInfos");

        list = new ArrayList<>();
        adapterRecycler = new EventJoinedAdapterRecycler(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterRecycler);

        adapterRecycler.setOnItemClickListener(userEvent -> {
            Intent intent = new Intent(EventJoined.this, DetailsParticipant.class);
            intent.putExtra("programName", userEvent.getEventName());
            intent.putExtra("programDate", userEvent.getEventDate());
            intent.putExtra("programTime", userEvent.getEventTime());
            intent.putExtra("programFee", userEvent.getEventFee());
            intent.putExtra("programMerit", userEvent.getEventMerit());
            intent.putExtra("programLocation", userEvent.getEventLocation());
            intent.putExtra("receiptURL", userEvent.getReceiptURL());
            intent.putExtra("email", userEvent.getEmail());
            intent.putExtra("name", userEvent.getName());
            intent.putExtra("phone", userEvent.getPhone());
            intent.putExtra("matrix", userEvent.getMatrix());
            startActivity(intent);
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userJoinRef = databaseReference.child(userUID);

            userJoinRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserEvent userEvent = dataSnapshot.getValue(UserEvent.class);
                        if (userEvent != null) {
                            list.add(userEvent);
                            Log.d(TAG, "UserEvent: " + userEvent.getName());
                        } else {
                            Log.d(TAG, "UserEvent is null");
                        }
                    }
                    adapterRecycler.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
                }
            });
        } else {
            Log.e(TAG, "User not logged in");
        }

        btn_back.setOnClickListener(v -> startActivity(new Intent(EventJoined.this, Home.class)));
    }
}
