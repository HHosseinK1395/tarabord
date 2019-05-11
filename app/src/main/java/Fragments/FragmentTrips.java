package Fragments;
//===================================================================================== Privates
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.R;

//===================================================================================== Privates
public class FragmentTrips extends android.support.v4.app.Fragment
{
    public static ImageView imgTripsBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View vResult = inflater.inflate(R.layout.fragment_trips, container, false);

        imgTripsBack = (ImageView) vResult.findViewById(R.id.btn_trips_back);

        imgTripsBack.setOnClickListener(new View.OnClickListener()
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
