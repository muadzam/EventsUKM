package com.example.eventukm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    EditText etName, etPhone;
    Spinner spFaculty, spCollege;
    Button btnChange;
    ImageView btn_back;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        etName = findViewById(R.id.et_name_registration);
        etPhone = findViewById(R.id.et_phone_registration);
        spFaculty = findViewById(R.id.sp_faculty);
        spCollege = findViewById(R.id.sp_college);
        btnChange = findViewById(R.id.btn_joinus_registration);
        btn_back = findViewById(R.id.btn_back_profile);

        // Define options for the spinner
        ArrayAdapter<CharSequence> collegeAdapter = ArrayAdapter.createFromResource(this, R.array.college_options, android.R.layout.simple_spinner_item);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollege.setAdapter(collegeAdapter);

        ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(this, R.array.faculty_options, android.R.layout.simple_spinner_item);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFaculty.setAdapter(facultyAdapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("UserInfos");

        // Set button click listener
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(); // Call the method to update profile when the button is clicked
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this, UserProfile.class));
            }
        });

        // Fetch current user's information and populate EditText fields
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            // Use currentUserId to fetch user info and populate EditText fields
            // For simplicity, assuming all user info is available locally or from previous activity
            // Update the EditText fields accordingly
        }
    }

    private void updateUserProfile() {
        // Get updated user inputs
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String faculty = "";
        String college = "";

        // Get the selected values from spinners if any
        if (spFaculty.getSelectedItem() != null) {
            faculty = spFaculty.getSelectedItem().toString();
        }
        if (spCollege.getSelectedItem() != null) {
            college = spCollege.getSelectedItem().toString();
        }

        // Get current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            // Update user info in Firebase Realtime Database
            DatabaseReference userRef = databaseReference.child(currentUserId);

            // Update only non-empty fields
            if (!name.isEmpty()) {
                userRef.child("name").setValue(name);
            }
            if (!phone.isEmpty()) {
                userRef.child("phone").setValue(phone);
            }
            // Check if the user has selected faculty and college
            if (!faculty.isEmpty()) {
                userRef.child("faculty").setValue(faculty);
            }
            if (!college.isEmpty()) {
                userRef.child("college").setValue(college);
            }

            // Inform user about successful update
            Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Finish the activity
            finish();
        }
    }


}
