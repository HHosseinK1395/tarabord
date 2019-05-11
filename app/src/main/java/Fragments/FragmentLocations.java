package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.R;

public class FragmentLocations extends android.support.v4.app.Fragment
{
    public static ImageView imgLocationsBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View vResult = inflater.inflate(R.layout.fragment_locations, container, false);

        imgLocationsBack = (ImageView) vResult.findViewById(R.id.btn_locations_back);

        imgLocationsBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment frgOldFragmentPointer = ActivityMain.frgStack.pop();
                ActivityMain.frgmgrGlobal.beginTransaction().replace(R.id.frame_container, frgOldFragmentPointer).commit();
            }
        });

        return vResult;
    }
}
