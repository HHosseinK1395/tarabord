package Fragments;
//===================================================================================== Privates
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import Components.ButtonFont;
import Components.EditTextFont;
import Components.TextViewFont;
import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.R;
//===================================================================================== Privates
public class FragmentCredit extends android.support.v4.app.Fragment
{
    public ButtonFont btnIncrease;
    public TextViewFont btn20000Toman, btn50000Toman, btn100000Toman;
    public EditTextFont edtCreditValue;
    public static ImageView imgCreditBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View vResult = inflater.inflate(R.layout.fragment_credit, container, false);

        // TODO : initialize objects by id
        imgCreditBack = (ImageView) vResult.findViewById(R.id.btn_credit_back);
        btnIncrease = (ButtonFont) vResult.findViewById(R.id.btn_credit_ok_increase);
        btn20000Toman = (TextViewFont) vResult.findViewById(R.id.txt_credit_20000_toman);
        btn50000Toman = (TextViewFont) vResult.findViewById(R.id.txt_credit_50000_toman);
        btn100000Toman = (TextViewFont) vResult.findViewById(R.id.txt_credit_100000_toman);
        edtCreditValue = (EditTextFont) vResult.findViewById(R.id.edt_credit_value);

        // TODO : other init process
        edtCreditValue.setText("");

        imgCreditBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment frgOldFragmentPointer = ActivityMain.frgStack.pop();
                ActivityMain.frgmgrGlobal.beginTransaction().replace(R.id.frame_container, frgOldFragmentPointer).commit();
            }
        });

        btn20000Toman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCreditValue.setText("20000");
            }
        });

        btn50000Toman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCreditValue.setText("50000");
            }
        });

        btn100000Toman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCreditValue.setText("100000");
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : goto web link for increase credit
            }
        });


        // TODO : For disable global TouchListner for

        return vResult;
    }
}
