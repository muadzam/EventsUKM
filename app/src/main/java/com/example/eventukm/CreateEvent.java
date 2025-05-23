package com.example.eventukm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventukm.Adapter.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 100;

    Button btnCreate;
    ImageView btn_back, programimage, qrpayment;
    EditText name, date, time, fee, description, location;
    Spinner sptype;
    RadioGroup istarmerit;
    RadioButton radioButtoniStarSelected;
    DatabaseReference databaseEvent;
    StorageReference storageReference;
    Uri imageUri, qrimageUri;
    boolean isProgramImage;
    private FirebaseAuth mAuth;

    private DatePickerDialog picker;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnCreate = findViewById(R.id.btn_create);
        btn_back = findViewById(R.id.btn_back);
        name = findViewById(R.id.et_name);
        date = findViewById(R.id.et_date);
        time = findViewById(R.id.et_time);
        fee = findViewById(R.id.et_fee);
        sptype = findViewById(R.id.sp_type);
        location = findViewById(R.id.et_location);
        description = findViewById(R.id.et_description);
        programimage = findViewById(R.id.img_programimage);
        qrpayment = findViewById(R.id.img_qrpayment);

        storageReference = FirebaseStorage.getInstance("gs://eventsukm.appspot.com").getReference("Program Pictures");

        // Radio button for Merit iStar
        istarmerit = findViewById(R.id.rb_istarmerit);
        istarmerit.clearCheck();

        // Define options for the spinner
        ArrayAdapter<CharSequence> collegeAdapter = ArrayAdapter.createFromResource(this, R.array.event_options, android.R.layout.simple_spinner_item);
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptype.setAdapter(collegeAdapter);

        databaseEvent = FirebaseDatabase.getInstance("https://eventsukm-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImagesAndInsertData();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        currentTime.set(Calendar.HOUR_OF_DAY, hour);
                        currentTime.set(Calendar.MINUTE, minute);

                        String myFormat = "HH:mm";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                        time.setText(dateFormat.format(currentTime.getTime()));
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateEvent.this, EventsCatalog.class));
            }
        });

        programimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProgramImage = true;
                selectImage();
            }
        });

        qrpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProgramImage = false;
                selectImage();
            }
        });
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
            Uri selectedImageUri = data.getData();
            if (isProgramImage) {
                imageUri = selectedImageUri;
                programimage.setImageURI(imageUri);
            } else {
                qrimageUri = selectedImageUri;
                qrpayment.setImageURI(qrimageUri);
            }
        }
    }

    private void uploadImagesAndInsertData() {
        // Validate input fields
        if (!validateInputs()) {
            return;
        }

        final String timestamp = String.valueOf(System.currentTimeMillis());
        final String imageFilename = "event_" + timestamp + ".jpg";
        final String qrFilename = "event_qrpayment_" + timestamp + ".jpg";

        StorageReference imageRef = storageReference.child("eventImages/" + imageFilename);
        StorageReference qrRef = storageReference.child("eventQRPayment/" + qrFilename);

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        uploadQRImageAndInsertData(imageURL, qrRef);
                    }
                });
            }
        });
    }

    private void uploadQRImageAndInsertData(final String imageURL, StorageReference qrRef) {
        qrRef.putFile(qrimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                qrRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String qrimageURL = uri.toString();
                        insertData(imageURL, qrimageURL);
                    }
                });
            }
        });
    }

    private void insertData(String imageURL, String qrimageURL) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String organiserId = currentUser.getUid();
        int selectMeritid = istarmerit.getCheckedRadioButtonId();
        if (selectMeritid == -1) {
            Toast.makeText(this, "Please select iStar Merit option", Toast.LENGTH_SHORT).show();
            return;
        }

        radioButtoniStarSelected = findViewById(selectMeritid);

        String programname = name.getText().toString();
        String programdate = date.getText().toString();
        String programtime = time.getText().toString();
        String programfee = fee.getText().toString();
        String programdescription = description.getText().toString();
        String programtype = sptype.getSelectedItem().toString();
        String programmerit = radioButtoniStarSelected.getText().toString();
        String programlocation = location.getText().toString();
        String id = databaseEvent.push().getKey();

        Events events = new Events(programname, programdate, programtime, programfee, programdescription, programtype, programmerit, programlocation, imageURL, qrimageURL, organiserId);
        String eventId = databaseEvent.child("Events").push().getKey();

        // Save event under UserInfos (Organiser)
        databaseEvent.child("UserInfos").child(organiserId).child("registeredevent").child(eventId).setValue(events);

        // Save event under Events
        databaseEvent.child("Events").child(id).setValue(events).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateEvent.this, "Program Details Inserted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateEvent.this, Home.class));
                    finish();
                } else {
                    Toast.makeText(CreateEvent.this, "Failed to insert program details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs() {
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the event name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (time.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fee.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the fee", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!fee.getText().toString().equalsIgnoreCase("Free") && !fee.getText().toString().startsWith("RM")) {
            Toast.makeText(this, "Please enter the fee starting with RM or specify 'Free'", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (location.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sptype.getSelectedItem() == null) {
            Toast.makeText(this, "Please select an event type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(this, "No image selected for the program", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (qrimageUri == null) {
            Toast.makeText(this, "No QR image selected for payment", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
