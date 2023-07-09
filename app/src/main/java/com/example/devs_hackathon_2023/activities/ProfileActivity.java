package com.example.devs_hackathon_2023.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.User.Stat;
import com.example.devs_hackathon_2023.adaptors.ProfileAdaptor;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpProfile();

        // Create a list of Stat objects
        List<Stat> statList = new ArrayList<>();
        statList.add(new Stat("Distance Walked", String.valueOf(MainPlayer.getDistanceWalked())));
        statList.add(new Stat("Steps Taken", String.valueOf(MainPlayer.getSteps())));
        statList.add(new Stat("Landmarks Visited", String.valueOf(MainPlayer.getLandmarksVisited())));
        statList.add(new Stat("Quests Completed", String.valueOf(MainPlayer.getCompleted())));
        // Add more stats as needed

        // Create the StatsAdapter and pass the statList to it
        ProfileAdaptor statsAdapter = new ProfileAdaptor(this, statList);

        // Find the GridView in the layout
        GridView statsGridView = findViewById(R.id.statsGridView);

        // Set the adapter to the GridView
        statsGridView.setAdapter(statsAdapter);

        LinearProgressIndicator progressIndicator = findViewById(R.id.myProgressIndicator);
        int progress = MainPlayer.getXp(); // Get the progress value from MainPlayer or any desired source
        progressIndicator.setProgressCompat(progress, true);

    }

    private void setUpProfile() {
        setImageResource();
        TextView name = findViewById(R.id.username);
        TextView level = findViewById(R.id.level);
        name.setText(MainPlayer.getName());
        level.setText("Level " + MainPlayer.getLevel());
    }

    private void setImageResource() {
        ImageView profilePicture = findViewById(R.id.profilePicture);
        int image = MainPlayer.getProfilePicture();
        Bitmap circularBitmap = getCircularBitmapFromResourceId(image);
        profilePicture.setImageBitmap(circularBitmap);
    }

    private Bitmap getCircularBitmapFromResourceId(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // Adjust the sample size as needed for resizing

        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);
        Bitmap resizedMarkerBitmap = Bitmap.createScaledBitmap(markerBitmap, 160, 160, false);

        // Create a circular mask
        Bitmap circularBitmap = Bitmap.createBitmap(resizedMarkerBitmap.getWidth(), resizedMarkerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circularBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, resizedMarkerBitmap.getWidth(), resizedMarkerBitmap.getHeight());
        canvas.drawOval(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resizedMarkerBitmap, null, rect, paint);

        return circularBitmap;
    }
}
