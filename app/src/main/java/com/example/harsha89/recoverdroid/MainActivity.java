package com.example.harsha89.recoverdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    int normal = 2;
    int vibrate = 1;
    int silent = 0;
    GPStracker gpStracker;
//    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        startAlert();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            super.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAlert() {

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),1000, pendingIntent);
    }

    public void ring(View view) {
        AudioManager audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume, AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    public void changeMode(View view) {

        AudioManager audioMngr = (AudioManager) getBaseContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        if (audioMngr.getRingerMode() == normal) {
            audioMngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            Toast.makeText(getApplicationContext(), "Normal to Silent", Toast.LENGTH_LONG).show();
        } else if (audioMngr.getRingerMode() == silent) {
            audioMngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(getApplicationContext(), "Silent to Normal", Toast.LENGTH_LONG).show();
        } else if (audioMngr.getRingerMode() == vibrate) {
            audioMngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(getApplicationContext(), "Silent to Normal", Toast.LENGTH_LONG).show();
        }
        //And after do all your jobs..... you can return to previous mode:
//        AUDIOMANAGER.setRingerMode(RingerMode);
    }


    public void GetLocation(View view) {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            AlertDialog.Builder locationset = new AlertDialog.Builder(this);
            locationset.setTitle("Use location?");
            /*aboutus.setIcon(android.R.drawable.ic_dialog_alert);*/
            locationset.setMessage("This app wants to change your device settings");
            locationset.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            locationset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            locationset.create();
            locationset.show();
        }else{
            Intent in=new Intent(getBaseContext(),Address_Display.class);
            startActivity(in);
        }

    }
}


