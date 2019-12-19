package com.example.talentplusapplication;

import android.content.Intent;
import android.os.Bundle;

import com.example.talentplusapplication.main.profile.ProfileActivity;
import com.example.talentplusapplication.video.VideoListActivity;
import com.example.talentplusapplication.video.adapter.VideoPlayAdapter;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "NDA";
    VideoView video_one;
//    VideoView video_two;

    ImageView play_one;
//    ImageView play_two;

    RecyclerView mRecyclerView;

    ImageView imageView_profile;
    TextView txtUsername, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        intialization();
    }


    private void intialization() {


        /*video_one=findViewById(R.id.videoview_one);
        play_one=findViewById(R.id.playbutton_one);

        video_one.setVideoPath("https://www.ebookfrenzy.com/android_book/movie.mp4");
        video_one.seekTo(1);


        play_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                video_one.start();
                play_one.setVisibility(View.GONE);

            }
        });*/

        imageView_profile = findViewById(R.id.imageView_navigation_drawer);
        txtUsername = findViewById(R.id.txt_name_navigation_drawer);
        txtEmail = findViewById(R.id.txt_mail_navigation_drawer);
        /*set profile data*/

        getUserProfile(AccessToken.getCurrentAccessToken());


        ArrayList<String> videoUrlList = new ArrayList<>();
        videoUrlList.add("http://androhub.com/demo/demo.mp4");
        videoUrlList.add("https://www.demonuts.com/Demonuts/smallvideo.mp4");
        videoUrlList.add("https://www.demonuts.com/Demonuts/smallvideo.mp4");
        mRecyclerView = findViewById(R.id.recycler_view_video_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoPlayAdapter mVideoPlayAdapter = new VideoPlayAdapter(this, videoUrlList);
        mRecyclerView.setAdapter(mVideoPlayAdapter);


    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, object.toString());

                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
//                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText("");
//                            Picasso.with(NavigationDrawerActivity.this).load(image_url).into(imageView_profile);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent mIntent = new Intent(NavigationDrawerActivity.this, ProfileActivity.class);
            startActivity(mIntent);
        } else if (id == R.id.nav_gallery) {
            Intent mIntent = new Intent(NavigationDrawerActivity.this, VideoListActivity.class);
            startActivity(mIntent);

        } else if (id == R.id.nav_my_feed) {
//            Intent mIntent = new Intent(NavigationDrawerActivity.this, VideoWithWebview.class);
//            startActivity(mIntent);



        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {


            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "https://www.google.com/";
            String shareSub = "Talent Plus";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub);
            startActivity(Intent.createChooser(myIntent, "Share using"));

        } else if (id == R.id.nav_help_support) {

        } else if (id == R.id.nav_logout) {

            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
