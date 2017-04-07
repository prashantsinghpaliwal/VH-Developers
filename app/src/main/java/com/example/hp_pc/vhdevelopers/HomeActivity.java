package com.example.hp_pc.vhdevelopers;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView feedList;
    DatabaseReference mDataref;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    ProgressDialog load;
//    TextView title_, desc_;
//    ImageView post_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load = new ProgressDialog(this);
        load.setMessage("Refreshing Feeds...");
        load.setCanceledOnTouchOutside(false);
        load.show();
        mDataref = FirebaseDatabase.getInstance().getReference().child("Feed");
        mDataref.keepSynced(true);
        feedList = (RecyclerView) findViewById(R.id.feed_list);
        feedList.setLayoutManager(new LinearLayoutManager(this));
        feedList.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

//                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    Toast.makeText(HomeActivity.this, "Please Sign In ", Toast.LENGTH_SHORT).show();
                }
            }
        };
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PostActivity.class));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);

        FirebaseRecyclerAdapter<Feed, FeedHolder> adapter = new FirebaseRecyclerAdapter<Feed, FeedHolder>(
                Feed.class,
                R.layout.feed,
                FeedHolder.class,
                mDataref
        ) {

            @Override
            protected void populateViewHolder(FeedHolder viewHolder, Feed model, int position) {

                final String mTitle = model.getTitle();
                final String mDesc = model.getDesc();
                final String mImageUrl = model.getImage();
                viewHolder.setTitle(mTitle);
                viewHolder.setDesc(mDesc);
                viewHolder.setImage(getApplicationContext(), mImageUrl);

                load.dismiss();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView img = (ImageView) findViewById(R.id.feed_image);
                        TextView title_ = (TextView) findViewById(R.id.feed_title);
                        TextView desc_ = (TextView) findViewById(R.id.feed_desc);
                        Pair[] pair = new Pair[3];
                        pair[0] = new Pair<View, String>(img, "shared_image");
                        pair[1] = new Pair<View, String>(title_, "shared_title");
                        pair[2] = new Pair<View, String>(desc_, "shared_desc");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, pair);
                            Intent mainIntent = new Intent(HomeActivity.this, FeedDetails.class);
                            startActivity(mainIntent, options.toBundle());
                        } else {

                            Intent mainIntent = new Intent(HomeActivity.this, FeedDetails.class);
                            mainIntent.putExtra("shared_image_url", mImageUrl);
                            mainIntent.putExtra("shared_title", mTitle);
                            mainIntent.putExtra("shared_desc", mDesc);
                            startActivity(mainIntent);
                        }
                    }
                });

            }
        };
        feedList.setAdapter(adapter);

    }


    public static class FeedHolder extends RecyclerView.ViewHolder {

        View mView;
        Boolean isLiked = false;


        public FeedHolder(View itemView) {
            super(itemView);
            mView = itemView;
            final ImageButton likeButton = (ImageButton) mView.findViewById(R.id.like_button);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isLiked) {
                        likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isLiked = true;
                    } else if (isLiked) {
                        likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isLiked = false;
                    }
                }
            });
        }

        public void setTitle(String title) {
            TextView title_ = (TextView) mView.findViewById(R.id.feed_title);
            title_.setText(title);
        }

        public void setDesc(String desc) {
            TextView desc_ = (TextView) mView.findViewById(R.id.feed_desc);
            desc_.setText(desc);
        }

        public void setImage(Context applicationContext, String image) {
            //here final variables are created as tehy are to be used inside the inner methods
            final String img = image;
            final Context ctx = applicationContext;

            final ImageView post_image = (ImageView) mView.findViewById(R.id.feed_image);
            Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(img).into(post_image);
                }
            });


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {

        mAuth.signOut();


    }
}
