package ir.iro.passenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import Components.ButtonFont;
import Components.EditTextFont;
import Components.TextViewFont;

/**
 * Created by N550J on 1/14/2019.
 */

public class IncreaseCreditActivity extends Activity {

    public ButtonFont btnIncrease;
    public TextViewFont btn20000Toman, btn50000Toman, btn100000Toman;
    public EditTextFont edtCreditValue;
    public static ImageView imgCreditBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_credit);

        imgCreditBack = (ImageView) findViewById(R.id.btn_credit_back);
        btnIncrease = (ButtonFont) findViewById(R.id.btn_credit_ok_increase);
        btn20000Toman = (TextViewFont) findViewById(R.id.txt_credit_20000_toman);
        btn50000Toman = (TextViewFont) findViewById(R.id.txt_credit_50000_toman);
        btn100000Toman = (TextViewFont) findViewById(R.id.txt_credit_100000_toman);
        edtCreditValue = (EditTextFont) findViewById(R.id.edt_credit_value);

        //Init
        Init();

    }

    void Init()
    {
        // TODO : other init process
        edtCreditValue.setText("");

        imgCreditBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
        public void onClick(View v)
        {

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

            }
        });

    }
}
