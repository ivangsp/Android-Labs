package ee.ojiambout.ivan.classwork4;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.carto.core.MapPos;
import com.carto.core.MapPosVector;
import com.carto.core.Variant;
import com.carto.datasources.LocalVectorDataSource;
import com.carto.geometry.FeatureCollection;
import com.carto.graphics.Color;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.VectorElementEventListener;
import com.carto.layers.VectorLayer;
import com.carto.projections.Projection;
import com.carto.services.CartoSQLService;
import com.carto.styles.BalloonPopupStyleBuilder;
import com.carto.styles.MarkerStyleBuilder;
import com.carto.styles.PointStyleBuilder;
import com.carto.styles.PolygonStyleBuilder;
import com.carto.ui.MapView;
import com.carto.ui.VectorElementClickInfo;
import com.carto.utils.Log;
import com.carto.vectorelements.BalloonPopup;
import com.carto.vectorelements.Billboard;
import com.carto.vectorelements.Marker;
import com.carto.vectorelements.Polygon;
import com.carto.vectorelements.VectorElement;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static final String LICENSE = "XTUN3Q0ZCQXJybmtSYmVvY0hMQkRzSkZDNGhoS1R5TWFBaFFpNGl0d2xuSDNydFA3K1I5bVVWYkkzWlN0enc9PQoKcHJvZHVjdHM9KgpwYWNrYWdlTmFtZT0qCndhdGVybWFyaz1jYXJ0bwp2YWxpZFVudGlsPTIwMTctMTItMDEKYXBwVG9rZW49NGExOTkwYTktNmIyYy00YTcwLThlNzEtOThmZDA1Mjc4MTM3Cg==";
    private LocationListener locationListener = null;
    private MapView mapView;
    private LocalVectorDataSource source;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView.registerLicense(LICENSE, getApplicationContext());

        mapView = (MapView) findViewById(R.id.map_view);

        CartoOnlineVectorTileLayer layer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
        mapView.getLayers().add(layer);


        // Initialize a local vector data source​
        final Projection projection = mapView.getOptions().getBaseProjection();
        source = new LocalVectorDataSource(projection);

        // Create Marker style
        MarkerStyleBuilder builder = new MarkerStyleBuilder();
        builder.setSize(30);
        builder.setColor(new Color(android.graphics.Color.WHITE));

        // Add marker to the data source
        MapPos tartu = projection.fromWgs84(new MapPos(26.7363542, 58.3842485));
        Marker marker = new Marker(tartu, builder.buildStyle());
        source.add(marker);

        // Create VectorLayer with previously created source and add it to the map
        VectorLayer markerLayer = new VectorLayer(source);
        mapView.getLayers().add(markerLayer);

        marker.setMetaDataElement("id", new Variant("HOME"));

        markerLayer.setVectorElementEventListener(new MyVectorElementEventListener(source));


        // to start with showing my location have to start with requesting permissions for location:
        // this will end up with callbacks

        if (Build.VERSION.SDK_INT >= 23) { // Marshmallow or newer
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            showMyLocation();
        }

        // add speedcameras from CARTO SQL API, using special client Service

        final CartoSQLService service = new CartoSQLService();
        service.setUsername("nutiteq");

        // Be sure to make network queries on another thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    PointStyleBuilder pointStyleBuilder = new PointStyleBuilder();
                    pointStyleBuilder.setColor(new Color(android.graphics.Color.MAGENTA));
                    pointStyleBuilder.setSize(3);

                    String query = "SELECT sc.*,ay.a_nimi,ay.m_nimi,ay.o_nimi FROM ee_speedcams sc, asustusyksus_2017_reformitud ay where st_contains(ay.the_geom,sc.the_geom)";

                    FeatureCollection features = service.queryFeatures(query, projection);

                    source.addFeatureCollection(features,pointStyleBuilder.buildStyle());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }


    protected void requestPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
    }

    // Implement this callback method, called after user clicks "Allow" or "Deny" on the AlertDialog
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMyLocation();
                } else {
                    // Inform that no permissions no cookie
                    Toast.makeText(this, "Location permission not granted, cannot show device location", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void showMyLocation() {

        // prepare MapView graphical elements to show location circle

        PolygonStyleBuilder polygonBuilder = new PolygonStyleBuilder();
        polygonBuilder.setColor(new Color(0xAAFF0000));

        MapPosVector positionVector = new MapPosVector();

        final Polygon circle = new Polygon(positionVector, polygonBuilder.buildStyle());

        // Initially empty and hidden
        circle.setVisible(false);
        source.add(circle);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.debug("GPS onLocationChanged " + location);
                if (circle != null) {
                    circle.setPoses(createLocationCircle(location, mapView.getOptions().getBaseProjection()));
                    circle.setVisible(true);
                    mapView.setFocusPos(mapView.getOptions().getBaseProjection().fromWgs84(new MapPos(location.getLongitude(), location.getLatitude())), 0.5f);
                    mapView.setZoom(18, 1.0f); // zoom 2, duration 0 seconds (no animation)
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }

            private MapPosVector createLocationCircle(Location location, Projection proj) {

                // number of points of circle
                int N = 50;
                int EARTH_RADIUS = 6378137;

                float radius = location.getAccuracy();
                double centerLat = location.getLatitude();
                double centerLon = location.getLongitude();

                MapPosVector points = new MapPosVector();

                for (int i = 0; i <= N; i++) {

                    double angle = Math.PI * 2 * (i % N) / N;
                    double dx = radius * Math.cos(angle);
                    double dy = radius * Math.sin(angle);
                    double lat = centerLat + (180 / Math.PI) * (dy / EARTH_RADIUS);
                    double lon = centerLon + (180 / Math.PI) * (dx / EARTH_RADIUS) / Math.cos(centerLat * Math.PI / 180);

                    points.add(proj.fromWgs84(new MapPos(lon, lat)));
                }

                return points;
            }
        };

        // create location manager and find providers

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // user has maybe disabled location services / GPS
        if (locationManager.getProviders(true).size() == 0) {
            Toast.makeText(this, "Cannot get location, no location providers enabled. Check device settings", Toast.LENGTH_LONG).show();
        }

        // use all enabled device providers with same parameters
        for (String provider : locationManager.getProviders(true)) {
            Log.debug("adding location provider " + provider);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 50, locationListener);
        }
    }

    private class MyVectorElementEventListener extends VectorElementEventListener {

        LocalVectorDataSource source;

        public  MyVectorElementEventListener(LocalVectorDataSource source) {
            this.source = source;
        }

        @Override
        public boolean onVectorElementClicked(VectorElementClickInfo vectorElementClickInfo) {

            BalloonPopupStyleBuilder builder = new BalloonPopupStyleBuilder();

            VectorElement element = vectorElementClickInfo.getVectorElement();
            String text = element.getMetaDataElement("id").getString();

            BalloonPopup popup = new BalloonPopup((Billboard)element, builder.buildStyle(), text, "");

            source.add(popup);

            return true;
        }
    }


}