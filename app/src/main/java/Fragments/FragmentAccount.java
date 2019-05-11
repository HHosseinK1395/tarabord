package Fragments;

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


public class FragmentAccount extends android.support.v4.app.Fragment
{

    public EditTextFont edtUserName;
    public TextViewFont txtMobileNumber, txtEmial, txtExitAccount, txtAccountMaaliHistory;
    public ButtonFont btnGotoCreditFragment;
    public ImageView btnEditAccountInfo;
    public static ImageView imgAccountBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View vResult = inflater.inflate(R.layout.fragment_account, container, false);

        // TODO : initialize objects by id
        imgAccountBack = (ImageView) vResult.findViewById(R.id.btn_account_back);
        edtUserName = (EditTextFont) vResult.findViewById(R.id.edt_account_username);
        txtMobileNumber = (TextViewFont) vResult.findViewById(R.id.txt_account_phonenumber);
        txtEmial = (TextViewFont) vResult.findViewById(R.id.txt_account_email);
        btnGotoCreditFragment = (ButtonFont) vResult.findViewById(R.id.btn_account_credit);
        btnEditAccountInfo = (ImageView) vResult.findViewById(R.id.btn_account_editinfo);
        txtExitAccount = (TextViewFont) vResult.findViewById(R.id.txt_account_exit);
        txtAccountMaaliHistory = (TextViewFont) vResult.findViewById(R.id.txt_account_maalihistory);

        imgAccountBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment frgOldFragmentPointer = ActivityMain.frgStack.pop();
                ActivityMain.frgmgrGlobal.beginTransaction().replace(R.id.frame_container, frgOldFragmentPointer).commit();
            }
        });


        btnEditAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtExitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtAccountMaaliHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return vResult;
    }
}
