package com.example.eventukm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailsParticipant extends AppCompatActivity {

    TextView programName, programDate, programTime, programFee, programMerit, programLocation;
    TextView participantName, participantMatrix, participantEmail, participantPhone;
    ImageView btn_back_detail, img_receiptParticipant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_participant);

        btn_back_detail = findViewById(R.id.btn_back_profile);
        programName = findViewById(R.id.tv_nameprogram_details);
        programDate = findViewById(R.id.tv_programdate_details);
        programTime = findViewById(R.id.tv_programtime_details);
        programFee = findViewById(R.id.tv_programfee_details);
        programMerit = findViewById(R.id.tv_programmerit_details);
        programLocation = findViewById(R.id.tv_programlocation_details);
        participantName = findViewById(R.id.tv_participantname_details);
        participantMatrix = findViewById(R.id.tv_participantmatrix_details);
        participantEmail = findViewById(R.id.tv_participantemail_details);
        participantPhone = findViewById(R.id.tv_participantphone_details);
        img_receiptParticipant = findViewById(R.id.img_participantreceipt_details);

        Intent intent = getIntent();
        String programNameStr = intent.getStringExtra("programName");
        String programDateStr = intent.getStringExtra("programDate");
        String programTimeStr = intent.getStringExtra("programTime");
        String programFeeStr = intent.getStringExtra("programFee");
        String programMeritStr = intent.getStringExtra("programMerit");
        String programLocationStr = intent.getStringExtra("programLocation");
        String receiptURL = intent.getStringExtra("receiptURL");
        String nameparticipant = intent.getStringExtra("name");
        String emailparticipant = intent.getStringExtra("email");
        String matrixparticipant = intent.getStringExtra("matrix");
        String phoneparticipant = intent.getStringExtra("phone");


        programName.setText(programNameStr);
        programDate.setText(programDateStr);
        programTime.setText(programTimeStr);
        programFee.setText(programFeeStr);
        programMerit.setText(programMeritStr);
        programLocation.setText(programLocationStr);
        participantName.setText(nameparticipant);
        participantMatrix.setText(matrixparticipant);
        participantEmail.setText(emailparticipant);
        participantPhone.setText(phoneparticipant);

        // Load the image using Picasso
        Picasso.get().load(receiptURL).into(img_receiptParticipant);

        btn_back_detail.setOnClickListener(view -> startActivity(new Intent(DetailsParticipant.this, EventJoined.class)));
    }
}
