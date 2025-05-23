package com.example.eventukm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventukm.Adapter.UserEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EventRegistration extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 100;

    Button btn_register;
    protected EditText et_emailregi, et_nameregi, et_matrixregi, et_phoneregi;
    TextView programname, programdate, programtime, programfee, programmerit, programlocation;
    ImageView userreceipt;
    Uri receiptUri;

    DatabaseReference databaseEventUser, databaseParticipants;
    StorageReference storageReference;
    String organiserUID;  // Add organiserUID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);

        btn_register = findViewById(R.id.btn_registeregi);
        et_emailregi = findViewById(R.id.et_emailregi);
        et_nameregi = findViewById(R.id.et_nameuserregi);
        et_matrixregi = findViewById(R.id.et_matrixregi);
        et_phoneregi = findViewById(R.id.et_phoneregi);
        programname = findViewById(R.id.tv_programnameregi);
        programtime = findViewById(R.id.tv_programtimeregi);
        programdate = findViewById(R.id.tv_programdateregi);
        programfee = findViewById(R.id.tv_programfeeregi);
        programmerit = findViewById(R.id.tv_programmeritregi);
        programlocation = findViewById(R.id.tv_locationprogram);
        userreceipt = findViewById(R.id.img_receiptregi);

        // Add an InputFilter to et_nameregi to automatically convert input to uppercase
        et_nameregi.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Initialize Firebase references
        databaseEventUser = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("UserEventInfos");
        databaseParticipants = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("UserInfos");
        storageReference = FirebaseStorage.getInstance("gs://eventsukm.appspot.com").getReference("User Receipts");

        Intent intent = getIntent();
        String programNameStr = intent.getStringExtra("programName");
        String programTimeStr = intent.getStringExtra("programTime");
        String programDateStr = intent.getStringExtra("programDate");
        String programFeeStr = intent.getStringExtra("programFee");
        String programMeritStr = intent.getStringExtra("programMerit");
        String programLocationStr = intent.getStringExtra("programLocation");
        organiserUID = intent.getStringExtra("organiserUID");  // Retrieve organiserUID from intent

        programname.setText(programNameStr);
        programtime.setText(programTimeStr);
        programdate.setText(programDateStr);
        programfee.setText(programFeeStr);
        programmerit.setText(programMeritStr);
        programlocation.setText(programLocationStr);

        btn_register.setOnClickListener(view -> registerUserForEvent());

        userreceipt.setOnClickListener(view -> selectImage());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            receiptUri = data.getData();
            Picasso.get().load(receiptUri).into(userreceipt);
        }
    }

    private void registerUserForEvent() {
        String email = et_emailregi.getText().toString().trim();
        String name = et_nameregi.getText().toString().trim();
        String matrix = et_matrixregi.getText().toString().trim();
        String phone = et_phoneregi.getText().toString().trim();
        String eventName = programname.getText().toString().trim();
        String eventTime = programtime.getText().toString().trim();
        String eventDate = programdate.getText().toString().trim();
        String eventFee = programfee.getText().toString().trim();
        String eventMerit = programmerit.getText().toString().trim();
        String eventLocation = programlocation.getText().toString().trim();

        if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || eventName.isEmpty() || eventTime.isEmpty() || eventDate.isEmpty() || eventFee.isEmpty() || eventMerit.isEmpty() || eventLocation.isEmpty() || receiptUri == null) {
            Toast.makeText(EventRegistration.this, "Please fill in all fields and upload a receipt", Toast.LENGTH_SHORT).show();
            return;
        }

        final String timestamp = String.valueOf(System.currentTimeMillis());
        final String receiptFilename = "receipt_" + timestamp + ".jpg";
        StorageReference receiptRef = storageReference.child(receiptFilename);

        receiptRef.putFile(receiptUri).addOnSuccessListener(taskSnapshot -> receiptRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String receiptURL = uri.toString();
            insertData(name, matrix, email, phone, eventName, eventTime, eventDate, eventFee, eventMerit, eventLocation, receiptURL, organiserUID);
        }));
    }

    private void insertData(String name, String matrix, String email, String phone, String eventName, String eventTime, String eventDate, String eventFee, String eventMerit, String eventLocation, String receiptURL, String organiserUID) {
        organiserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userEventRef = databaseEventUser.child(organiserUID).push(); // Generate a unique join ID
        String joinID = userEventRef.getKey(); // Get the generated join ID

        UserEvent newUser = new UserEvent(name, matrix, email, phone, receiptURL, eventName, eventTime, eventDate, eventFee, eventMerit, eventLocation, organiserUID);

        newUser.setOrganiserUID(organiserUID); // Set the organiserUID

        userEventRef.setValue(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EventRegistration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EventRegistration.this, Home.class));
                finish();
            } else {
                Toast.makeText(EventRegistration.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
