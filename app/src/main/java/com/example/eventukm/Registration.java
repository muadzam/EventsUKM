package com.example.eventukm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventukm.Objects.UserInfos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    Button btn_joinus_registration;
    protected EditText etEmail, etPassword, etName, etMatrix, etPhone;
    private Spinner spCollege, spFaculty;
    private FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseUserInfo;

    private static final String ROLE_ADMIN = "User/Organiser";
    private static final String ROLE_USER = "User";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn_joinus_registration = findViewById(R.id.btn_joinus_registration);
        etEmail = findViewById(R.id.et_email_registration);
        etPassword = findViewById(R.id.et_pw_registration);
        etName = findViewById(R.id.et_name_registration);
        etMatrix = findViewById(R.id.et_matrix_registration);
        etPhone = findViewById(R.id.et_phone_registration);
        spCollege = findViewById(R.id.sp_college);
        spFaculty = findViewById(R.id.sp_faculty);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Define options for the spinner
        ArrayAdapter<CharSequence> collegeAdapter = ArrayAdapter.createFromResource(this, R.array.college_options, android.R.layout.simple_spinner_item);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollege.setAdapter(collegeAdapter);

        ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(this, R.array.faculty_options, android.R.layout.simple_spinner_item);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFaculty.setAdapter(facultyAdapter);

        // Get reference to the Firebase Realtime Database
        databaseUserInfo = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("UserInfos");

        btn_joinus_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String name = etName.getText().toString();
                String matrix = etMatrix.getText().toString();
                String college = spCollege.getSelectedItem().toString();
                String faculty = spFaculty.getSelectedItem().toString();
                String phone = etPhone.getText().toString();

                name = name.trim().toUpperCase();
                matrix = matrix.trim().toUpperCase();
                college = college.trim();
                faculty = faculty.trim();
                password = password.trim();
                email = email.trim();
                phone = phone.trim();

                if (password.isEmpty() || email.isEmpty() || name.isEmpty() || college.isEmpty() || faculty.isEmpty() || phone.isEmpty()) {
                    // Remind the user to key in the data
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setMessage("Please enter all the required information").setTitle("Warning").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (!matrix.isEmpty() && !matrix.matches("A\\d+")) {
                    // Validate matrix number if it's not empty
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setMessage("Matrix number must start with 'A' followed by numbers").setTitle("Warning").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
                    // Validate password (at least one letter, one number, and minimum 6 characters)
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setMessage("Password must contain at least one letter, one number, and be at least 6 characters long").setTitle("Warning").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    String finalName = name;
                    String finalMatrix = matrix;
                    String finalCollege = college;
                    String finalFaculty = faculty;
                    String finalPassword = password;
                    String finalEmail = email;
                    String finalPhone = phone;
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String role;
                                if (finalEmail.endsWith("@siswa.ukm.edu.my")) {
                                    role = ROLE_ADMIN;
                                } else {
                                    role = ROLE_USER;
                                }

                                String userId = mFirebaseAuth.getCurrentUser().getUid();
                                UserInfos newUser = new UserInfos(finalName, finalMatrix, finalCollege, finalFaculty, finalPassword, finalEmail, finalPhone, role);
                                // Use userId as the key to store user information
                                databaseUserInfo.child(userId).setValue(newUser);
                                startActivity(new Intent(Registration.this, Login.class));
                                Toast.makeText(Registration.this, "Pendaftaran Berjaya ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Registration.this, "Pendaftaran Tidak Berjaya: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
