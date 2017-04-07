package com.example.hp_pc.vhdevelopers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText title,desc;
    private ImageButton postImage;
    private  Button submitPost;
    private int REQUEST_CODE=1;
    private Uri resultUri =null;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mStorageReference= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Feed");
        mProgress=new ProgressDialog(this);
        title= (EditText) findViewById(R.id.post_title);
        desc= (EditText) findViewById(R.id.post_desc);
        postImage= (ImageButton) findViewById(R.id.choose_img);
        submitPost= (Button) findViewById(R.id.submit_post);
        postImage.setOnClickListener(this);
        submitPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.choose_img:
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,REQUEST_CODE);
                break;
            case R.id.submit_post:
                postToFeeds();
                break;
        }
    }

    private void postToFeeds() {
        final String title_val=title.getText().toString().trim();
        final String desc_val=desc.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && resultUri!=null){
            mProgress.setMessage("Posting to feeds...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.show();
            StorageReference imgFilePath=mStorageReference.child("Feed_Images").child(resultUri.getLastPathSegment());
            imgFilePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPostLink=mDatabase.push();
                    newPostLink.child("title").setValue(title_val);
                    newPostLink.child("desc").setValue(desc_val);
                    assert downloadUrl != null;
                    newPostLink.child("image").setValue(downloadUrl.toString());
                    mProgress.dismiss();
                    Intent mainIntent=new Intent(PostActivity.this,HomeActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE & resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(2,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                postImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

}
