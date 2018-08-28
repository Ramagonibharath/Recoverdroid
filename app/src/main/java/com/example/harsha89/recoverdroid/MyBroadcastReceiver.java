package com.example.harsha89.recoverdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Harsha89 on 15-04-2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://pickaro.in/recover.php";
    String replaceID = "0";
    String action = "0";
    int success;
    int normal = 2;
    int vibrate = 1;
    int silent = 0;
    MailLgic mail;
    Geocoder gcd;
    GPStracker gpStracker;
//    TextView tAddress;
    String result;
    String toMail="harshavardhanreddy1995@gmail.com";
    private static final String TAG_ACTION = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "I m Running", Toast.LENGTH_LONG).show();
        gpStracker = new GPStracker(context);
        mail = new MailLgic();
        gcd = new Geocoder(context, Locale.getDefault());
            AttemptRecover recover =new AttemptRecover(context);
            recover.execute();

    }
    class AttemptRecover extends AsyncTask<String,String,String>{
        Context context;
        public AttemptRecover(Context context) {
            this.context=context;
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("replaceID", replaceID));
                param.add(new BasicNameValuePair("action",action));
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", param);
                success = json.getInt(TAG_ACTION);

                if(success==3){
                    AudioManager audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
                    int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Ringtone r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                    return json.getString(TAG_MESSAGE);
                }else if(success==2){

                    if (gpStracker.canGetLocation()) {
//                        Toast.makeText(context,"why inside",Toast.LENGTH_LONG).show();
                        double latitude = gpStracker.getLatitude();
                        double longitude = gpStracker.getLongitude();

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
                                sb.append(address.getCountryName());
                                result = sb.toString();
//                                Toast.makeText(context,"why"+latitude+longitude,Toast.LENGTH_LONG).show();
                            }
                                    try{
                                        mail.setToMail(toMail);
                                        mail.setSubject("RecoverDroid Data");
                                        mail.setMailDescription("This data came from your mobile.\n" + result);
                                        mail.send();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                         }catch (Exception e) {
                        }
                    }
                    return json.getString(TAG_MESSAGE);
                }else if(success==1){
                    AudioManager audioMngr = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
                    if (audioMngr.getRingerMode() == normal) {
                        audioMngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                        Toast.makeText(context, "Normal to Silent", Toast.LENGTH_LONG).show();
                    } else if (audioMngr.getRingerMode() == silent) {
                        audioMngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        Toast.makeText(context, "Silent to Normal", Toast.LENGTH_LONG).show();
                    } else if (audioMngr.getRingerMode() == vibrate) {
                        audioMngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                        Toast.makeText(context, "Silent to Normal", Toast.LENGTH_LONG).show();
                    }
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            if(message!=null){
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        }
    }
}
