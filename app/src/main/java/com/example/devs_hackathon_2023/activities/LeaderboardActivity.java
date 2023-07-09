package com.example.devs_hackathon_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ListView;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.User.Player;
import com.example.devs_hackathon_2023.adaptors.LeaderboardAdapter;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private List<Integer> imageList = new ArrayList<Integer>(Arrays.asList(R.drawable.bill_icon, R.drawable.connor_icon, R.drawable.dhruv_icon, R.drawable.elon_icon, R.drawable.grant_icon, R.drawable.jordan_icon, R.drawable.leander_icon, R.drawable.zucc_icon, R.drawable.chatgpt, R.drawable.le, R.drawable.raymond));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().hide();
        generateListView();
    }

    private void generateListView() {
        List<Player> players = Database.getTopPlayers();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setProfilePicture(getAvatarIcon(imageList.get(i), 100, false));
        }
        Player mainPlayer = new Player(MainPlayer.getName(), MainPlayer.getId(), MainPlayer.getScore(), getAvatarIcon(R.drawable.aaron_icon, 100, false));
        players.add(mainPlayer);
        players.sort((o1, o2) -> o2.getScore() - o1.getScore());
        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(this, R.layout.leaderboard_tab, players);
        ListView listView = findViewById(R.id.leaderboardList);
        listView.setAdapter(leaderboardAdapter);


    }

    public Bitmap getAvatarIcon(int resourceId, int size, boolean hasBorder) {

        BitmapDescriptor customMarkerIcon = BitmapDescriptorFactory.fromResource(resourceId);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // Adjust the sample size as needed for resizing

        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);
        Bitmap resizedMarkerBitmap = Bitmap.createScaledBitmap(markerBitmap, size, size, false);

        // Create a circular mask
        Bitmap roundedMarkerBitmap = Bitmap.createBitmap(resizedMarkerBitmap.getWidth(),
                resizedMarkerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedMarkerBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, size, size);
        canvas.drawOval(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resizedMarkerBitmap, null, rect, paint);

        if (hasBorder) {
            // border options
            int borderWidth = 8; // Set the desired width of the border in pixels
            int borderColor = Color.rgb(210, 101, 15); // Set the desired color of the border
            // Draw a border around the marker icon
            Paint borderPaint = new Paint();
            borderPaint.setAntiAlias(true); // Apply anti-aliasing to the border
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setColor(borderColor);
            borderPaint.setDither(true); // Enable dithering for better color gradient

            // Adjust the position of the border based on the border width
            float borderOffset = borderWidth / 2f;
            RectF borderRect = new RectF(rect.left + borderOffset, rect.top + borderOffset, rect.right - borderOffset,
                    rect.bottom - borderOffset);
            canvas.drawOval(borderRect, borderPaint);
        }

        return roundedMarkerBitmap;
    }
}