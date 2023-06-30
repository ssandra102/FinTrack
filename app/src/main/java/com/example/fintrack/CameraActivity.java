package com.example.fintrack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;


public class CameraActivity extends AppCompatActivity {

    private Button btnChoose,btnUpload, viewResults;
    private ImageView imageView;
    private ImageView imageView2;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private TextView gotoUser;

    //Firebase

    FirebaseStorage storage;
    StorageReference storageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        gotoUser = (TextView) findViewById(R.id.goToUser);
        imageView = (ImageView) findViewById(R.id.imgView);
        imageView2 = (ImageView) findViewById(R.id.imgView2);
        viewResults = (Button) findViewById(R.id.viewResults);

//        imgFirebase = (ImageView) findViewById(R.id.imgFirebase);

        //Firebase init
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        viewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { navigatetoResultsActivity(); }
        });

        gotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CameraActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    private void navigatetoResultsActivity() {
        finish();
        Intent intent = new Intent(CameraActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

    private void uploadImage() {
        if(filePath != null){
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Uploading....");
            progress.show();

            StorageReference ref= storageReference.child( "receipt" + ".jpg");
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @NonNull
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(CameraActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    String imageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    Picasso.with(getBaseContext()).load(imageUrl).into(imageView2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Toast.makeText(CameraActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progres_time = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progress.setMessage("Uploaded "+(int)progres_time+" %");
                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}