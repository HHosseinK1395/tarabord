package Fragments;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import Components.TextViewFont;
import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.R;

public class FragmentShare extends android.support.v4.app.Fragment
{
    public static ImageView imgShareBack;
    public TextViewFont txtShareCode;
    public Button btnShareTelegram, btnShareEmail, btnShareSMS, btnShareOthers;

    private String strShareMessage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View vResult = inflater.inflate(R.layout.fragment_share, container, false);

        // TODO : initialize objects by id
        imgShareBack = (ImageView) vResult.findViewById(R.id.btn_share_back);
        btnShareTelegram = (Button) vResult.findViewById(R.id.btn_share_telegram);
        btnShareEmail = (Button) vResult.findViewById(R.id.btn_share_email);
        btnShareSMS = (Button) vResult.findViewById(R.id.btn_share_sms);
        btnShareOthers = (Button) vResult.findViewById(R.id.btn_share_others);
        txtShareCode = (TextViewFont) vResult.findViewById(R.id.txt_share_code);

        strShareMessage = "کد دعوت شما :" + txtShareCode.getText();

        imgShareBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment frgOldFragmentPointer = ActivityMain.frgStack.pop();
                ActivityMain.frgmgrGlobal.beginTransaction().replace(R.id.frame_container, frgOldFragmentPointer).commit();
            }
        });


        btnShareTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShareMessage(strShareMessage, 1);
            }
        });

        btnShareEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShareMessage(strShareMessage, 2);
            }
        });

        btnShareSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShareMessage(strShareMessage, 3);
            }
        });

        btnShareOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentShareMessage(strShareMessage, 4);
            }
        });

        return vResult;
    }
    public void intentShareMessage(String msg, int appType)
    {
        String appName = "";

        switch (appType)
        {
            case 1: // Telegram
            {
                appName = "org.telegram.messenger";
            }
            break;
            case 2: // Email
            {
                appName = "org.telegram.messenger";
            }
            break;
            case 3: // SMS
            {
                appName = "org.telegram.messenger";
            }
            break;
            case 4: // Others
            {
                appName = "org.telegram.messenger";
            }
            break;
        }

        final boolean isAppInstalled = isAppAvailable(ActivityMain.GlobalActivityContext, appName);
        if (isAppInstalled)
        {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage(appName);
            myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
            startActivity(Intent.createChooser(myIntent, "Share with"));
        }
        else
        {
            Toast.makeText(ActivityMain.GlobalActivityContext, "Telegram not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppAvailable(Context context, String appName)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
