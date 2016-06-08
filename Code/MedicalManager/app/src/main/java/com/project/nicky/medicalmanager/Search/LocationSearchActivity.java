package com.project.nicky.medicalmanager.Search;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Startup.BackgroundResponse;
import com.project.nicky.medicalmanager.Startup.BackgroundTask;
import com.project.nicky.medicalmanager.Startup.UserInformation;
import com.project.nicky.medicalmanager.Startup.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class LocationSearchActivity extends AppCompatActivity implements BackgroundResponse,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;
    private Button enterButton;
    private EditText addressField, distanceField;
    private RadioGroup addressRadioGroup;
    private BackgroundTask backgroundTask;
    private GoogleApiClient googleApiClient;
    private UserInformation userInformation;
    private String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        userInformation = new UserInformation(this);
        loggedInUser = userInformation.getId();

        googleApiClient = new GoogleApiClient.Builder(LocationSearchActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(LocationSearchActivity.this)
                .addOnConnectionFailedListener(LocationSearchActivity.this)
                .build();

        map  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng location = new LatLng(48.124873,4.1254583);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 3);
        map.animateCamera(update);
        map.setOnMarkerClickListener(this);
    }

    public void detailsButtonClicked(View view) {
        final Dialog dialog = new Dialog(LocationSearchActivity.this);
        dialog.setTitle("Enter details");
        dialog.setContentView(R.layout.dialog_map);
        dialog.show();

        addressField = (EditText)dialog.findViewById(R.id.addressField);
        addressRadioGroup = (RadioGroup)dialog.findViewById(R.id.addressRadioGroup);
        distanceField = (EditText)dialog.findViewById(R.id.distanceField);
        enterButton = (Button)dialog.findViewById(R.id.enterButton);

        addressRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.userAddress) {
                    UserInformation userInformation = new UserInformation(getApplicationContext());
                    String address = userInformation.getAddress();
                    if (address != null) {
                        addressField.setText(address);
                    } else {
                        Toast.makeText(getApplicationContext(), "Address unavailable. Please enter address manually", Toast.LENGTH_SHORT).show();
                    }
                } else if (checkedId == R.id.currentLocation) {
                    googleApiClient.connect();
                }
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validation = new Validation();
                validation.checkIfEmpty(addressField.getText().toString(), addressField, "Address cannot be empty");
                validation.checkIfEmpty(distanceField.getText().toString(), distanceField, "Distance cannot be empty");
                if (validation.counter == 0) {
                    double[] coords = geocodeLocation(addressField.getText().toString());
                    if (coords != null) {
                        LatLng location = new LatLng(coords[0], coords[1]);
                        int distance = Integer.parseInt(distanceField.getText().toString()) * 1000;
                        map.addMarker(new MarkerOptions()
                                .position(location)
                                .title("You are here")
                                .snippet(loggedInUser)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.here)));
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 9);
                        map.animateCamera(update);
                        map.addCircle(new CircleOptions()
                                .center(location)
                                .radius(distance)
                                .strokeColor(Color.BLUE));
                    }
                    String type = "getDoctors";
                    backgroundTask = new BackgroundTask(getApplicationContext());
                    backgroundTask.backgroundResponse = LocationSearchActivity.this;
                    backgroundTask.execute(type);
                    dialog.hide();
                }
            }
        });
    }

    public double[] geocodeLocation(String location){
        double[] coords = null;
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> list = geocoder.getFromLocationName(location, 1);
            if(list.size() > 0){
                Address address = list.get(0);
                coords = new double[2];
                coords[0] = address.getLatitude();
                coords[1] = address.getLongitude();
                return coords;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coords;
    }

    public String reverseGeocode(double lat, double lng){
        try{
            Geocoder geocoder = new Geocoder(this);
            List<Address> list = geocoder.getFromLocation(lat, lng, 1);
            if(list.size() > 0){
                Address geoAddress = list.get(0);
                String address = geoAddress.getLocality();
                return address;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.getSnippet().equals(loggedInUser)) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra("profileId", marker.getSnippet());
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void processResponse(String result) {
        if(result != null){
            try {
                JSONObject root = new JSONObject(result);
                JSONArray list = root.getJSONArray("result");
                for(int i = 0; i < list.length(); i++){
                    JSONObject listItem = list.getJSONObject(i);
                    String id = listItem.getString("id");
                    String firstName = listItem.getString("first_name");
                    String lastName = listItem.getString("last_name");
                    String addressLine = listItem.getString("address_line_1_2");
                    String city = listItem.getString("city");
                    String county = listItem.getString("county");
                    String country = listItem.getString("country");
                    String fullAddress  = addressLine + " " + city + " " + county + " " + country;
                    double[] coords = geocodeLocation(fullAddress);
                    if(coords != null){
                        map.addMarker(new MarkerOptions()
                            .position(new LatLng(coords[0], coords[1]))
                            .title(firstName + " " + lastName)
                            .snippet(id)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        double lat = currentLocation.getLatitude();
        double lng = currentLocation.getLongitude();
        String address = reverseGeocode(lat, lng);
        addressField.setText(address);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
    }
}
