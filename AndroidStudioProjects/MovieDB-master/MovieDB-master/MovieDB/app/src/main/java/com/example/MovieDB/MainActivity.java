package com.example.MovieDB;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.MovieDB.fragment.FavoriteFragment;
import com.example.MovieDB.fragment.HomeFragment;
import com.example.MovieDB.fragment.MapFragment;
import com.example.MovieDB.fragment.SearchActorFragment;
import com.example.MovieDB.fragment.SearchFragment;


public class MainActivity extends AppCompatActivity {
    private boolean search=true;
    long first_time;
    long second_time;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FragmentTransaction transaction;
    //
//    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainACtivity", "Seungrok");
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        SearchView searchView = findViewById(R.id.searchView);
        Spinner spinner = findViewById(R.id.spinner);
        //시작 프레그먼트 지정
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, homeFragment).commitAllowingStateLoss();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment home =new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, home);
                ft.commit();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteFragment favorite= new FavoriteFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, favorite);
                ft.commit();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    fragmentManager = getSupportFragmentManager();
                    mapFragment=new MapFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, mapFragment);
                    ft.commit();
                } else {
                    if (checkPermission()) {
                        Log.d("onCreate","4");
                        fragmentManager = getSupportFragmentManager();
                        mapFragment=new MapFragment();
                        Log.d("permission","a");
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment, mapFragment);
                        ft.commit();
                        Log.d("permission","b");
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12345);  //request하기
                    }
                }
            }
        });
        String[] category = new String[2];
        category[0] = "영화";
        category[1] = "배우";
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,category);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search= position == 0;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(search){
                    SearchFragment search= new SearchFragment(query);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, search);
                    ft.commit();
                }else{
                    SearchActorFragment search= new SearchActorFragment(query);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, search);
                    ft.commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    } //oncreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save last queries to disk

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //결과받기
        if (requestCode == 12345) {
            if (checkPermission()) {
                Log.d("onCreate","5");
                fragmentManager = getSupportFragmentManager();
                mapFragment=new MapFragment();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment, mapFragment);

            } else {
                Toast.makeText(this, "위치권한 필요", Toast.LENGTH_LONG).show();
                finish(); //MainActivity.java종료(앱종료)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
        if(locationManager != null)
            //위치수신종료
            locationManager.removeUpdates(IListener);

         */
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume(); //꼭필요
        /*
        //위치수신시작
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, IListener); //위치수신시간
         */

    }


    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        if(second_time - first_time < 2000){
            super.onBackPressed();
            finishAffinity();
        }else{
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, homeFragment).commitAllowingStateLoss();
        }
        first_time = System.currentTimeMillis();
    }
//    public boolean CheckAppFirstExecute(){
//        SharedPreferences pref = getSharedPreferences("IsFirst" , Activity.MODE_PRIVATE);
//        boolean isFirst = pref.getBoolean("isFirst", false);
//        if(!isFirst){ //최초 실행시 true 저장
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putBoolean("isFirst", true);
//            editor.commit();
//        }
//        return !isFirst;
//    }
}

