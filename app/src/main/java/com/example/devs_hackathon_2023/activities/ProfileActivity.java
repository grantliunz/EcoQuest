package com.example.devs_hackathon_2023.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.nio.ByteBuffer;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpProfile();

    }

     void setUpProfile() {
        setImageResource();
        TextView name = findViewById(R.id.username);
        TextView level = findViewById(R.id.level);
        name.setText(MainPlayer.getName());
        level.setText("Level " + MainPlayer.getLevel());
    }

    public void setImageResource() {
        ImageView profilePicture = findViewById(R.id.profilePicture);
        int image = MainPlayer.getProfilePicture();
        Bitmap circularBitmap = getCircularBitmapFromResourceId(image);
        profilePicture.setImageBitmap(circularBitmap);
    }

    private Bitmap getCircularBitmapFromResourceId(int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // Adjust the sample size as needed for resizing
        ImageView profilePicture = findViewById(R.id.profilePicture);

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
