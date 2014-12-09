package scarecrow.beta.gmaps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private GoogleMap googleMap;
	double latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			initialiseMap();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		latitude = 28.631370;
		longitude = 77.222107;
		
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude));
		
		googleMap.addMarker(marker);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initialiseMap() {
		if(googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			googleMap.setMyLocationEnabled(true);
		}
		
		if(googleMap == null)
			Toast.makeText(getApplicationContext(), "Sorry,  Unable to create GoogleMaps", Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initialiseMap();
	}
}
