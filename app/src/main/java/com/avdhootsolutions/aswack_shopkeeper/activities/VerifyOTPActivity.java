package com.avdhootsolutions.aswack_shopkeeper.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.avdhootsolutions.aswack_shopkeeper.R;

public class VerifyOTPActivity extends AppCompatActivity {
    private Context mContext;
    TextView tvSubmit,tvResendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mContext = VerifyOTPActivity.this;
        initView();
    }

    private void initView() {
        tvSubmit = findViewById(R.id.tvSubmit);
        tvResendOTP = findViewById(R.id.tvResendOTP);

        tvResendOTP.setPaintFlags(tvResendOTP.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SignUpActivity.class));
            }
        });
    }
}