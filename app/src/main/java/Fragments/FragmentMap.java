package Fragments;
//===================================================================================== Imports
//https://stackoverflow.com/questions/7477003/calculating-new-longitude-latitude-from-old-n-meters

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Components.CustomSearchListViewAdapter;
import Interfaces.InterfaceAsyncTasks;
import Utilities.AddressNameAndDriversLocations;
import Utilities.DirectionsJSONParser;
import Utilities.GPSTracker;
import Utilities.Utility;
import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.PaymentActivity;
import ir.iro.passenger.R;

//===================================================================================== Main Class
public class FragmentMap extends android.support.v4.app.Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapLoadedCallback {

    public final static float DEFAULTZOOM = 11;//15;
    public View vMapView;
    public static GoogleMap mapGlobalMap;
    public AddressTask asyncCurrentTask;
    public static List<String> lstLocationNameList;
    public static ListView lvSimilarAddress;
    public static Marker avareziMarker, pardisAvareziMarker, karajAvareziMarker;

    private final double TEHRAN_LAT = 35.692272, TEHRAN_LNG = 51.385515;
    private boolean bNearToAvarezi, bTrackerStarter;
    private LinearLayout lntFragmentMap;
    private List<LatLng> lstWardensLantLng, lstHandyTollWardens, lstTrafficEnv, lstAutomaticToll;
    private List<Marker> lstMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vResult = inflater.inflate(R.layout.fragment_map, container, false);
        lntFragmentMap = (LinearLayout) vResult.findViewById(R.id.fragment_map_id);

        init();
        MapInit(mapGlobalMap);

        return vResult;
    }

    void init() {

        Utility.showProgress(getContext());
        String strObject;
        lstWardensLantLng = new ArrayList<>();
        lstHandyTollWardens = new ArrayList<>();
        lstTrafficEnv = new ArrayList<>();
        lstMarker = new ArrayList<>();
        lstAutomaticToll = new ArrayList<>();

        bTrackerStarter = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            strObject = bundle.getString(Utility.MAP_INTENT_WARDENS, "");
            if (strObject != null && strObject.length() > 0) {
                Type listType = new TypeToken<ArrayList<LatLng>>() {
                }.getType();

                lstWardensLantLng = new Gson().fromJson(strObject, listType);
            }

            strObject = bundle.getString(Utility.MAP_INTENT_HANDY_TOLL, "");
            if (strObject != null && strObject.length() > 0) {
                Type listType = new TypeToken<ArrayList<LatLng>>() {
                }.getType();

                lstHandyTollWardens = new Gson().fromJson(strObject, listType);
            }

            strObject = bundle.getString(Utility.MAP_INTENT_TRAVE_PLAN, "");
            if (strObject != null && strObject.length() > 0) {
                Type listType = new TypeToken<ArrayList<LatLng>>() {
                }.getType();

                lstTrafficEnv = new Gson().fromJson(strObject, listType);
            }

            strObject = bundle.getString(Utility.MAP_INTENT_AUTOMATIC_TOLL, "");
            if (strObject != null && strObject.length() > 0) {
                Type listType = new TypeToken<ArrayList<LatLng>>() {
                }.getType();

                bTrackerStarter = true;
                lstAutomaticToll = new Gson().fromJson(strObject, listType);
            }

        }

        ActivityMain.spGlobalSharedpreferences = getActivity().getSharedPreferences(
                getResources().getString(R.string.shared_prefrences_name),
                Context.MODE_PRIVATE
        );

    }

    private void MapInit(GoogleMap mMap) {
        Log.e("AAAAA", "AAAAAA");
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        vMapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        bNearToAvarezi = false;
        SetupMap(mapFragment);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapGlobalMap = googleMap;

        mapGlobalMap.getUiSettings().setRotateGesturesEnabled(false);
        mapGlobalMap.getUiSettings().setMyLocationButtonEnabled(true);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(TEHRAN_LAT, TEHRAN_LNG), DEFAULTZOOM);
        mapGlobalMap.moveCamera(update);

        mapGlobalMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapGlobalMap.setTrafficEnabled(true);
        mapGlobalMap.setBuildingsEnabled(true);

        mapGlobalMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                for (int j = 0; j < lstMarker.size(); j++) {
                    if (marker.equals(lstMarker.get(j))) {
                        if (lstHandyTollWardens.size() > 0)
                            intent.putExtra("page_type", "عوارضی");

                        if (lstWardensLantLng.size() > 0)
                            intent.putExtra("page_type", "پارکبان");

                        startActivity(intent);
                    }
                }

                for (int k = 0; k < lstTrafficEnv.size(); k++) {
                    if (marker.equals(lstTrafficEnv.get(k))) {

                        startActivity(intent);
                    }
                }

                return true;
            }
        });

        mapGlobalMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //lstTrafficEnv
                if (isPointInPolygon(latLng, lstTrafficEnv)) {
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("page_type", "طرح ترافیک");
                    startActivity(intent);
                }
            }
        });


        ActivityMain.speUserCurrentLocation = getActivity().getSharedPreferences(
                getResources().getString(R.string.shared_prefrences_name),
                Context.MODE_PRIVATE
        ).edit();

        ActivityMain.speUserCurrentLocation.putBoolean(getResources().getString(R.string.once_run), true);
        ActivityMain.speUserCurrentLocation.apply();

        mapGlobalMap.setOnMapLoadedCallback(this);

        Log.e("BBBB", "BBBB");
    }

    void setupMarkersAndTrafficEnvironment() {

        if (lstWardensLantLng.size() > 0) {
            for (int i = 0; i < lstWardensLantLng.size(); i++) {
                lstMarker.add(addMarker(lstWardensLantLng.get(i).latitude, lstWardensLantLng.get(i).longitude, "TEHRAN", R.drawable.marker_parking));
            }
        }

        if (lstHandyTollWardens.size() > 0) {
            for (int i = 0; i < lstHandyTollWardens.size(); i++) {
                lstMarker.add(addMarker(lstHandyTollWardens.get(i).latitude, lstHandyTollWardens.get(i).longitude, "TEHRAN", R.drawable.marker_toll));
            }
        }

        if (lstTrafficEnv.size() > 0) {
            DrawTraffic();
        }

        if (lstAutomaticToll.size() > 0) {
            for (int i = 0; i < lstAutomaticToll.size(); i++) {
                lstMarker.add(addMarker(lstAutomaticToll.get(i).latitude, lstAutomaticToll.get(i).longitude, "TEHRAN", R.drawable.marker_toll));
            }
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Utility.PERMISSION_LOCATION);

        } else {
            enableGpsAndTrack();
        }
    }

    public void enableGpsAndTrack() {
        try {
            GPSTracker gps;
            gps = new GPSTracker(getActivity());
            if (gps.canGetLocation()) {
                mapGlobalMap.setMyLocationEnabled(true);
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                gotoLocation(latitude, longitude, DEFAULTZOOM);

                Location currentLocation = new Location(LocationManager.PASSIVE_PROVIDER);
                currentLocation.setLatitude(latitude);
                currentLocation.setLongitude(longitude);

                if (bTrackerStarter)
                    gpsTRackerStart();
                //final Location destLocation = TestShowDialog(currentLocation);
            } else {
                gps.showSettingsAlert();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    void gpsTRackerStart() {


        LocationManager service = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Location[] userLocationLocation = {service.getLastKnownLocation(provider)};


        if (userLocationLocation[0] != null) {
            gotoLocation(userLocationLocation[0].getLatitude(), userLocationLocation[0].getLongitude(), DEFAULTZOOM);
            final Location destLocation = TestShowDialog(userLocationLocation[0]);

            mapGlobalMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(final Location location) {
                    gotoLocation(location.getLatitude(), location.getLongitude(), DEFAULTZOOM);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            CalculateWalking(location);
                        }

                    };

                    thread.start();

                    userLocationLocation[0] = location;
                }

            });
        }
    }

    Location TestShowDialog(Location location) {

        double meters = 1000;
        double coef = meters * 0.0000089;
        final double new_lat = location.getLatitude() + coef;
        final double new_long = location.getLongitude() + coef / Math.cos(location.getLatitude() * 0.018);
        //showDirection(avareziMarker, location);
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(new_lat);
        temp.setLongitude(new_long);

        return temp;

    }

    void showDirection(Marker marker, Location location) {

        LatLng origin = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        LatLng dest = new LatLng(location.getLatitude(), location.getLongitude());
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);

    }

    void DrawTraffic() {
        PolygonOptions opts = new PolygonOptions();
        for (LatLng location : lstTrafficEnv) {
            opts.add(location);
        }

        Polygon polygon = mapGlobalMap.addPolygon(opts
                .strokeColor(getResources().getColor(R.color.polyline_stroke_color))
                .fillColor(getResources().getColor(R.color.polyline_color)));

    }

    @Override
    public void onMapLoaded() {
        Utility.hideProgress();
        setupMarkersAndTrafficEnvironment();
        checkPermission();
    }

    void CalculateWalking(Location oldLocation) {

        for (int i = 0; i < lstAutomaticToll.size(); i++) {
            float distance = ActivityMain.Distance(
                    oldLocation.getLatitude(),
                    oldLocation.getLongitude(),
                    lstAutomaticToll.get(i).latitude,
                    lstAutomaticToll.get(i).longitude);


            if (distance < 140.93) {
                new FancyGifDialog.Builder(getActivity())
                        .setTitle("عوارضی")
                        .setMessage("شما به محدوده ی عوارضی نزدیک شدید. آیا مایل به پرداخت عوارض به صورت آنلاین هستید ؟")
                        .setNegativeBtnText("خیر")
                        .setPositiveBtnBackground("#FF4081")
                        .setPositiveBtnText("بله")
                        .setNegativeBtnBackground("#FFA9A7A8")
                        .isCancellable(true)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                //Toast.makeText(getActivity(), "بله", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                startActivity(intent);
                                bNearToAvarezi = false;
                            }
                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getActivity(), "خیر", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getActivity(), PaymentActivity.class);
//                                startActivity(intent);
//                                bNearToAvarezi = false;
                            }
                        })
                        .build();

                break;
            }
        }
    }

    public static void gotoLocation(double lat, double lng, float Zoom) {
        LatLng ll = new LatLng(lat, lng);

        // TODO : Go Camera To lat/lng Location
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, Zoom);
        mapGlobalMap.animateCamera(update);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class ShowAddressTask extends AsyncTask<String, String, String> {
        public double lat, lng;
        public InterfaceAsyncTasks interfaceAsyncTasks = null;

        // TODO : Show Address Constructor
        public ShowAddressTask(double fLat, double fLng) {
            lat = fLat;
            lng = fLng;
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO : Show Address In SearchBox
            return ShowAddress(lat, lng);
        }

        @Override
        protected void onPostExecute(String s) {
            interfaceAsyncTasks.ShowAddressTask_Callback(s);
        }
    }

    private String ShowAddress(Double latitude, Double longitude) {
        String strResult = getResources().getString(R.string.no_address);
        try {
            String strLanguage = "fa";
            Locale lcLocal = new Locale(strLanguage);
            Geocoder geoCoder;
            List<Address> lstAddreses;

            Locale.setDefault(lcLocal);
            geoCoder = new Geocoder(getContext(), Locale.getDefault());
            lstAddreses = geoCoder.getFromLocation(latitude, longitude, 1);

            if (lstAddreses.isEmpty()) {
                return strResult;
            } else {
                if (lstAddreses.size() > 0) {
                    strResult =
                            lstAddreses.get(0).getFeatureName() + ", " +
                                    lstAddreses.get(0).getLocality() + ", " +
                                    lstAddreses.get(0).getAdminArea();
                    return strResult;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResult;
    }

    private void SetupMap(SupportMapFragment mapFragment) {
        View vLocationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("2"));

        RelativeLayout.LayoutParams rlPrams = (RelativeLayout.LayoutParams) vLocationButton.getLayoutParams();
        rlPrams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlPrams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlPrams.setMargins(0, 0, 30, 30);

    }

    private static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Marker addMarker(double lat, double lng, String locality, int iDrawbleId) {
        MarkerOptions markerOpt;
        if (iDrawbleId == 0) {
            markerOpt = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(lat, lng))
                    .flat(true);
        } else {
            markerOpt = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(lat, lng))
                    .flat(true)
                    //.anchor(0.0f, 0.3f)
                    .icon(BitmapDescriptorFactory.fromResource(iDrawbleId));
        }


        return FragmentMap.mapGlobalMap.addMarker(markerOpt);
    }

    public static void MoveCameraWithAnimate(int bChangeZoom, double dblLatitude, double dblLongtitude) {
        CameraUpdate update;
        LatLng ll;

        ll = new LatLng
                (
                        dblLatitude,
                        dblLongtitude
                );

        if (bChangeZoom == 1) {
            update = CameraUpdateFactory.newLatLngZoom(ll, FragmentMap.mapGlobalMap.getCameraPosition().zoom + 1);
        } else if (bChangeZoom == 0) {
            update = CameraUpdateFactory.newLatLngZoom(ll, FragmentMap.mapGlobalMap.getCameraPosition().zoom - 1);
        } else {
            update = CameraUpdateFactory.newLatLngZoom(ll, FragmentMap.mapGlobalMap.getCameraPosition().zoom);
        }

        FragmentMap.mapGlobalMap.animateCamera(update);

    }

    private class AddressTask extends AsyncTask<String, AddressNameAndDriversLocations, AddressNameAndDriversLocations> {
        public LatLng myLatLng;
        public InterfaceAsyncTasks syncInterfaceAsyncTasks = null;

        // TODO : Show Address Constructor
        public AddressTask(LatLng latlng) {
            myLatLng = latlng;
        }

        @Override
        protected AddressNameAndDriversLocations doInBackground(String... params) {
            try {
                Gson gson = new Gson();
                //String latlng = "35.709658, 51.348395";
                HttpPost request = new HttpPost("http://192.168.43.164:8081/IROWebService.svc/IRO/GetAddressByLatlng");
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                StringEntity entity = new StringEntity(gson.toJson(myLatLng.latitude + "," + myLatLng.longitude), "UTF-8");
                //Log.i("test", "AAAAA" + gson.toJson(myLatLng));
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                entity.setContentType("application/json");
                request.setEntity(entity);
                // Send request to WCF service
                DefaultHttpClient httpClient = new DefaultHttpClient();
                //Log.i("test", "Befor Execute");
                HttpResponse response = httpClient.execute(request);
                //Log.i("test", "After Execute");
                String responseJson = EntityUtils.toString(response.getEntity());
                //Log.i("test", "Excetion Accourde : " + responseJson);
                AddressNameAndDriversLocations lst = new AddressNameAndDriversLocations();
                lst = gson.fromJson(responseJson, AddressNameAndDriversLocations.class);
                //Log.i("test", lst.Address);

                return lst;
            } catch (Exception e) {
                Log.i("test", e.toString());
            } finally {
                Log.i("test", "Error : in finally");
            }
            return null;
        }

        @Override
        protected void onPostExecute(AddressNameAndDriversLocations s) {
            super.onPostExecute(s);
            if (s != null)
                syncInterfaceAsyncTasks.SyncShowAddress_Callback(s);
        }
    }

    public static void StartSimilarAddress(View v, String str) throws IOException {
        //FragmentMap frgMap = new FragmentMap();
        geoLocate(v, str);
    }

    public static String geoLocate(View view, String strSearchAddress) throws IOException {

        String strResult = "", strlanguage;
        Locale lcLocal;
        Geocoder geo;
        List<Address> lstAddresses;

        // TODO : Hide keyboard
        hideSoftKeyboard(view);

        // TODO : Set Searching Language

        strlanguage = "fa";
        lcLocal = new Locale(strlanguage);

        Locale.setDefault(lcLocal);
        geo = new Geocoder(ActivityMain.GlobalActivityContext, lcLocal.getDefault());
        lstAddresses = geo.getFromLocationName(strSearchAddress, 20, 35.556301, 51.053682, 35.808553, 51.594103);

        if (lstAddresses == null) {
            strResult = "";
        } else {
            if (lstAddresses.isEmpty()) {
                strResult = "";
            } else {
                if (lstAddresses.size() > 0) {
                    String str = String.valueOf(lstAddresses.size());
                    Toast.makeText(view.getContext(), str, Toast.LENGTH_LONG).show();
                    // TODO : Clear Location Names List
                    lstLocationNameList.clear();

                    // TODO : Parse Address For Get Address Name
                    for (Address i : lstAddresses) {
                        if (i.getFeatureName() == null) {
                            lstLocationNameList.add("unknown");
                        } else {
                            //lat = i.getLatitude();
                            //lan = i.getLongitude();

                            strResult = i.getAddressLine(0) + ", " + i.getFeatureName() + ", " +
                                    i.getLocality() + ", " + i.getAdminArea() + ", " +
                                    i.getCountryName() + ", " + i.getExtras() + ", " +
                                    i.getPremises() + ", " + i.getSubAdminArea() + ", " +
                                    i.getSubLocality() + ", " + i.getSubThoroughfare() + ", " +
                                    i.getThoroughfare();

                            //if(i.getLocality() == "تهران")
                            lstLocationNameList.add(getAddress(i));

                            // TODO : Goto Address Location In Map
                            //gotoLocation(lat, lan, DEFAULTZOOM);
                        }
                    }

                    lvSimilarAddress.setAdapter(new CustomSearchListViewAdapter(ActivityMain.GlobalActivityContext, lvSimilarAddress, FragmentMap.lstLocationNameList));
                }
            }
        }
        return strResult;
    }

    public static String getAddress(Address address) {

        String strDisplayAddress = "";

        strDisplayAddress += address.getAddressLine(0) + "\n";
        for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
            strDisplayAddress += address.getAddressLine(i) + ", ";
        }
        strDisplayAddress = strDisplayAddress.substring(0, strDisplayAddress.length() - 2);

        return strDisplayAddress;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String getDirectionsUrlByLocation(double srcLat, double srcLng, double destLat, double desLng) {

        // Origin of route
        String str_origin = "origin=" + srcLat + "," + srcLng;

        // Destination of route
        String str_dest = "destination=" + destLat + "," + desLng;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(ActivityMain.GlobalActivityContext, "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        Toast.makeText(ActivityMain.GlobalActivityContext, point.get("distance"), Toast.LENGTH_LONG).show();
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
        }
    }

    private boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.latitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX);               // Rise over run
        double bee = (-aX) * m + aY;                // y = mx + b
        double x = (pY - bee) / m;                  // algebra is neat!

        return x > pX;
    }

}
