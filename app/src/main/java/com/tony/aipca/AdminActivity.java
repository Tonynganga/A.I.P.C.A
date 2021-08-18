package com.tony.aipca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tony.aipca.Utils.MemberModel;
import com.tony.aipca.Utils.ReadingModel;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final int REQUEST_CODE = 100;
    private MaterialCardView addMember, addAdmin, addReadings, addHistory, addAnnouncement, logout;
    AlertDialog.Builder dialogBuilder, admindialogBuilder, readingdialogBuilder, anndialogBuilder;
    AlertDialog dialog, admindialog, readingdialog, anndialog;
    private Uri imageUri;

    CircleImageView comMemPhoto;
    ImageView annImage;
    EditText comMName, comMPosition, comMPhone, comMLocation, annDescription, annDate;
    private Button comMPhotoSelect, comMSave, comMCancel, btnadminsave, btnadmincancel, savereading, cancelreading;
            Button btnselectrdate, annImageSelect, annSave, annCancel;
    private EditText adminusername, adminpassword;

    EditText readingday, readingdate, englishreading, kiswahilireading, kikuyureading,
    englishinjili, kiswahiliinjili, kikuyuinjili;



    private DatabaseReference mMemberRef;
    private DatabaseReference mReadingRef;

    ProgressDialog LoadingBar;


    private StorageReference mMemRef;
    private FirebaseAuth adminAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        LoadingBar = new ProgressDialog(this);


        mMemberRef = FirebaseDatabase.getInstance().getReference().child("CommitteeMembers");
        mReadingRef = FirebaseDatabase.getInstance().getReference().child("Readings");
        mMemRef = FirebaseStorage.getInstance().getReference("MembersImages");
        adminAuth = FirebaseAuth.getInstance();

        addMember = findViewById(R.id.addMember);
        addAdmin = findViewById(R.id.addAdministrator);
        addReadings = findViewById(R.id.addreading);
        addHistory = findViewById(R.id.addhistory);
        addAnnouncement = findViewById(R.id.addannouncement);
        logout = findViewById(R.id.logoutcard);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CreateMemberDialog();
            }
        });

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAdminDialog();
            }
        });

        addReadings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateReadingDialog();
            }
        });
        addAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAnnDialog();
            }
        });
    }

    private void CreateAnnDialog() {
        anndialogBuilder = new AlertDialog.Builder(this);
        View annView = getLayoutInflater().inflate(R.layout.announcementdialog, null);
        annImage = annView.findViewById(R.id.annImage);
        annImageSelect = annView.findViewById(R.id.btnanndate);
        annSave = annView.findViewById(R.id.btnsaveann);
        annCancel = annView.findViewById(R.id.btncancelann);
        annDescription = annView.findViewById(R.id.edt_announcement);
        annDate = annView.findViewById(R.id.edtanndate);
        anndialogBuilder.setView(annView);
        anndialog = anndialogBuilder.create();
        anndialog.show();

        annImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        annImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);


            }
        });

        annCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anndialog.dismiss();
            }
        });
        annSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AnnDate = annDate.getText().toString();
                String AnnDescription = annDescription.getText().toString();

            }
        });


    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        readingdate.setText(date);
        String ANNDATE = dayOfMonth + "/" + month + "/" + year;
        annDate.setText(ANNDATE);
    }

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void CreateReadingDialog() {
        readingdialogBuilder = new AlertDialog.Builder(this);
        View readingView = getLayoutInflater().inflate(R.layout.readingdialog, null);
        readingday = readingView.findViewById(R.id.readingday);
        readingdate = readingView.findViewById(R.id.readingdate);
        englishreading = readingView.findViewById(R.id.edtenglishreading);
        kiswahilireading = readingView.findViewById(R.id.edtkiswahilireading);
        kikuyureading = readingView.findViewById(R.id.edtkikuyureading);
        englishinjili = readingView.findViewById(R.id.edtenglishinjili);
        kiswahiliinjili = readingView.findViewById(R.id.edtkiswahiliinjili);
        kikuyuinjili = readingView.findViewById(R.id.edtkikuyuiinjili);
        btnselectrdate = readingView.findViewById(R.id.btnselectdate);
        savereading = readingView.findViewById(R.id.btnsavereading);
        cancelreading = readingView.findViewById(R.id.btncancelreading);
        readingdialogBuilder.setView(readingView);
        readingdialog = readingdialogBuilder.create();
        readingdialog.show();

        btnselectrdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        cancelreading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readingdialog.dismiss();
            }
        });

        savereading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ReadingDay = readingday.getText().toString();
                String ReadingDate = readingdate.getText().toString();
                String EnglishReading = englishreading.getText().toString();
                String KiswahiliReading = kiswahilireading.getText().toString();
                String KikuyuReading = kikuyureading.getText().toString();
                String EnglishInjili = englishinjili.getText().toString();
                String KiswahiliInjili = kiswahiliinjili.getText().toString();
                String KikuyuInjili = kikuyuinjili.getText().toString();
                ReadingModel readingModel = new ReadingModel(ReadingDay, ReadingDate, EnglishReading, KiswahiliReading, KikuyuReading,
                        EnglishInjili, KiswahiliInjili, KikuyuInjili);
                mReadingRef.push().setValue(readingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminActivity.this, "Reading Saved Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AdminActivity.this, "Try Again Later!!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }



    private void CreateAdminDialog() {
        admindialogBuilder = new AlertDialog.Builder(this);
        final View adminView = getLayoutInflater().inflate(R.layout.createadmindialog, null);
        adminusername = adminView.findViewById(R.id.editadminusername);
        adminpassword = adminView.findViewById(R.id.edittextadminpass);
        btnadminsave = adminView.findViewById(R.id.btnsaveadmin);
        btnadmincancel = adminView.findViewById(R.id.btncanceladmin);
        admindialogBuilder.setView(adminView);
        admindialog = admindialogBuilder.create();
        admindialog.show();

        btnadminsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AdminUsername = adminusername.getText().toString();
                String AdminPassword = adminpassword.getText().toString();

                adminAuth.createUserWithEmailAndPassword(AdminUsername, AdminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminActivity.this, "Admin Saved", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AdminActivity.this, "Admin Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        btnadmincancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admindialog.dismiss();
            }
        });
    }

    private void CreateMemberDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View memberView = getLayoutInflater().inflate(R.layout.addcommember, null);
        comMName = memberView.findViewById(R.id.addmem_name);
        comMPosition = memberView.findViewById(R.id.addmem_position);
        comMPhone = memberView.findViewById(R.id.addmem_phone);
        comMLocation = memberView.findViewById(R.id.addmem_location);
        comMSave = memberView.findViewById(R.id.btnmem_save);
        comMCancel = memberView.findViewById(R.id.btnmem_cancel);
        comMemPhoto = memberView.findViewById(R.id.addmem_imageview);
        comMPhotoSelect = memberView.findViewById(R.id.btnselectphoto);
        dialogBuilder.setView(memberView);
        dialog = dialogBuilder.create();
        dialog.show();

        comMCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        comMPhotoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        comMSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    saveCommitteeMember(imageUri);
                }else{
                    Toast.makeText(AdminActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveCommitteeMember(Uri uri) {
        String membername = comMName.getText().toString();
        String memberposition = comMPosition.getText().toString();
        String memberphone = comMPhone.getText().toString();
        String memberlocation = comMLocation.getText().toString();

        if (membername.isEmpty() || membername.length() < 3){
            showError(comMName, "Name is not valid!! Must be more then 3 characters.");
        }else  if (memberposition.isEmpty()){
            showError(comMPosition, "Position can not be null");
        }else if (memberphone.isEmpty() || memberphone.length()<10){
            showError(comMPhone, "Phone number not valid!!");
        }else if (memberlocation.isEmpty()){
            showError(comMLocation, "Location cannot be null!!");
        }else if (imageUri == null){
            Toast.makeText(this, "Please Select an Image!!", Toast.LENGTH_SHORT).show();
        }else {
            LoadingBar.setTitle("Saving Member");
            LoadingBar.setMessage("Please Wait a Moment.");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();

            final StorageReference fileRef = mMemRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            MemberModel memberModel = new MemberModel(membername, memberposition, memberphone, memberlocation, uri.toString());
                            String modelId = mMemberRef.push().getKey();
                            mMemberRef.child(modelId).setValue(memberModel);
                            LoadingBar.dismiss();
                            comMName.setText("");
                            comMPosition.setText("");
                            comMPhone.setText("");
                            comMLocation.setText("");
                            comMemPhoto.setImageURI(null);
                            Toast.makeText(AdminActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LoadingBar.dismiss();
                    Toast.makeText(AdminActivity.this, "Something Went Wrong.Please try Again!!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            comMemPhoto.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}