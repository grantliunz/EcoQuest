package com.example.devs_hackathon_2023.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.devs_hackathon_2023.Quest.Quest;
import com.example.devs_hackathon_2023.R;
import com.example.devs_hackathon_2023.User.Database;
import com.example.devs_hackathon_2023.User.MainPlayer;
import com.example.devs_hackathon_2023.User.Player;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class Map extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener
        {
    public class Coordinates {
        public double latitude;
        public double longitude;

        Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private final int THRESHOLD_DISTANCE = 1;
    private FragmentMapBinding binding;
    private GoogleMap map;
    private boolean permissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    protected View circleView;
    private View boundedBox;

    private Location previousLocation = null;

    private Polyline polyline;
    private boolean isCanceled = false;
    private PolylineOptions currPolylineOptions;

    private int distanceToXp = 0;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private Coordinates targetLocation;
    private Handler delayedHandler;

    public void setTargetLocation(double latitude, double longitude){
        this.targetLocation = new Coordinates(latitude, longitude);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = FragmentMapBinding.inflate(inflater, container, false).getRoot();

        ImageView cameraButton = view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call your function here
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] { Manifest.permission.CAMERA }, 1);
                }
            }
        });

        return view;
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
        locationCallback = new LocationCallback() {
            // callback whenever location updates
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        currentLocation = location;
//                        Log.d("TAG", "loc: lat " + currentLocation.getLatitude() + ", long " + currentLocation.getLongitude());
                        updateIcons();
                        System.out.println("Location: " + location.getLatitude() + ", " + location.getLongitude());

                        while (distanceToXp >= 100) {
                            MainPlayer.setScore(MainPlayer.getScore() + 10);
                            distanceToXp -= 100;
                        }

                        loadProfile();


                        if (previousLocation != null) {
                            System.out.println("Previous Location: " + previousLocation.getLatitude() + ", " + previousLocation.getLongitude());
                        }
                        // Increment step count when location changes
                        if (previousLocation != null) {
                            if (isLocationChanged(location)) {
                                double distance_between = location.distanceTo(previousLocation);
                                System.out.println("distance between: " + distance_between);
                                MainPlayer.setSteps(MainPlayer.getSteps() + (int) (distance_between / 0.8142));
                                distanceToXp += (int) distance_between;
                                MainPlayer.setDistanceWalked(MainPlayer.getDistanceWalked() + (int) distance_between);

                            }

                        }
                        previousLocation = location;
                        // update quests
                        MainPlayer.updateQuests(new com.example.devs_hackathon_2023.User.Location(
                                currentLocation.getLatitude(), currentLocation.getLongitude()));

                        updatePolyline();
                        if (targetLocation != null) {
                            // check if current location is close to target location
                            // Log.d("TAG", String.valueOf(areTwoCoordinatesClose(new
                            // Coordinates(currentLocation.getLatitude(), currentLocation.getLongitude()),
                            // targetLocation)));
                        }
                        addLocationMarkers();
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

    private void updateIcons () {
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        // update user avatar
        LatLng myMarkerLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        BitmapDescriptor myIcon = getAvatarIcon(MainPlayer.getProfilePicture(), 150, true);
        MarkerOptions myMarkerOptions = new MarkerOptions()
                .position(myMarkerLocation)
                .title(MainPlayer.getName())
                .icon(myIcon).zIndex(1);
        markers.add(myMarkerOptions);
        // update friends avatars
        List<Player> players = Database.getPlayers().subList(0,30);
        for (Player friend : players){
            // add a marker
            LatLng markerLocation = new LatLng(friend.getLocation().getLatitude(), friend.getLocation().getLongitude());
            BitmapDescriptor icon = getAvatarIcon(R.drawable.pfp, 150, false);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(markerLocation)
                    .title(friend.getName())
                    .icon(icon);
            markers.add(markerOptions);
        }
        map.clear();
        for (MarkerOptions marker : markers){
            map.addMarker(marker);
        }
    }
    private void addLocationMarkers() {
        BitmapDescriptor myIcon = getAvatarIcon(R.drawable.pin, 100, false);

        for (Quest quest : MainPlayer.getQuests()) {
            if (quest.getQuestLoc() == null)
                continue;
            double lat = quest.getQuestLoc().getLatitude();
            double lon = quest.getQuestLoc().getLongitude();
            System.out.println(quest.getTitle() + " " + lat + " " + lon);

            map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(quest.getTitle()).icon(myIcon));
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5000).setMinUpdateIntervalMillis(3000).setMaxUpdateDelayMillis(7000).build();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private boolean isLocationChanged(Location newLocation) {
        // Compare the new location with the previous location or any other logic as
        // needed
        // Return true if the location has changed, otherwise false

        // Example logic: Check if the distance between the new location and previous
        // location is significant
        if (previousLocation != newLocation) {
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_theme));
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraIdleListener(this);
        enableMyLocation();
        onMyLocationButtonClick();
        startLocationUpdates();

        // Move the camera to the user's location
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) requireContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location lastKnownLocation = locationManager
                    .getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (lastKnownLocation != null) {
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
            }
        }

    }

    public BitmapDescriptor getAvatarIcon(int resourceId, int size, boolean hasBorder) {

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

        return BitmapDescriptorFactory.fromBitmap(roundedMarkerBitmap);
    }

    /**
     * Enables the My Location layer if the fine location permission has been
     * granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // map.setMyLocationEnabled(true);
            return;
        }
        PermissionUtils.requestLocationPermissions((AppCompatActivity) requireActivity(), LOCATION_PERMISSION_REQUEST_CODE, true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (map != null) {
            LocationManager locationManager = (LocationManager) requireContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            Location lastKnownLocation = locationManager
                    .getLastKnownLocation(locationManager.getBestProvider(criteria, false));
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
        LinearProgressIndicator progressIndicator = getView().findViewById(R.id.myProgressIndicator);
        int progress = MainPlayer.getXp(); // Get the progress value from MainPlayer or any desired source
        progressIndicator.setProgressCompat(progress, true);
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
                // super.onAnimationEnd(animation);
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
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(Map.this.requireContext(), 0,
                R.anim.blow_up);
        startActivity(intent, options.toBundle());
    }

    private void startShopActivity() {
        Intent intent = new Intent(Map.this.requireContext(), ShopActivity.class);
        System.out.println("Starting ShopActivity");
        // Start the pop-up animation
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(Map.this.requireContext(), 0,
                R.anim.blow_up);
        startActivity(intent, options.toBundle());
    }

    private void drawRoute(LatLng origin, LatLng destination) throws PackageManager.NameNotFoundException {
        Context appContext = requireActivity().getApplicationContext();
        ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(),
                PackageManager.GET_META_DATA);
        Bundle metaData = ai.metaData;
        String apiKey = metaData.getString("com.google.android.geo.API_KEY");

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.WALKING)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));

        try {
            DirectionsResult result = request.await();

            // Process the directions result
            if (result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                // Retrieve the encoded polyline representing the route
                String encodedPolyline = route.overviewPolyline.getEncodedPath();
                // Decode the polyline into a list of LatLng points
                List<LatLng> decodedPolyline = PolyUtil.decode(encodedPolyline);
                // Add the polyline to the map
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(decodedPolyline)
                        .color(Color.BLUE)
                        .width(8f);
                polyline = map.addPolyline(polylineOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    private void updatePolyline() {
        if (currentLocation == null)
            return;
        LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        // -36.8570, 174.7650
        LatLng destination = new LatLng(-36.8570, 174.7650);
        try {
            drawRoute(origin, destination);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onCameraMoveStarted(int reason) {
        // This method will be called when the camera starts to move
        // Handle the camera move event and call your function here
        if (delayedHandler != null){
            delayedHandler.removeCallbacksAndMessages(null);
        }
        stopLocationUpdates();
    }

    @Override
    public void onCameraIdle() {
        // This method will be called when the camera movement has ended
        // Handle the camera idle event and call your function here
        delayedHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Code to be executed after the delay
                startLocationUpdates();
            }
        };

        delayedHandler.postDelayed(runnable, 1500);
    }

}
