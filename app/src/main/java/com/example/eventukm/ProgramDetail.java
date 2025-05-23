package com.example.eventukm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProgramDetail extends AppCompatActivity {

    ImageView btn_back_detail, programImage, programQR;
    Button btn_joinprog;
    TextView programName, programType, programDate, programTime, programFee, programLocation, programDescription, programMerit;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        btn_back_detail = findViewById(R.id.btn_back_detail);
        btn_joinprog = findViewById(R.id.btn_join);
        programName = findViewById(R.id.tv_programname_details);
        programType = findViewById(R.id.tv_typeprogram);
        programDate = findViewById(R.id.tv_programdate);
        programTime = findViewById(R.id.tv_programtime);
        programFee = findViewById(R.id.tv_feeprogram);
        programLocation = findViewById(R.id.tv_programlocation);
        programDescription = findViewById(R.id.tv_programdescription);
        programMerit = findViewById(R.id.tv_programmerit);
        programImage = findViewById(R.id.img_posterprogram);
        programQR = findViewById(R.id.img_programqr);

        databaseReference = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Events");

        Intent intent = getIntent();
        String programNameStr = intent.getStringExtra("programName");
        String programTypeStr = intent.getStringExtra("programType");
        String programDateStr = intent.getStringExtra("programDate");
        String programTimeStr = intent.getStringExtra("programTime");
        String programFeeStr = intent.getStringExtra("programFee");
        String programLocationStr = intent.getStringExtra("programLocation");
        String programDescriptionStr = intent.getStringExtra("programDescription");
        String programMeritStr = intent.getStringExtra("programMerit");
        String imageURL = intent.getStringExtra("programImage");
        String QRimageURL = intent.getStringExtra("programQR");
        String organiserId = intent.getStringExtra("organiserUID");

        Picasso.get().load(imageURL).into(programImage);
        Picasso.get().load(QRimageURL).into(programQR);
        programName.setText(programNameStr);
        programType.setText(programTypeStr);
        programDate.setText(programDateStr);
        programTime.setText(programTimeStr);
        programFee.setText(programFeeStr);
        programLocation.setText(programLocationStr);
        programDescription.setText(programDescriptionStr);
        programMerit.setText(programMeritStr);

        btn_back_detail.setOnClickListener(view -> startActivity(new Intent(ProgramDetail.this, EventsCatalog.class)));

        btn_joinprog.setOnClickListener(view -> {
            Intent registrationIntent = new Intent(ProgramDetail.this, EventRegistration.class);
            registrationIntent.putExtra("programName", programNameStr);
            registrationIntent.putExtra("programType", programTypeStr);
            registrationIntent.putExtra("programDate", programDateStr);
            registrationIntent.putExtra("programTime", programTimeStr);
            registrationIntent.putExtra("programFee", programFeeStr);
            registrationIntent.putExtra("programLocation", programLocationStr);
            registrationIntent.putExtra("programDescription", programDescriptionStr);
            registrationIntent.putExtra("programMerit", programMeritStr);
            registrationIntent.putExtra("organiserUID", organiserId);  // Pass organiserUID
            startActivity(registrationIntent);
        });
    }
}
