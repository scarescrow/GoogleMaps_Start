package scarecrow.beta.gmaps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	private GoogleMap googleMap;
	double latitude, longitude;
	String title, snippet;
	
	private static String locations_Url = "http://dcetech.com/sagnik/gmaps/get_data.php";
	private static String KEY_NUMBER = "num";
	private static String KEY_DATA = "data";
	private static String KEY_SUCCESS = "success";
	private static String KEY_TITLE = "title";
	private static String KEY_SNIPPET = "snippet";
	private static String KEY_LATITUDE = "latitude";
	private static String KEY_LONGITUDE = "longitude";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new LoadLocations().execute();
		
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
	
	class LoadLocations extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;
	    
		@Override
	    protected void onPreExecute() {
			super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Locations ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
	    }
		
		@Override
		protected JSONObject doInBackground(String... params) {
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.getJSONFromUrl(locations_Url, null);
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			
			JSONObject row = null;
			
			try {
				initialiseMap();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			String status = "UnInitialised";
			try {
				status = json.getString(KEY_SUCCESS);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG);
			
			try {
				int num = json.getInt(KEY_NUMBER);
				
				if (num > 0) {
        			JSONArray data = json.getJSONArray(KEY_DATA);
        			
        			for(int i = 0; i < num; i++) {
        				row = data.getJSONObject(i);
        				title = row.getString(KEY_TITLE);
        				snippet = row.getString(KEY_SNIPPET);
        				latitude = row.getDouble(KEY_LATITUDE);
        				longitude =row.getDouble(KEY_LONGITUDE);
        				
        				Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
        				
        				MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude))
        						.title(title)
        						.snippet(snippet);
        				
        				googleMap.addMarker(marker);
        				
        			}
        			
        		} else {
        			Log.d("Error!", "No Notices");
        		}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
}
