package com.example.egifthouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity
{
    EditText Contact, City;
    FirebaseAuth mAuth;
    DatabaseReference UsersRef;
    String currentUserId,gender;
    ProgressDialog loadingBar;
    EditText textView;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Toast toast;

    CircleImageView setupProfileImage;
    Uri imageUri;
    StorageReference storagePicRef;
    String myUrl = "";
    String checker = "";
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        storagePicRef = FirebaseStorage.getInstance().getReference().child("ProfilePictures");

        Contact = (EditText) findViewById(R.id.et_phone);
        City = (EditText) findViewById(R.id.et_city);

        setupProfileImage = findViewById(R.id.setupProfileImage);
        setupProfileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SetupActivity.this);
            }
        });

        Toast toast = new Toast(this);

        textView = findViewById(R.id.genderTextView);
        radioGroup = findViewById(R.id.genderGroup);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            setupProfileImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error! Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SetupActivity.this,SetupActivity.class));
            finish();
        }
    }

    public void checkButtom(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        //toast = new Toast(this);
        toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
        textView.setText(radioButton.getText());
    }

    public void SaveDetailsToDB(View view)
    {
        String phone = Contact.getText().toString();
        String city = City.getText().toString();
        String gender = textView.getText().toString();

        if (TextUtils.isEmpty(phone) && (TextUtils.isEmpty(city)))
        {
            Toast.makeText(this, "Fields are empty.", Toast.LENGTH_SHORT).show();
        }
        if (phone.length()!=10)
        {
            Toast.makeText(this, "Invalid Contact.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (imageUri != null)
            {
                final StorageReference fileref = storagePicRef.child(currentUserId + ".jpg");
                uploadTask = fileref.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return fileref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                            HashMap<String, Object> userMapImg = new HashMap<String, Object>();
                            userMapImg.put("image",myUrl);
                            ref.updateChildren(userMapImg);
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            }

            HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("Phone",phone);
            userMap.put("City",city);
            userMap.put("Gender",gender);
            userMap.put("uid",currentUserId);
            /*userMap.put("image",myUrl);*/
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SetupActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SetupActivity.this,MainActivity.class));
                        finish();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String msg = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error! " + msg, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}
