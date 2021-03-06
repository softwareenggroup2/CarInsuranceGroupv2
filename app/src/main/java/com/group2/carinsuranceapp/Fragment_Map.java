package com.group2.carinsuranceapp;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class Fragment_Map extends Fragment implements OnMapReadyCallback {


    private GoogleMap map;
    MapView mMapView;
    View mView;
    LoggedInMainActivity loggedInMainActivity;
    Location location;
    LatLng currentLocationLatLng;
    String currentAddress;
    private TextView incidentLocationAsCurrentLocation;
    private TextView addressView;

    private ToggleButton button;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(R.layout.fragment_map,null);
    return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressView = view.findViewById(R.id.text_address_in_map_fragment);
        button = view.findViewById(R.id.button_use_address_in_new_incident);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(button.isChecked()){
                loggedInMainActivity.setUseAddressFromMapFragment(true);
                Toast.makeText(getActivity(),"Will use this location",Toast.LENGTH_LONG).show();
            }else{
                loggedInMainActivity.setUseAddressFromMapFragment(false);
                Toast.makeText(getActivity(),"Will not use this location",Toast.LENGTH_LONG).show();
            }
            }
        });

        location = new Location("");
        mMapView = view.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        //device location
        loggedInMainActivity = (LoggedInMainActivity) getActivity();
        location = loggedInMainActivity.lastKnownLocation;
        currentLocationLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        loggedInMainActivity.setUseAddressFromMapFragment(false);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(currentLocationLatLng).title("You are here"));
        map.moveCamera(CameraUpdateFactory.zoomTo(8.0f));
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLng));
        //set variables in LoggedInMain
        loggedInMainActivity.setLatlngFromMapFragment(currentLocationLatLng);
        try {
            loggedInMainActivity.updateAddressFromMapFragment(currentLocationLatLng);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set address view to selected address
        addressView.setText(loggedInMainActivity.getAddressFromMapFragment());

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title("You are here"));

                currentLocationLatLng = latLng;

                //set variables in LoggedInMain
                loggedInMainActivity.setLatlngFromMapFragment(latLng);
                try {
                    loggedInMainActivity.updateAddressFromMapFragment(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //set address view to selected address
                addressView.setText(loggedInMainActivity.getAddressFromMapFragment());
            }
        });

    }
}

/*loggedInMainActivity.setLatlngFromMapFragment(latLng);
                try {
                    loggedInMainActivity.updateAddress(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentAddress = loggedInMainActivity.getCurrentAddress();
                addressView.setText(currentAddress);*/