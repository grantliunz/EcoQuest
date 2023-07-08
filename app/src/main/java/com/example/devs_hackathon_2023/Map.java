package com.example.devs_hackathon_2023;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.activities.ProfileActivity;
import com.example.devs_hackathon_2023.activities.ShopActivity;
import com.example.devs_hackathon_2023.databinding.FragmentMapBinding;
import com.example.devs_hackathon_2023.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;


public class Map extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    public class Coordinates {
        public double latitude;
        public double longitude;

        Coordinates(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public class Friend {
        public String name;
        public Coordinates location;
        public int icon;

        Friend(String name, Coordinates coordinate, int icon){
            this.name = name;
            this.location = coordinate;
            this.icon = icon;
        }
    }

    private FragmentMapBinding binding;
    private GoogleMap map;
    private boolean permissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    protected View circleView;
    private View boundedBox;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private Coordinates targetLocation;
    private ArrayList<Friend> friends; // temp type


    public void setTargetLocation(double latitude, double longitude){
        this.targetLocation = new Coordinates(latitude, longitude);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView myLocation = view.findViewById(R.id.myLocationView);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your function here
                onMyLocationButtonClick();
            }
        });
        myLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RelativeLayout myLocationBackdrop = view.findViewById(R.id.myLocationViewBackdrop);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Apply a darker color filter when the image is touched
                        myLocationBackdrop.setAlpha(0.9f); // Adjust the alpha (transparency) value as needed
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Remove the color filter when the touch is released or canceled
                        myLocationBackdrop.setAlpha(0.6f);
                        break;
                }
                return false;
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        friends = new ArrayList<Friend>();
        friends.add(new Friend("Dhruv", new Coordinates(-36.8509, 174.7719), R.drawable.dhruv_icon));
        friends.add(new Friend("Aaron", new Coordinates( -36.8513, 174.7711), R.drawable.aaron_icon));
        locationCallback = new LocationCallback() {
            // callback whenever location updates
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        currentLocation = location;
//                        Log.d("TAG", "loc: lat " + currentLocation.getLatitude() + ", long " + currentLocation.getLongitude());
                        ArrayList<MarkerOptions> markers = new ArrayList<>();
                        // update user avatar
                        LatLng myMarkerLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        BitmapDescriptor myIcon = getAvatarIcon(R.drawable.connor_icon, 150, true);
                        MarkerOptions myMarkerOptions = new MarkerOptions()
                                .position(myMarkerLocation)
                                .title("Connor")
                                .icon(myIcon).zIndex(1);
                        markers.add(myMarkerOptions);
                        // update friends avatars
                        for (Friend friend : friends){
                            // add a marker
                            LatLng markerLocation = new LatLng(friend.location.latitude, friend.location.longitude);
                            BitmapDescriptor icon = getAvatarIcon(friend.icon, 150, false);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(markerLocation)
                                    .title(friend.name)
                                    .icon(icon);
                           markers.add(markerOptions);
                        }
                        map.clear();
                        for (MarkerOptions marker : markers){
                            map.addMarker(marker);
                        }
                        moveFriendsRandomly();
                        if(targetLocation != null){
                            // check if current location is close to target location
//                            Log.d("TAG", String.valueOf(areTwoCoordinatesClose(new Coordinates(currentLocation.getLatitude(), currentLocation.getLongitude()), targetLocation)));
                        }
                    }
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        boundedBox = getView().findViewById(R.id.boundedBox);
        boundedBox.bringToFront();
        setupShopButton();

        setUpProfileButton();

        loadProfile();

    }

    public void moveFriendsRandomly(){
        Random r = new Random();
        for (Friend friend : friends){
            double dir = r.nextDouble();
            if (dir < 0.25){
                friend.location.latitude += 0.0001;
            } else if (dir < 0.5){
                friend.location.longitude += 0.0001;
            } else if (dir < 0.75){
                friend.location.latitude -= 0.0001;
            } else {
                friend.location.longitude -= 0.0001;
            }
        }
    }
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).setMinUpdateIntervalMillis(500).setMaxUpdateDelayMillis(1500).build();

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_theme
                )
        );
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        onMyLocationButtonClick();
        startLocationUpdates();

        // Move the camera to the user's location
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (lastKnownLocation != null) {
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
            }
        }
    }

    public BitmapDescriptor getAvatarIcon(int resourceId, int size, boolean hasBorder){

        BitmapDescriptor customMarkerIcon = BitmapDescriptorFactory.fromResource(resourceId);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // Adjust the sample size as needed for resizing

        Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);
        Bitmap resizedMarkerBitmap = Bitmap.createScaledBitmap(markerBitmap, size, size, false);


        // Create a circular mask
        Bitmap roundedMarkerBitmap = Bitmap.createBitmap(resizedMarkerBitmap.getWidth(), resizedMarkerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedMarkerBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, size, size);
        canvas.drawOval(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resizedMarkerBitmap, null, rect, paint);

        if (hasBorder){
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
            RectF borderRect = new RectF(rect.left + borderOffset, rect.top + borderOffset, rect.right - borderOffset, rect.bottom - borderOffset);
            canvas.drawOval(borderRect, borderPaint);
        }

        return BitmapDescriptorFactory.fromBitmap(roundedMarkerBitmap);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            map.setMyLocationEnabled(true);
            return;
        }

        PermissionUtils.requestLocationPermissions((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (map != null) {
            LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (lastKnownLocation != null) {
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
            }
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ViewGroup) getActivity().getWindow().getDecorView()).removeView(circleView);
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
        startLocationUpdates();
    }
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(requireFragmentManager(), "dialog");
    }

    public boolean areTwoCoordinatesClose(Coordinates first, Coordinates second){
        if(first == null || second == null){
            return false;
        }
        double distance = Math.sqrt(Math.pow(first.latitude - second.latitude, 2) + Math.pow(first.longitude - second.longitude, 2));
        return distance < 0.0003;
    }

    //user stuff

    private void setUpProfileButton() {
        RelativeLayout layout = getView().findViewById(R.id.profileButton);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                animateAndSwitch(v);
            }
        });
    }

    private void setupShopButton() {
        ImageView clickableImageView = getView().findViewById(R.id.shopButton);
        clickableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                animateAndSwitch(v);
            }
        });
    }


    private void loadProfile() {
        ImageView profilePicture = getView().findViewById(R.id.mapProfilePicture);
        profilePicture.setImageResource(MainPlayer.getProfilePicture());
        TextView username = getView().findViewById(R.id.mapUserName);
        username.setText(MainPlayer.getName());
        TextView level = getView().findViewById(R.id.mapLevel);
        level.setText(String.format("%d", MainPlayer.getLevel()));
    }

    private void animateAndSwitch(View view) {
        // Get the click position
        int viewId = view.getId();

        int shop = getView().findViewById(R.id.shopButton).getId();
        int profile = getView().findViewById(R.id.profileButton).getId();
        float startX = 0, startY = 0;
        if (viewId == profile) {
            startX = 120;
            startY = 110;
        }

        else if (viewId == shop) {
            startX = 655;
            startY = 120;
        }


        circleView = new View(Map.this.requireContext());

        // Create a circular view to animate
        circleView.setLayoutParams(new ViewGroup.LayoutParams(3000, 3000));
        circleView.setBackground(ContextCompat.getDrawable(Map.this.requireContext(), R.drawable.circle_shape));
        circleView.setX(0);
        circleView.setY(view.getHeight());
        ((ViewGroup) getActivity().getWindow().getDecorView()).addView(circleView);

        // Define the animation
        float endRadius = 600;
        Animator animator = ViewAnimationUtils.createCircularReveal(circleView, (int) startX, (int) startY, 30, 2900);
        animator.setDuration(800);

        // Set an animation listener
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
                // Remove the circular view

                // Start the pop-up animation
                startPopUpAnimation(viewId);
            }
        });

        // Start the animation
        animator.start();

    }

    private void startPopUpAnimation(int viewId) {
        int shop = getView().findViewById(R.id.shopButton).getId();
        int profile = getView().findViewById(R.id.profileButton).getId();

        if (viewId == profile) {
            startProfileActivity();
        }

        else if (viewId == shop) {
            startShopActivity();
        }

    }

    private void startProfileActivity() {
        Intent intent = new Intent(Map.this.requireContext(), ProfileActivity.class);
        System.out.println("Starting ProfileActivity");
        // Start the pop-up animation
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(Map.this.requireContext(), 0, R.anim.blow_up);
        startActivity(intent, options.toBundle());
    }

    private void startShopActivity() {
        Intent intent = new Intent(Map.this.requireContext(), ShopActivity.class);
        System.out.println("Starting ShopActivity");
        // Start the pop-up animation
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(Map.this.requireContext(), 0, R.anim.blow_up);
        startActivity(intent, options.toBundle());
    }
}

