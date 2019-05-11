package ir.iro.passenger;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.Stack;

import Components.EditTextFont;
import Fragments.FragmentMap;
import Utilities.Utility;

public class ActivityMain extends FragmentActivity {

    private EditTextFont edtSearchText;
    public static SharedPreferences spGlobalSharedpreferences;
    public static SharedPreferences.Editor speUserCurrentLocation;
    public static ActivityMain GlobalActivityContext;
    public static FragmentManager frgmgrGlobal;
    public static Stack<Fragment> frgStack;
    public static Fragment frgMap;
    private boolean bDoubleBackToExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!googlePlayServiceExist()) {
            Utility.ShowSnakMessage(this, getWindow().getDecorView().findViewById(android.R.id.content), R.string.google_play_services_not_exist, Snackbar.LENGTH_LONG);
            return;
        }

        //init
        init();


    }

    void init() {
        Bundle bundle = getIntent().getExtras();
        bDoubleBackToExit = false;
        FragmentMap.lstLocationNameList = new ArrayList<String>();
        GlobalActivityContext = this;

        frgMap = new FragmentMap();
        frgStack = new Stack<Fragment>();

        spGlobalSharedpreferences = getSharedPreferences(
                getResources().getString(R.string.shared_prefrences_name),
                Context.MODE_PRIVATE
        );


        if (Utility.isNetworkAvailable(getApplicationContext())) {
            frgmgrGlobal = getSupportFragmentManager();
            if (bundle != null)
                frgMap.setArguments(bundle);
            frgmgrGlobal.beginTransaction().replace(R.id.frame_container, frgMap).commit();
        } else {
            Toast.makeText(this, "لطفا اینترنت خود را روشن کرده و برنامه را مجددا اجرا نمایید.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean googlePlayServiceExist() {
        int GPS_ERRORLOADING_REQUEST = 9001, isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORLOADING_REQUEST);
            dialog.show();
        } else {
            //Show error
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (frgStack != null && frgStack.size() > 0) {
            Fragment frgOldFragmentPointer = frgStack.pop();
            frgmgrGlobal.beginTransaction().replace(R.id.frame_container, frgOldFragmentPointer).commit();
            return;
        }
        if (frgStack != null && frgStack.size() <= 0) {
            //SaveMapInfoBeforExit();

            if (bDoubleBackToExit) {
                super.onBackPressed();
            } else {
                this.bDoubleBackToExit = true;
                Utility.ShowSnakMessage(this, getWindow().getDecorView().findViewById(android.R.id.content), R.string.double_tap_for_exit, Snackbar.LENGTH_LONG);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bDoubleBackToExit = false;
                    }
                }, 2000);
            }
        }
    }

    public static float Distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length <= 0) {
            return;
        }

        if ((requestCode == Utility.PERMISSION_LOCATION)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ((FragmentMap) frgMap).enableGpsAndTrack();
            } else {
                Toast.makeText(this, "دسترسی به مکان قابل انجام نیست لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

