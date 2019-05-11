package ir.iro.passenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import Components.ButtonFont;
import Components.TextViewFont;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by N550J on 1/13/2019.
 */

public class PaymentActivity extends Activity implements View.OnClickListener {
    CircleImageView imgIncreaseCredit;
    ButtonFont btnPay;
    String newString;
    TextViewFont txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("page_type");
        }


        btnPay = findViewById(R.id.btn_pay);
        imgIncreaseCredit = findViewById(R.id.img_credit_ok_increase);
        txtDescription = findViewById(R.id.txt_credit_description);

        //Init
        Init();
    }

    void Init() {
        btnPay.setOnClickListener(this);
        imgIncreaseCredit.setOnClickListener(this);
        setupPage(newString);
    }

    void setupPage(String pageType) {
        if (pageType.equals("عوارضی")) {
            txtDescription.setText("بابت عوارض اتوبان");
        }

        if(pageType.equals("پارکبان")){
            txtDescription.setText("بابت پارکینگ");
        }

        if(pageType.equals("طرح ترافیک")){
            txtDescription.setText("بابت طرح ترافیک");
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay: {
                Toast.makeText(this, "پرداخت با موفقیت انجام شد", Toast.LENGTH_LONG).show();
            }
            break;
            case R.id.img_credit_ok_increase: {
                Intent intent = new Intent(PaymentActivity.this, IncreaseCreditActivity.class);
                startActivity(intent);
            }
            break;
            default: {

            }
            break;
        }
    }
}
