package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity{

    private TextView textIn, textOut;
    private View view;
    private boolean lastScanButtonWasIn = true;
    String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //textIn = findViewById(R.id.resultIn);
        //textOut = findViewById(R.id.resultOut);

        ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        System.out.println(textIn );

    }

    public void scanIn(View view) {
        lastScanButtonWasIn = true;
        launchScan();
    }

    public void scanOut(View view) {
        lastScanButtonWasIn = false;
        launchScan();
    }

    private void launchScan() {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult( requestCode, resultCode, data);


        if (intentResult != null) {
            if (intentResult.getContents() == null) {


                textIn = findViewById(R.id.resultIn);
                setContentView(R.layout.timeout_fragment);
                textOut = findViewById(R.id.resultOut);

                textIn.setText("Eek Error");
                textOut.setText("Eek Error");
            } else {


                JsonObject jsonObject = new JsonParser().parse(intentResult.getContents()).getAsJsonObject();

                final String id = jsonObject.getAsJsonObject("ronaTicket").get("id").getAsString();
                final String name = jsonObject.getAsJsonObject("ronaTicket").get("name").getAsString();

                // either
                String sPersonDirection = lastScanButtonWasIn ? "in" : "out";

                // or
                if(lastScanButtonWasIn) {
                    // handle in

                    EventBus.getDefault().post(new SendTimeIn("Verifying and Clocking"));

                    //setContentView(R.layout.timein_fragment);
                    //textIn = findViewById(R.id.resultIn);
                    //textIn.setText("Verifying and Clocking");
                    timein in = new timein(this);
                    in.execute(id, name);
                } else {
                    // handle out

                    EventBus.getDefault().post(new SendTimeOut("Verifying and Clocking"));

                    timeout out = new timeout(this);
                    out.execute(id);
                }

                // ^ or both



            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class SendTimeIn {
        private String text;

    public SendTimeIn(String text){
        this.text=text;
    }

    public String getText(){
        return text;
    }

    public void setText(String text) {
        this.text=text;
    }

    }

    public class SendTimeOut {
        private String text;

        public SendTimeOut(String text){
            this.text=text;
        }

        public String getText(){
            return text;
        }

        public void setText(String text) {
            this.text=text;
        }

    }

}

