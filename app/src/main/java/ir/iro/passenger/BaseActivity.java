package ir.iro.passenger;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Components.TextViewFont;
import Fragments.FragmentBottomSheetAboutUs;
import Utilities.Utility;

/**
 * Created by N550J on 2/7/2019.
 */

public class BaseActivity extends AppCompatActivity {

    private float BOTTOM_MENU_NORMAL_SCALE = 1f, BOTTOM_MENU_CLICK_SCALE = 1.1f;

    private ImageView imgMap;
    private RelativeLayout rvAutomaticPayment, rvHandyPayment, rvWardensPayment, rvTravelPlan;
    private List<LatLng> lstAutomatic, lstHandy, lstWardens, lstTravel;
    private Timer timer;

    private LinearLayout lnrHome, lnrAboutUs, lnrSettings, lnrReports, lnrProfile;
    private ImageView imgHome, imgAboutUs, imgSettings, imgReports, imgProfile;
    private TextViewFont txtHome, txtAboutUs, txtSettings, txtReports, txtProfile;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        imgMap = findViewById(R.id.img_goto_map);

        rvAutomaticPayment = findViewById(R.id.rv_automatic_toll_payment_parent);
        rvHandyPayment = findViewById(R.id.rv_handy_toll_payment_parent);
        rvWardensPayment = findViewById(R.id.rv_wardens_payment_parent);
        rvTravelPlan = findViewById(R.id.rv_travel_plan_parent);
        lnrHome = findViewById(R.id.lnr_home);
        lnrAboutUs = findViewById(R.id.lnr_about_us);
        lnrSettings = findViewById(R.id.lnr_settings);
        lnrReports = findViewById(R.id.lnr_reports);
        lnrProfile = findViewById(R.id.lnr_profile);

        imgHome = findViewById(R.id.img_home);
        imgAboutUs = findViewById(R.id.img_about_us);
        imgSettings = findViewById(R.id.img_settings);
        imgReports = findViewById(R.id.img_reports);
        imgProfile = findViewById(R.id.img_profile);

        txtHome = findViewById(R.id.txt_home);
        txtAboutUs = findViewById(R.id.txt_about_us);
        txtSettings = findViewById(R.id.txt_settings);
        txtReports = findViewById(R.id.txt_reports);
        txtProfile = findViewById(R.id.txt_profile);

        initPage();

    }


    private void initPage() {

        timer = new Timer();

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, ActivityMain.class);
                startActivity(intent);

            }
        });

        rvAutomaticPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTrafficData();
                getHandyTollData();
                getAutomaticTollData();
                Intent intent = new Intent(BaseActivity.this, ActivityMain.class);
                intent.putExtra(Utility.MAP_INTENT_TRAVE_PLAN, new Gson().toJson(lstTravel));
                intent.putExtra(Utility.MAP_INTENT_HANDY_TOLL, new Gson().toJson(lstHandy));
                intent.putExtra(Utility.MAP_INTENT_AUTOMATIC_TOLL, new Gson().toJson(lstAutomatic));
                startActivity(intent);
            }
        });

        rvHandyPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTrafficData();
                getHandyTollData();
                Intent intent = new Intent(BaseActivity.this, ActivityMain.class);
                intent.putExtra(Utility.MAP_INTENT_TRAVE_PLAN, new Gson().toJson(lstTravel));
                intent.putExtra(Utility.MAP_INTENT_HANDY_TOLL, new Gson().toJson(lstHandy));
                startActivity(intent);
            }
        });

        rvWardensPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWardensData();
                Intent intent = new Intent(BaseActivity.this, ActivityMain.class);
                intent.putExtra(Utility.MAP_INTENT_WARDENS, new Gson().toJson(lstWardens));
                startActivity(intent);
            }
        });

        rvTravelPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getTrafficData();
//                Intent intent = new Intent(BaseActivity.this, ActivityMain.class);
//                intent.putExtra(Utility.MAP_INTENT_TRAVE_PLAN, new Gson().toJson(lstTravel));
//                startActivity(intent);
                Utility.ShowSnakMessage(
                        BaseActivity.this,
                        getWindow().getDecorView().findViewById(android.R.id.content),
                        R.string.service_not_access,
                        Snackbar.LENGTH_LONG);
            }
        });

        //Bottom Menu
        lnrHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleView(lnrHome, BOTTOM_MENU_NORMAL_SCALE, BOTTOM_MENU_CLICK_SCALE);
                scaleView(lnrHome, BOTTOM_MENU_CLICK_SCALE, BOTTOM_MENU_NORMAL_SCALE);
                bottomMenuManagement(true, false, false, false, false);
            }
        });

        lnrAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleView(lnrAboutUs, BOTTOM_MENU_NORMAL_SCALE, BOTTOM_MENU_CLICK_SCALE);
                scaleView(lnrAboutUs, BOTTOM_MENU_CLICK_SCALE, BOTTOM_MENU_NORMAL_SCALE);
                bottomMenuManagement(false, true, false, false, false);

                FragmentBottomSheetAboutUs bottomSheetAboutUs = new FragmentBottomSheetAboutUs();
                bottomSheetAboutUs.show(getSupportFragmentManager(), "Yes");
            }
        });

        lnrSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleView(lnrSettings, BOTTOM_MENU_NORMAL_SCALE, BOTTOM_MENU_CLICK_SCALE);
                scaleView(lnrSettings, BOTTOM_MENU_CLICK_SCALE, BOTTOM_MENU_NORMAL_SCALE);
                bottomMenuManagement(false, false, true, false, false);

                FragmentBottomSheetAboutUs bottomSheetAboutUs = new FragmentBottomSheetAboutUs();
                bottomSheetAboutUs.show(getSupportFragmentManager(), "Yes");
            }
        });

        lnrReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleView(lnrReports, BOTTOM_MENU_NORMAL_SCALE, BOTTOM_MENU_CLICK_SCALE);
                scaleView(lnrReports, BOTTOM_MENU_CLICK_SCALE, BOTTOM_MENU_NORMAL_SCALE);
                bottomMenuManagement(false, false, false, true, false);

                FragmentBottomSheetAboutUs bottomSheetAboutUs = new FragmentBottomSheetAboutUs();
                bottomSheetAboutUs.show(getSupportFragmentManager(), "Yes");
            }
        });

        lnrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleView(lnrProfile, BOTTOM_MENU_NORMAL_SCALE, BOTTOM_MENU_CLICK_SCALE);
                scaleView(lnrProfile, BOTTOM_MENU_CLICK_SCALE, BOTTOM_MENU_NORMAL_SCALE);
                bottomMenuManagement(false, false, false, false, true);

                FragmentBottomSheetAboutUs bottomSheetAboutUs = new FragmentBottomSheetAboutUs();
                bottomSheetAboutUs.show(getSupportFragmentManager(), "Yes");
            }
        });

        menuController();
        lnrHome.callOnClick();

    }

    void bottomMenuManagement(boolean bHome, boolean bAboutUs, boolean bSettings, boolean bReports, boolean bProfile) {
        if(bHome){
            imgHome.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txtHome.setTextColor(getResources().getColor(R.color.white));

            imgAboutUs.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtAboutUs.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgSettings.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtSettings.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgReports.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtReports.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtProfile.setTextColor(getResources().getColor(R.color.text_color_light_gray));
        }

        if(bAboutUs){
            imgHome.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtHome.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgAboutUs.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txtAboutUs.setTextColor(getResources().getColor(R.color.white));

            imgSettings.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtSettings.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgReports.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtReports.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtProfile.setTextColor(getResources().getColor(R.color.text_color_light_gray));
        }

        if(bSettings){
            imgHome.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtHome.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgAboutUs.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtAboutUs.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgSettings.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txtSettings.setTextColor(getResources().getColor(R.color.white));

            imgReports.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtReports.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtProfile.setTextColor(getResources().getColor(R.color.text_color_light_gray));
        }

        if(bReports){
            imgHome.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtHome.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgAboutUs.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtAboutUs.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgSettings.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtSettings.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgReports.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txtReports.setTextColor(getResources().getColor(R.color.white));

            imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtProfile.setTextColor(getResources().getColor(R.color.text_color_light_gray));
        }

        if(bProfile){
            imgHome.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtHome.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgAboutUs.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtAboutUs.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgSettings.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtSettings.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgReports.setColorFilter(ContextCompat.getColor(this, R.color.text_color_light_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            txtReports.setTextColor(getResources().getColor(R.color.text_color_light_gray));

            imgProfile.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txtProfile.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    private void getWardensData() {
        lstWardens = new ArrayList<>();
        lstWardens.add(new LatLng(35.784133, 51.453727));
        lstWardens.add(new LatLng(35.771373, 51.439200));
        lstWardens.add(new LatLng(35.762119, 51.459799));
        lstWardens.add(new LatLng(35.759725, 51.478864));
        lstWardens.add(new LatLng(35.748647, 51.474579));
        lstWardens.add(new LatLng(35.730017, 51.458781));
        lstWardens.add(new LatLng(35.706071, 51.452350));
        lstWardens.add(new LatLng(35.700277, 51.428457));
        lstWardens.add(new LatLng(35.708288, 51.406367));
        lstWardens.add(new LatLng(35.716891, 51.385568));
        lstWardens.add(new LatLng(35.721488, 51.382564));
        lstWardens.add(new LatLng(35.736955, 51.400587));
        lstWardens.add(new LatLng(35.759795, 51.409406));
        lstWardens.add(new LatLng(35.784760, 51.427377));
        lstWardens.add(new LatLng(35.793141, 51.446861));
        lstWardens.add(new LatLng(35.726321, 51.381258));
        lstWardens.add(new LatLng(35.733697, 51.387491));
        lstWardens.add(new LatLng(35.737170, 51.393609));
        lstWardens.add(new LatLng(35.739702, 51.399281));
        lstWardens.add(new LatLng(35.742750, 51.407918));
    }

    private void getHandyTollData() {
        lstHandy = new ArrayList<>();
        lstHandy.add(new LatLng(35.744725, 51.778087));
        lstHandy.add(new LatLng(35.732138, 51.178906));
        lstHandy.add(new LatLng(35.759795, 51.409406));
        lstHandy.add(new LatLng(35.784760, 51.427377));


        lstHandy.add(new LatLng(35.745591, 51.780883));//Pardis Toll
        lstHandy.add(new LatLng(35.542039, 51.354016));//Tehran Ghom Toll
        lstHandy.add(new LatLng(36.253479, 50.114243));//Tehran Karaj Ghazvin Toll

        lstHandy.add(new LatLng(35.785695, 51.491164));//East Sadr Toll
        lstHandy.add(new LatLng(35.779962, 51.431517));//West Sadr Toll

        lstHandy.add(new LatLng(35.754237, 51.452816));//East Hemmat Toll
        lstHandy.add(new LatLng(35.760546, 51.249036));//West Hemmat Toll

        lstHandy.add(new LatLng(35.742015, 51.399654));//East Hakim Toll
        lstHandy.add(new LatLng(35.735858, 51.314628));//West Hakim Toll

        lstHandy.add(new LatLng(35.706044, 51.377751));//East Hakim Toll
        lstHandy.add(new LatLng(35.711358, 51.377890));//West Hakim Toll


    }

    private void getTrafficData() {
        lstTravel = new ArrayList<>();
        lstTravel.add(new LatLng(35.701763, 51.418250));
        lstTravel.add(new LatLng(35.712357, 51.377802));
        lstTravel.add(new LatLng(35.700178, 51.359155));
        lstTravel.add(new LatLng(35.680816, 51.366204));
        lstTravel.add(new LatLng(35.665485, 51.393369));
        lstTravel.add(new LatLng(35.668283, 51.420803));
    }

    private void getAutomaticTollData() {
        lstAutomatic = new ArrayList<>();
        lstAutomatic.add(new LatLng(35.744725, 51.778087));
        lstAutomatic.add(new LatLng(35.732138, 51.178906));
        lstAutomatic.add(new LatLng(35.759795, 51.409406));
        lstAutomatic.add(new LatLng(35.784760, 51.427377));
    }


    void menuController() {
        timer.schedule(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        PowerManager powerManager = (PowerManager)
                                getSystemService(Context.POWER_SERVICE);

//                        if ((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
//                                && powerManager.isPowerSaveMode()) || !areSystemAnimationsEnabled()) {
                        CircleLayout pie = (CircleLayout) findViewById(R.id.pie);
                        ImageView centerImage = (ImageView) findViewById(R.id.image);
                        float scale;

                        float pieCenterX = (pie.getMeasuredWidth() / 2);
                        float pieCenterY = (pie.getMeasuredHeight() / 2);
                        final int metricBase = Math.min(pie.getMeasuredWidth(), pie.getMeasuredHeight());
                        for (int i = 0; i < pie.getChildCount(); i++) {

                            View child = pie.getChildAt(i);

                            child.setX(pieCenterX - child.getWidth() / 2);
                            child.setY(pieCenterY - child.getHeight() / 2);
                            MainTestInterpolator interpolator = new MainTestInterpolator();

                            int cWidth = child.getMeasuredWidth();
                            scale = (float) (metricBase) / (5 * cWidth);
                            child.setScaleX(scale);
                            child.setScaleY(scale);
                            child.animate()
                                    .translationX(0)
                                    .translationY(0)
                                    .setInterpolator(interpolator)
                                    .setDuration(50)
                                    .start();
                        }
                        pie.setVisibility(View.VISIBLE);
                        //}
                    }
                });
            }
        }, 100);
    }

    private boolean areSystemAnimationsEnabled() {
        float duration, transition;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            duration = Settings.Global.getFloat(getContentResolver(),
                    Settings.Global.ANIMATOR_DURATION_SCALE, 1);
            transition = Settings.Global.getFloat(getContentResolver(),
                    Settings.Global.TRANSITION_ANIMATION_SCALE, 1);
        } else {
            duration = Settings.System.getFloat(getContentResolver(),
                    Settings.System.ANIMATOR_DURATION_SCALE, 1);
            transition = Settings.System.getFloat(getContentResolver(),
                    Settings.System.TRANSITION_ANIMATION_SCALE, 1);
        }
        return (duration != 0 && transition != 0);
    }

    private class MainTestInterpolator implements TimeInterpolator {
        @Override
        public float getInterpolation(float input) {
            if (input < 0.3)
                return input * 17 / 6;

            float in = (input - 0.3f) * 10 / 7;
            return (float) (0.85 + Math.sin(Math.toRadians(360) * in) / 6);
        }
    }

}