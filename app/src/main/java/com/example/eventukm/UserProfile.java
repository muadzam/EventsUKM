package com.example.eventukm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventukm.Objects.UserInfos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  UserProfile extends AppCompatActivity {
    ImageView btn_back;
    Button btn_logout;
    TextView tv_name, tv_matrix, tv_college, tv_faculty, tv_email, tv_phone, tv_roleuser, tv_manageevent,tv_editprofile;

    //reference for the Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    //reference for the User in the firebase
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        btn_back = findViewById(R.id.btn_back_profile);
        btn_logout = findViewById(R.id.btn_logout);
        tv_name = findViewById(R.id.tv_name);
        tv_matrix = findViewById(R.id.tv_matrix);
        tv_college = findViewById(R.id.tv_college);
        tv_faculty = findViewById(R.id.tv_faculty);
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        tv_roleuser = findViewById(R.id.tv_userrole);
        tv_manageevent = findViewById(R.id.tv_manageevent);
        tv_editprofile = findViewById(R.id.tv_editprofile);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseUserInfo = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserInfos");

        // Get the current user's ID
        String userId = mFirebaseAuth.getCurrentUser().getUid();

        // Retrieve user information from Firebase Realtime Database
        databaseUserInfo.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    // Retrieve user information from the snapshot
                    UserInfos user = snapshot.getValue(UserInfos.class);

                    // Set the TextViews with user information
                    if (user != null) {
                        tv_name.setText(user.getName());
                        tv_matrix.setText(user.getMatrix());
                        String college = user.getCollege();
                        String faculty = user.getFaculty();
                        tv_college.setText(college);
                        tv_faculty.setText(faculty);
                        tv_phone.setText(user.getPhone());
                        tv_email.setText(user.getEmail());
                        tv_roleuser.setText(user.getRole());

                        // Check if the user is an organiser
                        // You can adjust this condition based on your role system
                        if ("User/Organiser".equals(user.getRole())) {
                            // If yes, make the manage event TextView visible
                            tv_manageevent.setVisibility(View.VISIBLE);
                        } else {
                            // If not, hide the manage event TextView
                            tv_manageevent.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        // Back button click listener
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, Home.class));
            }
        });

        // Logout button click listener
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                Intent intent = new Intent(UserProfile.this, Login.class);
                startActivity(intent);
            }
        });

        tv_manageevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, OrganiserEventManagement.class));
            }
        });

        tv_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfile.this, EditProfile.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is still logged in
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // If not, navigate to the login page
            Intent intent = new Intent(UserProfile.this, Login.class);
            startActivity(intent);
            finish(); // Close this activity
        }
    }
}
