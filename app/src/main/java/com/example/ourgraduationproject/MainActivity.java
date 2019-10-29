package com.example.ourgraduationproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener
        , PermissionsListener, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private boolean TrafficFlag1, TrafficFlag2, TrafficFlag3, TrafficFlag4, TrafficFlag5, TrafficFlag6;
    private DatabaseReference myRef;
    private FirebaseUser user;
    violationClass violationClass;
    CountDownTimer Timer, timer;
    NotificationCompat.Builder mBuilder;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuilder = new NotificationCompat.Builder(this);
        TrafficFlag1 = false;
        TrafficFlag2 = true;
        TrafficFlag3 = false;
        TrafficFlag4 = true;
        TrafficFlag5 = false;
        TrafficFlag6 = false;
        Timer = null;
        Timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (TrafficFlag1 == true) {
                    TrafficFlag1 = false;
                } else if (TrafficFlag1 == false) {
                    TrafficFlag1 = true;
                }
                if (TrafficFlag2 == true) {
                    TrafficFlag2 = false;
                } else if (TrafficFlag2 == false) {
                    TrafficFlag2 = true;
                }
                if (TrafficFlag3 == true) {
                    TrafficFlag3 = false;
                } else if (TrafficFlag3 == false) {
                    TrafficFlag3 = true;
                }
                if (TrafficFlag4 == true) {
                    TrafficFlag4 = false;
                } else if (TrafficFlag4 == false) {
                    TrafficFlag4 = true;
                }
                if (TrafficFlag5 == true) {
                    TrafficFlag5 = false;
                } else if (TrafficFlag5 == false) {
                    TrafficFlag5 = true;
                }
                if (TrafficFlag6 == true) {
                    TrafficFlag6 = false;
                } else if (TrafficFlag6 == false) {
                    TrafficFlag6 = true;
                }
                Toast.makeText(getApplicationContext(), " " + TrafficFlag1 + " , " + TrafficFlag2 + " , " +
                        TrafficFlag3 + " , " + TrafficFlag4 + " , " + TrafficFlag5 +
                        " , " + TrafficFlag6, Toast.LENGTH_LONG).show();
                Timer.start();
            }
        }.start();

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        startButton = (Button) findViewById(R.id.startButton);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        violationClass = new violationClass();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        myRef = database.getReference("users").child(id).child("Violations");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .origin(originPosition)
                        .destination(destinationPosition)
                        .shouldSimulateRoute(true)
                        .build();
                NavigationLauncher.startNavigation(MainActivity.this, options);
            }
        });
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.trafficmarker);
        IconFactory iconFactory1 = IconFactory.getInstance(MainActivity.this);
        Icon icon1 = iconFactory1.fromBitmap(bitmap1);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(30.07676327745203, 31.28802592831127))
                .icon(icon1));

        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.trafficmarker);
        IconFactory iconFactory2 = IconFactory.getInstance(MainActivity.this);
        Icon icon2 = iconFactory2.fromBitmap(bitmap2);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(30.078246749396925, 31.289950658765008))
                .icon(icon2));

        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.trafficmarker);
        IconFactory iconFactory3 = IconFactory.getInstance(MainActivity.this);
        Icon icon3 = iconFactory3.fromBitmap(bitmap3);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(30.078401162751177, 31.290101926806102))
                .icon(icon3));

        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.trafficmarker);
        IconFactory iconFactory4 = IconFactory.getInstance(MainActivity.this);
        Icon icon4 = iconFactory4.fromBitmap(bitmap4);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(30.07966123528712, 31.290990206635428))
                .icon(icon4));

        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.trafficmarker);
        IconFactory iconFactory5 = IconFactory.getInstance(MainActivity.this);
        Icon icon5 = iconFactory5.fromBitmap(bitmap5);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(30.134120292316624, 31.27552210752063))
                .icon(icon5));

    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }

    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), 13.0));

    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (destinationMarker != null) {
            map.removeMarker(destinationMarker);
        }
        destinationMarker = map.addMarker(new MarkerOptions().position(point));
        destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
        getRoute(originPosition, destinationPosition);
        startButton.setEnabled(true);
        startButton.setBackgroundResource(R.color.mapBoxBlue);
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found , check rigth user and access token");
                            return;
                        } else if (response.body().routes().size() == 0) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error:" + t.getMessage());
                    }
                });
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            timer = new CountDownTimer(180000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (originLocation == location) {
                        Toast.makeText(getApplicationContext(), "Move form your location", Toast.LENGTH_SHORT).show();
                    } else {
                        resetTimer();
                    }
                }

                @Override
                public void onFinish() {
                    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                            .accessToken("pk.eyJ1IjoiZ3JhZHVhdGlvbnByb2plY3QiLCJhIjoiY2p3eTdvbHVxMHJscjQ5anpzd20zaXlmNCJ9.nqnC0PUynIR86JuoDF5doQ")
                            .query(Point.fromLngLat(location.getLongitude(), location.getLatitude()))
                            .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                            .mode(GeocodingCriteria.TYPE_ADDRESS)
                            .build();
                    String Place = mapboxGeocoding.toString();
                    Date date = Calendar.getInstance().getTime();
                    violationClass.setLocation(Place);
                    violationClass.setDate(date);
                    violationClass.setText("Road interrupt");
                    myRef.push().setValue(violationClass);
                    Toast.makeText(getApplicationContext(), "You have made a violation", Toast.LENGTH_SHORT).show();
                }
            }.start();
            double lng = 0;
            if (originLocation != null) {
                lng = originLocation.getLongitude();
            }
            originLocation = location;
            initializeLocationEngine();
            if ((location.getLongitude() <= 31.275522733441335 && location.getLongitude() >= 31.2755219088412)
                    || (location.getLatitude() <= 30.134122167881912 && location.getLatitude() >= 30.134118006918854)
                    || (location.getLongitude() == 31.27552210752063) || (location.getLatitude() == 30.134120292316624)
                    && (TrafficFlag5 == false)) {
                addNotification();
                MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken("pk.eyJ1IjoiZ3JhZHVhdGlvbnByb2plY3QiLCJhIjoiY2p3eTdvbHVxMHJscjQ5anpzd20zaXlmNCJ9.nqnC0PUynIR86JuoDF5doQ")
                        .query(Point.fromLngLat(location.getLongitude(), location.getLatitude()))
                        .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                        .mode(GeocodingCriteria.TYPE_ADDRESS)
                        .build();
                String Place = mapboxGeocoding.toString();
                Date date = Calendar.getInstance().getTime();
                violationClass.setText("Opposite Direction Driving");
                violationClass.setLocation(Place);
                violationClass.setDate(date);

                myRef.push().setValue(violationClass);
                Toast.makeText(getApplicationContext(), "Infraction", Toast.LENGTH_SHORT).show();
            } else if (lng - location.getLongitude() > 0) {
                Toast.makeText(getApplicationContext(), "Goooood", Toast.LENGTH_SHORT).show();
            } else if (lng - location.getLongitude() < 0) {
                addNotification();
                Toast.makeText(getApplicationContext(), "Infraction", Toast.LENGTH_SHORT).show();
                MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken("pk.eyJ1IjoiZ3JhZHVhdGlvbnByb2plY3QiLCJhIjoiY2p3eTdvbHVxMHJscjQ5anpzd20zaXlmNCJ9.nqnC0PUynIR86JuoDF5doQ")
                        .query(Point.fromLngLat(location.getLongitude(), location.getLatitude()))
                        .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                        .mode(GeocodingCriteria.TYPE_ADDRESS)
                        .build();
                String Place = mapboxGeocoding.toString();
                Date date = Calendar.getInstance().getTime();
                violationClass.setText("Opposite Direction Driving");
                violationClass.setLocation(Place);
                violationClass.setDate(date);
                violationClass.setText("Opposite Direction Driving");
                myRef.push().setValue(violationClass);
            }
        }
    }

    private void resetTimer() {
        timer.start();
    }

    public void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.trafficmarker)
                        .setContentTitle("You have made a traffic violation")
                        .setContentText("This is a violation notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //present toast or dialog
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }
}
