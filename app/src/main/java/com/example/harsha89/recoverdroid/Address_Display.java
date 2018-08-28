package com.example.harsha89.recoverdroid;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Address_Display extends AppCompatActivity {

    GPStracker gpStracker;
    TextView tAddress;
    String result;
    String toMail="harshavardhanreddy1995@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address__display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tAddress= (TextView) findViewById(R.id.textView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        gpStracker = new GPStracker(Address_Display.this);
        if (gpStracker.canGetLocation()) {
            double latitude = gpStracker.getLatitude();
            double longitude = gpStracker.getLongitude();
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> locationdetails = gcd.getFromLocation(latitude, longitude, 1);
                if (locationdetails != null && locationdetails.size() > 0) {
                    android.location.Address address = locationdetails.get(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Address:").append("\n");
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
//                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    result = sb.toString();
                }
            } catch (Exception e) {
            }
        }
        tAddress.setText(result);
    }

    public void sendMail(View view){

        MailLgic mail = new MailLgic();
        try{
            mail.setToMail(toMail);
            mail.setSubject("RecoverDroid Data");
            mail.setMailDescription("This data came from your mobile.\n" + result);
            mail.send();
            Toast.makeText(getApplicationContext(),"Mail sent Successfully",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
