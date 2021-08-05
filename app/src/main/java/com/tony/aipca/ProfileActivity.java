package com.tony.aipca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    CircleImageView profileimageview;
    EditText musername, mphone, mlocation, mlevel;
    Button msave;
    Uri imageUri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef, mUserRef;
    StorageReference storageRef;

    ProgressDialog mLoadingBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setup Profile");

        profileimageview = findViewById(R.id.profileImage);
        musername = findViewById(R.id.inputusernameprofile);
        mphone = findViewById(R.id.inputphone);
        mlocation = findViewById(R.id.inputLocation);
        mlevel = findViewById(R.id.inputlevel);
        msave = findViewById(R.id.buttonsave);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        mLoadingBar = new ProgressDialog(this);

        profileimageview.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });

        msave.setOnClickListener((View v) -> {
            saveProfile();
        });

//        Load profile
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String profileImage = snapshot.child("profileImage").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();
                    String location = snapshot.child("location").getValue().toString();
                    String level = snapshot.child("level").getValue().toString();

                    musername.setText(username);
                    mphone.setText(phone);
                    mlocation.setText(location);
                    mlevel.setText(level);
                    Picasso.get().load(profileImage).into(profileimageview);



                }else {
                    Toast.makeText(ProfileActivity.this, "No Profile Data Available..Please Upload Your Profile.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveProfile() {

        String username = musername.getText().toString();
        String phone = mphone.getText().toString();
        String location = mlocation.getText().toString();
        String level = mlevel.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            showError(musername, "Username is not valid");
        } else if (phone.isEmpty() || phone.length() < 10) {
            showError(mphone, "Phone Number is not valid");

        } else if (location.isEmpty() || location.length() < 3) {
            showError(mlocation, "Location is not valid");

        } else if (level.isEmpty() || level.length() < 3) {
            showError(mlevel, "Profession is not valid");
        } else if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        } else {

            mLoadingBar.setTitle("Saving Profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.setMessage("Please wait while saving profile");
            mLoadingBar.show();
            storageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("username", username);
                                hashMap.put("phone", phone);
                                hashMap.put("location", location);
                                hashMap.put("level", level);
                                hashMap.put("profileImage", uri.toString());

                                mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        mLoadingBar.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Profile Saved Successful.", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mLoadingBar.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Failed to Save!!. Try Again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }else {
                        Toast.makeText(ProfileActivity.this, "Something Went Wrong.Try Again!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveMembers();
    }

    private void retrieveMembers() {
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            profileimageview.setImageURI(imageUri);
        }
    }

}























