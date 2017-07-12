package mean.chan.mind.sendgps;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyServiceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] loginString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_layout);

        //get Value From Inten
        getValueFromInten();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        addMapFragment();


        //Back
        backController();

        //Save
        saveController();

        //List
        listController();


    } //Main Method

    private void listController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvListView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyServiceActivity.this, FirstActivity.class);
                intent.putExtra("Login", loginString);
                startActivity(intent);
            }
        });
    }

    private void saveController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvSave);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyServiceActivity.this, AddChildActivity.class);
                intent.putExtra("Login", loginString);
                startActivity(intent);
            }
        });
    }

    private void backController() {
        ImageView imageView = (ImageView) findViewById(R.id.imvBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void getValueFromInten() {

        loginString = getIntent().getStringArrayExtra("Login");
    }

    private void addMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String tag = "13JulyV1";

        try {

            Log.d(tag, "id_Parent ==> " + loginString[0]);

            GetLocationWhereIdParent getLocationWhereIdParent = new GetLocationWhereIdParent(this);
            getLocationWhereIdParent.execute(loginString[0]);

            String strJSON = getLocationWhereIdParent.get();
            Log.d(tag, "JSON ==> " + strJSON);

            JSONArray jsonArray = new JSONArray(getLocationWhereIdParent.get());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String strLat = jsonObject.getString("Lat");
            String strLng = jsonObject.getString("Lng");
            Log.d(tag, "Lat ==> " + strLat);
            Log.d(tag, "Lng ==> " + strLng);

            LatLng latLng = new LatLng(Double.parseDouble(strLat), Double.parseDouble(strLng));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);


        } catch (Exception e) {
            Log.d(tag, "e ==> " + e.toString());
        }


    } //omMapReady


} //Main Class
