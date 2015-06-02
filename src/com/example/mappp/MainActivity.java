package com.example.mappp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity implements LocationListener{

	MapView map;
	long start, stop;
	MyLocationOverlay comp;
	MapController control;
	int x, y;
	GeoPoint point;
	GeoPoint touchedpt;
	Drawable d;
	List<Overlay> list;
	int longitu,latitu;
	LocationManager locm;
	String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);

		Touchy t = new Touchy();
		list = map.getOverlays();
		list.add(t);

		comp = new MyLocationOverlay(MainActivity.this, map);
		list.add(comp);
		control = map.getController();
		point = new GeoPoint(51645218, 5842314);
		control.animateTo(point);
		control.setZoom(6);
		d = getResources().getDrawable(R.drawable.ball3);
		getlocation();
		
	}
//placing pinpointat location;
	private void getlocation() {
		
		locm =  (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria cri = new Criteria();
				
	provider = locm.getBestProvider(cri, false);
	Location loc= locm.getLastKnownLocation(provider);
	if(loc != null){
		 latitu = (int) (loc.getLatitude() *1E6);
		 longitu = (int) (loc.getLongitude() *1E6);
		
		 GeoPoint currentpoint = new GeoPoint(latitu, longitu);
			OverlayItem ovitem = new OverlayItem(currentpoint, "Message 1", "Message 2");
			Pinpoint cpp = new Pinpoint(d, MainActivity.this); 
			cpp.insertmark(ovitem);
			list.add(cpp);
		 Toast.makeText(getBaseContext(), " Range within " + loc.getAccuracy(), Toast.LENGTH_LONG).show();
	}
	else{
		Toast.makeText(getBaseContext(), " Location not found ", Toast.LENGTH_LONG);
	}
		
	}

	@Override
	protected void onPause() {
		comp.disableCompass();
		super.onPause();
		locm.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		comp.enableCompass();
		super.onResume();
	locm.requestLocationUpdates(provider, 500, 1, this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {

		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouchEvent(MotionEvent arg0, MapView arg1) {

			if (arg0.getAction() == MotionEvent.ACTION_DOWN) { 
				start = arg0.getEventTime();
				x = (int) arg0.getX();
				y = (int) arg0.getY();
				touchedpt = map.getProjection().fromPixels(x, y);
			}
			if (arg0.getAction() == MotionEvent.ACTION_UP) {

				stop = arg0.getEventTime();

			}
			if (stop - start > 1500) {

				AlertDialog popup = new AlertDialog.Builder(MainActivity.this)
						.create();
				popup.setTitle("Pick an option");
				popup.setMessage("What to do..?");
				popup.setButton("Get Address",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								Geocoder getinfo = new Geocoder(
										getBaseContext(), Locale.getDefault());
								try {
									List<Address> address = getinfo
											.getFromLocation(
													touchedpt.getLatitudeE6(),
													touchedpt.getLatitudeE6(),
													1);
									if (address.size() > 0) {
										String disp = "";
										for (int i = 0; i < address.get(0)
												.getMaxAddressLineIndex(); i++) {

											disp += address.get(0)
													.getAddressLine(i) + "\n";

										}
										Toast t = Toast.makeText(
												getBaseContext(), disp,
												Toast.LENGTH_LONG);
										t.show();

									}

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} finally {

								}

							}
						});
				popup.setButton2("Pinpoint this location",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								
								OverlayItem ovitem = new OverlayItem(touchedpt, "Message 1", "Message 2");
								Pinpoint cpp = new Pinpoint(d, MainActivity.this); 
								cpp.insertmark(ovitem);
								list.add(cpp);
							
							}
						});
				popup.setButton3("Toggle View",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								
								if(map.isSatellite()){
									
									map.setSatellite(false);
									map.setStreetView(true);
							
								}else{
									map.setStreetView(false);
									map.setSatellite(true);
								}
								
							}
						});
				popup.show();
				return true;
			}
			return false;
		}

	}

	@Override
	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub
		
		longitu = (int) (l.getLongitude()*1E6);
		latitu = (int) (l.getLatitude()*1E6);
		GeoPoint currentpoint = new GeoPoint(latitu, longitu);
		OverlayItem ovitem = new OverlayItem(currentpoint, "Message 1", "Message 2");
		Pinpoint cpp = new Pinpoint(d, MainActivity.this); 
		cpp.insertmark(ovitem);
		list.add(cpp);
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}}
