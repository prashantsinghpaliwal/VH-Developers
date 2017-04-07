package com.example.hp_pc.vhdevelopers;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FeedDetails extends AppCompatActivity {

    ImageView img;
    ImageView likeBtn;
    TextView mTitle,mDesc;
    Boolean isLiked=false;

    @Override
    public boolean onSupportNavigateUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
        img= (ImageView) findViewById(R.id.feed_image_details);
        mTitle= (TextView) findViewById(R.id.feed_title_details);
        mDesc= (TextView) findViewById(R.id.feed_desc_details);
        likeBtn= (ImageView) findViewById(R.id.like_button_details);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLiked){
                    isLiked=true;
                    likeBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                else{
                    isLiked=false;
                    likeBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });
//        Intent intent=getIntent();
//        mTitle.setText(intent.getStringExtra("title"));
//        mDesc.setText(intent.getStringExtra("desc"));
//        Picasso.with(FeedDetails.this).load(intent.getStringExtra("imageUrl")).into(img);


    }
}
