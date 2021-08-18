package com.tony.aipca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tony.aipca.Utils.CommitteeMembers;
import com.tony.aipca.Utils.MemberModel;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommitteeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private RecyclerView recyclerView;
    private CircleImageView comMemPhoto;
    private EditText comMName, comMPosition, comMPhone, comMLocation;
    private Button comMPhotoSelect, comMSave, comMCancel;
    private FloatingActionButton addmember;
    private Uri imageUri;
    ProgressDialog LoadingBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mMemberRef;
    private StorageReference mMemRef;

    private DatabaseReference retrieveMember;
    private MembersAdapter membersAdapter;
    private ArrayList<MemberModel> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee);

        mMemberRef = FirebaseDatabase.getInstance().getReference().child("CommitteeMembers");
        mMemRef = FirebaseStorage.getInstance().getReference("MembersImages");
        retrieveMember = FirebaseDatabase.getInstance().getReference().child("CommitteeMembers");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.committeerecyclerview);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        membersAdapter = new MembersAdapter(this ,list );

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(membersAdapter);

        LoadingBar = new ProgressDialog(this);

        String userPhone = mUser.getPhoneNumber();
        if (userPhone.equals("0722114014")){
            addmember.setVisibility(View.INVISIBLE);
        }else {
            addmember.setVisibility(View.VISIBLE);
        }

        retrieveMember.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MemberModel model = dataSnapshot.getValue(MemberModel.class);
                    list.add(model);
                }
                membersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//    public void createMemberDialog(){
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View memberView = getLayoutInflater().inflate(R.layout.addcommember, null);
//        comMName = memberView.findViewById(R.id.addmem_name);
//        comMPosition = memberView.findViewById(R.id.addmem_position);
//        comMPhone = memberView.findViewById(R.id.addmem_phone);
//        comMLocation = memberView.findViewById(R.id.addmem_location);
//        comMSave = memberView.findViewById(R.id.btnmem_save);
//        comMCancel = memberView.findViewById(R.id.btnmem_cancel);
//        comMemPhoto = memberView.findViewById(R.id.addmem_imageview);
//        comMPhotoSelect = memberView.findViewById(R.id.btnselectphoto);
//
//
//
//        dialogBuilder.setView(memberView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        comMCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//
//        comMPhotoSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });
//        comMSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (imageUri != null){
//                    saveCommitteeMember(imageUri);
//                }else{
//                    Toast.makeText(CommitteeActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }

//    private void saveCommitteeMember(Uri uri) {
//        String membername = comMName.getText().toString();
//        String memberposition = comMPosition.getText().toString();
//        String memberphone = comMPhone.getText().toString();
//        String memberlocation = comMLocation.getText().toString();
//
//        if (membername.isEmpty() || membername.length() < 3){
//            showError(comMName, "Name is not valid!! Must be more then 3 characters.");
//        }else  if (memberposition.isEmpty()){
//            showError(comMPosition, "Position can not be null");
//        }else if (memberphone.isEmpty() || memberphone.length()<10){
//            showError(comMPhone, "Phone number not valid!!");
//        }else if (memberlocation.isEmpty()){
//            showError(comMLocation, "Location cannot be null!!");
//        }else if (imageUri == null){
//            Toast.makeText(this, "Please Select an Image!!", Toast.LENGTH_SHORT).show();
//        }else {
//            LoadingBar.setTitle("Saving Member");
//            LoadingBar.setMessage("Please Wait a Moment.");
//            LoadingBar.setCanceledOnTouchOutside(false);
//            LoadingBar.show();
//
//            final StorageReference fileRef = mMemRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            MemberModel memberModel = new MemberModel(membername, memberposition, memberphone, memberlocation, uri.toString());
//                            String modelId = mMemberRef.push().getKey();
//                            mMemberRef.child(modelId).setValue(memberModel);
//                            LoadingBar.dismiss();
//                            comMName.setText("");
//                            comMPosition.setText("");
//                            comMPhone.setText("");
//                            comMLocation.setText("");
//                            comMemPhoto.setImageURI(null);
//                            Toast.makeText(CommitteeActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    LoadingBar.dismiss();
//                    Toast.makeText(CommitteeActivity.this, "Something Went Wrong.Please try Again!!", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }

//    private void showError(EditText input, String s) {
//        input.setError(s);
//        input.requestFocus();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null){
//            imageUri = data.getData();
//            comMemPhoto.setImageURI(imageUri);
//        }
//    }
//
//    private String getFileExtension(Uri mUri){
//        ContentResolver cr = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(mUri));
//
//    }
}