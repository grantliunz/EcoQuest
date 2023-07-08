package com.example.devs_hackathon_2023;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.devs_hackathon_2023.activities.ShopActivity;
import com.example.devs_hackathon_2023.databinding.FragmentMapBinding;
import com.example.devs_hackathon_2023.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class Map extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    public class Coordinates {
        public double latitude;
        public double longitude;
    }

    private FragmentMapBinding binding;
    private GoogleMap map;
    private boolean permissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    protected View circleView;
    private View boundedBox;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        boundedBox = getView().findViewById(R.id.boundedBox);
        boundedBox.bringToFront();
        setupShopButton();

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
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
        onMyLocationButtonClick();

        // Move the camera to the user's location
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (lastKnownLocation != null) {
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f));
            }
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
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

        Coordinates currentLocation = getCurrentLocation();
        Log.d("TAG", "loc - lat: " + currentLocation.latitude + ", long: " + currentLocation.longitude);
        Coordinates targetLocation = new Coordinates();
        targetLocation.latitude = -36.8509;
        targetLocation.longitude = 174.7719;
//        Log.d("TAG", String.valueOf(areTwoCoordinatesClose(currentLocation, targetLocation)));
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d("TAG", "onMyLocationClick: " + location.getLatitude() + " " + location.getLongitude());

        Toast.makeText(requireContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)
                || PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ViewGroup) getActivity().getWindow().getDecorView()).removeView(circleView);
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(requireFragmentManager(), "dialog");
    }

    public Coordinates getCurrentLocation(){
        if (map != null) {
                    LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    if (lastKnownLocation != null) {
                        Coordinates coordinates = new Coordinates();
                        coordinates.latitude = lastKnownLocation.getLatitude();
                        coordinates.longitude = lastKnownLocation.getLongitude();
                        return coordinates;
                    }
                }
        return null;
    }

    public boolean areTwoCoordinatesClose(Coordinates first, Coordinates second){
        if(first == null || second == null){
            return false;
        }
        double distance = Math.sqrt(Math.pow(first.latitude - second.latitude, 2) + Math.pow(first.longitude - second.longitude, 2));
        return distance < 0.0001;
    }

    //user stuff
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


    private void animateAndSwitch(View view) {
        // Get the click position
        float startX = view.getX() + view.getWidth();
        float startY = view.getY() + view.getHeight() + 50;
        System.out.println("startX: " + startX);
        System.out.println("startY: " + startY);


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
                startPopUpAnimation();
            }
        });

        // Start the animation
        animator.start();

    }

    private void startPopUpAnimation() {
        Intent intent = new Intent(Map.this.requireContext(), ShopActivity.class);
        System.out.println("Starting ShopActivity");
        // Start the pop-up animation
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(Map.this.requireContext(), 0, R.anim.blow_up);
        startActivity(intent, options.toBundle());

    }
}

