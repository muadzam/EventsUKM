package com.example.eventukm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventukm.Adapter.EventAdapterRecycler;
import com.example.eventukm.Adapter.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class EventsCatalog extends AppCompatActivity {
    private ImageView btn_back_catalog;
    private Button btn_create;
    private SearchView searchView;
    private ImageButton btn_filter;
    private RecyclerView recyclerView;
    private ArrayList<Events> list;
    private DatabaseReference databaseReference;
    private EventAdapterRecycler adapterRecycler;
    private String selectedDate = "All";
    private String selectedType = "All";

    // Filter dialog components
    private Dialog filterDialog;
    private Spinner dialogSpDate, dialogSpProgramType;
    private Button btnApplyFilters;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EventsCatalog.this, Home.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_catalog);

        // Initialize views
        searchView = findViewById(R.id.search_bar);
        btn_filter = findViewById(R.id.btn_filter);
        recyclerView = findViewById(R.id.program_recycler);
        btn_back_catalog = findViewById(R.id.btn_back);
        btn_create = findViewById(R.id.btn_create);

        // Check if the current user is an admin
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("UserInfos").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String role = snapshot.child("role").getValue(String.class);
                        if (!"User/Organiser".equals(role)) {
                            btn_create.setVisibility(View.GONE); // Hide the create event button for non-admin users
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors
                }
            });
        }

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Events");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycler = new EventAdapterRecycler(this, list);
        recyclerView.setAdapter(adapterRecycler);

        // Handle search text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, selectedDate, selectedType);
                return true;
            }
        });

        // Load event data
        loadEventData();

        // Handle back button click
        btn_back_catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventsCatalog.this, Home.class));
            }
        });

        // Handle create button click
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsCatalog.this, CreateEvent.class));
            }
        });

        // Handle filter button click
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        // Handle item click in RecyclerView
        adapterRecycler.setOnItemClickListener(new EventAdapterRecycler.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(EventsCatalog.this, ProgramDetail.class);
                Events selectedEvent = adapterRecycler.getEventAt(position);

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
    }

    private void showFilterDialog() {
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.dialogfilter);

        TextView dialogTvDate = filterDialog.findViewById(R.id.dialog_tv_date);
        dialogSpProgramType = filterDialog.findViewById(R.id.dialog_sp_programtype);
        btnApplyFilters = filterDialog.findViewById(R.id.btn_apply_filters);
        Button btnResetFilters = filterDialog.findViewById(R.id.btn_reset_filters);

        // Set up the DatePickerDialog
        dialogTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EventsCatalog.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dialogTvDate.setText(selectedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Populate program type spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(getTypes()));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSpProgramType.setAdapter(typeAdapter);

        // Apply filters button
        btnApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = dialogTvDate.getText().toString();
                if (selectedDate.equals("Tarikh Program")) {
                    selectedDate = "All";  // Reset to default if no date is selected
                }
                selectedType = dialogSpProgramType.getSelectedItem().toString();
                filterList(searchView.getQuery().toString(), selectedDate, selectedType);
                filterDialog.dismiss();
            }
        });

        // Reset filters button
        btnResetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTvDate.setText("Tarikh Program");
                dialogSpProgramType.setSelection(0);
                selectedDate = "All";
                selectedType = "All";
                filterList(searchView.getQuery().toString(), selectedDate, selectedType);
            }
        });

        // Adjust the dialog size
        filterDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        filterDialog.show();
    }





    private Set<String> getDates() {
        Set<String> dates = new HashSet<>();
        dates.add("All");
        for (Events event : list) {
            dates.add(event.getDate());
        }
        return dates;
    }

    private Set<String> getTypes() {
        Set<String> types = new HashSet<>();
        types.add("All");
        for (Events event : list) {
            types.add(event.getType());
        }
        return types;
    }

    private void loadEventData() {
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
                // Handle possible errors
            }
        });
    }

    private void filterList(String text, String date, String type) {
        ArrayList<Events> filteredList = new ArrayList<>();
        for (Events events : list) {
            boolean matchesText = events.getName().toLowerCase().contains(text.toLowerCase());
            boolean matchesDate = date.equals("All") || events.getDate().equals(date);
            boolean matchesType = type.equals("All") || events.getType().equals(type);

            if (matchesText && matchesDate && matchesType) {
                filteredList.add(events);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterRecycler.setFilteredList(filteredList);
        }
    }
}
