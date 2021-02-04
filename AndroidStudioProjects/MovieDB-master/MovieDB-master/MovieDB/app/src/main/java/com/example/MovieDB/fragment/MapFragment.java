package com.example.MovieDB.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.MovieDB.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private String theaterCode;
    private LatLng location;
    private String markerTitle;
    private String markerId;
    private MapView mapView = null;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    private void setDB(Context ctx) {
        Log.d("theater","1");
        AssetManager assetManager = ctx.getResources().getAssets();
        File dbFile = ctx.getDatabasePath("theater.db");
        Log.d("theater",dbFile.getName());
//        File outfile = new File("movie.db");
        InputStream is;
        FileOutputStream fo = null;
        Log.d("theater","3");
        try {
            is = assetManager.open("theater.db", AssetManager.ACCESS_BUFFER);
            OutputStream os = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }
            os.flush();
            os.close();

            is.close();
        } catch (IOException e) {
            Log.e("DB", e.getLocalizedMessage(), e);
        }
        Log.d("theater","4");
    }

    class ProductDBHelper extends SQLiteOpenHelper {  //새로 생성한 adapter 속성은 SQLiteOpenHelper이다.
        ProductDBHelper(Context context) {
            super(context, "theater.db", null, 1);    // db명과 버전만 정의 한다.
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }


    private GoogleMap mMap;

    public MapFragment() {
        super();
    }

    @Override
    //구글맵을 띄울 준비가 되었으면 자동 호출
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //oneMarker();
        manyMarker();
        Log.d("onMapReady","the map is ready");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("onCreateMapView","yes!");
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("\t로딩중...");
        progressDialog.show();
        View rootView = inflater.inflate(R.layout.mapview, container, false);
        mapView = rootView.findViewById(R.id.map);
        Log.d("onCreateMapView","1");
        mapView.onCreate(savedInstanceState);
        Log.d("onCreateMapView","2");
        mapView.onResume();
        Log.d("onCreateMapView","3");
        mapView.getMapAsync(this); // 비동기적 방식으로 구글 맵 실행
        Log.d("onCreateMapView","4");

        mWebView =  rootView.findViewById(R.id.cgv);
        Log.d("Web","1");
        mWebView.setWebViewClient(new WebViewClient());
        Log.d("Web","2");
        mWebSettings = mWebView.getSettings();
        Log.d("Web","3");
        mWebSettings.setJavaScriptEnabled(true);
        Log.d("Web","4");

        mWebView.loadUrl("https://www.cgv.co.kr/");
        progressDialog.dismiss();
        Log.d("Web","5 - loadUrl");
        return rootView;

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if(locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 200, IListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(locationManager != null)
            locationManager.removeUpdates(IListener);
    }

    ////////////////////////  구글맵 마커 여러개생성 및 띄우기 //////////////////////////
    private void manyMarker() {

        setDB(Objects.requireNonNull(getActivity()));
        ProductDBHelper mHelper = new ProductDBHelper(getActivity());
        SQLiteDatabase db = mHelper.getWritableDatabase();


        Cursor cursor = db.rawQuery("SELECT name,lon,lat,theaterCode from theater_address;", null); //쿼리문
        cursor.moveToFirst();
        Log.d("cursor",cursor.getString(0));
        Log.d("cursor", String.valueOf(cursor.getDouble(1)));

        if (cursor.moveToFirst()) {
            Log.d("cursor", String.valueOf(cursor.getDouble(01)));
            // for loop를 통한 n개의 마커 생성
            for (int idx = 0; idx < 27; idx++) {
                // 1. 마커 옵션 설정 (만드는 과정)
                MarkerOptions makerOptions = new MarkerOptions();

                makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                        .position(new LatLng(cursor.getDouble(2), cursor.getDouble(1)))
                        .snippet(cursor.getString(3))
                        .title(cursor.getString(0)); // 타이틀


                // 2. 마커 생성 (마커를 나타냄)
                mMap.addMarker(makerOptions);
                Log.d("cursor",cursor.getString(0));
                cursor.moveToNext();
                Log.d("cursor",cursor.getString(0));
            }
        }
        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.52487, 126.92723), 16));
        Log.d("marker","finished!");
    }

    //마커정보창 클릭리스너는 다작동하나, 마커클릭리스너는 snippet정보가 있으면 중복되어 이벤트처리가 안되는거같다.
    // oneMarker(); 는 동작하지않으나 manyMarker(); 는 snippet정보가 없어 동작이가능하다.

    //정보창 클릭 리스너
    private GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
            theaterCode = marker.getSnippet();

            while(theaterCode.length() <4){
                theaterCode = "0"+theaterCode;
            }

            Toast.makeText(getActivity(), "해당 cgv 예매페이지로 이동합니다. ", Toast.LENGTH_SHORT).show();
            mWebView.loadUrl("http://section.cgv.co.kr/theater/timetable/Default.aspx?code=" + theaterCode);
            Log.d("url","http://section.cgv.co.kr/theater/timetable/Default.aspx?code=" + theaterCode);
        }
    };

    //마커 클릭 리스너
    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            markerId = marker.getId();
            //선택한 타겟위치
            location = marker.getPosition();
            markerTitle = marker.getTitle();
            Toast.makeText(getActivity(), markerTitle + "cgv입니다. 예매페이지로 이동하려면 정보창을 눌러주세요.", Toast.LENGTH_SHORT).show();
            theaterCode = marker.getSnippet();
            return false;
        }
    };


    //여기서 부터 웹뷰
    private WebView mWebView;
    private WebSettings mWebSettings;

    public void onCreate(Bundle savedInstanceState) {
        Log.d("WebView","1");
        super.onCreate(savedInstanceState);
        Log.d("WebView","2");

        getLocationManager(); //2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Log.d("WebView","3");
        getLocationManager();
        Log.d("WebView","4");
    }




    //지속적으로 위치 공급자로부터 위치정보를 받아온다.
    private LocationListener IListener = new LocationListener() { //계속 듣고 있다가 반응한다.

        @Override
        public void onLocationChanged(Location location) { //위치의 변화가 있을 때.
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Here", "The location is changed");


            setDB(Objects.requireNonNull(getActivity()));
            ProductDBHelper mHelper = new ProductDBHelper(getActivity());
            SQLiteDatabase db = mHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT name,lon,lat,theaterCode from theater_address;", null); //쿼리문
            cursor.moveToFirst();
            Log.d("cursor1", cursor.getString(0));
            Log.d("cursor1", String.valueOf(cursor.getDouble(1)));

            double mindistance = 10000000;
            String nearCGV;
            double selected_lat = 0;
            double selected_lon = 0;


            if (cursor.moveToFirst()) {
                Log.d("cursor1", "if statement");
                // for loop를 통한 n개의 마커 생성
                for (int idx = 0; idx < 27; idx++) {

                    float cgv_lon = cursor.getFloat(1);
                    float cgv_lat = cursor.getFloat(2);
                    double distance = Math.pow((longitude - cgv_lon), 2) + Math.pow((latitude - cgv_lat), 2);
                    if (distance < mindistance) {
                        mindistance = distance;
                        nearCGV = cursor.getString(0);
                        selected_lat = cgv_lat;
                        selected_lon = cgv_lon;
                        Log.d("Here", "min changed to " + nearCGV);
                    }
                    cursor.moveToNext();
                }
                Log.d("Here", "lat: " + selected_lat);

            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selected_lat, selected_lon), 14));

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
    };

    private void getLocationManager() { //LOCATION_SERVICE 시스템 서비스를 이용할 수 있다.
        Log.d("locationManager","locationManager activated");
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_GRANTED == Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        return false;
    }
}
