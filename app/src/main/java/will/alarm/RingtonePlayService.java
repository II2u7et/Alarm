package will.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class RingtonePlayService extends Service {

    MediaPlayer song;
    int startId;
    boolean isRuning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        String state = intent.getExtras().getString("extra");
        Integer songID = intent.getExtras().getInt("toneID");

        Log.e("Ringtone state: " , state);

        NotificationManager notiMana = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intentMainActivity = new Intent(this.getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntentMainAct = PendingIntent
                .getActivity(this,0,intentMainActivity,0);

        Notification notiPopup = new Notification.Builder(RingtonePlayService.this)
                .setContentTitle("Alarm is going off!")
                .setContentText("Click me")
                .setContentIntent(pendingIntentMainAct)
                .setSmallIcon(R.drawable.noti_icon)
                .setAutoCancel(true)
                .build();



        assert  state != null;
        switch (state) {
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                Log.e("Start ID state: " , state);
                break;
            default:
                startId = 0;
                break;
        }



        if((!this.isRuning)&&(startId == 1)){
            Log.e("No music", " n alarm should start");

            if(songID == 0){
                song = MediaPlayer.create(RingtonePlayService.this, R.raw.attention);
                song.start();

            }else if( songID == 1){
                song = MediaPlayer.create(RingtonePlayService.this, R.raw.despacito);
                song.start();

            }else if( songID == 2){
                song = MediaPlayer.create(RingtonePlayService.this, R.raw.havana);
                song.start();
            }else if( songID == 3){
                song = MediaPlayer.create(RingtonePlayService.this, R.raw.shape_of_you);
                song.start();
            }else {
                song = MediaPlayer.create(RingtonePlayService.this, R.raw.we_dont_talk_anymore);
                song.start();
            }



            this.isRuning = true;
            this.startId = 0;
            notiMana.notify(0,notiPopup);

        }else if((this.isRuning)&&(startId == 0)){
            Log.e("No music", "n alarm should make no sound");
            song.stop();
            song.reset();

            this.isRuning = false;
            this.startId = 0;

        }else if((!this.isRuning)&&(startId == 0)){
            Log.e("Music", "n alarm should keep going");

            this.isRuning = false;
            this.startId = 0;


        }else if((this.isRuning)&&(startId == 1)){
            Log.e("Music", "n alarm should be stop");
            song.stop();
            song.reset();

            this.isRuning = true;
            this.startId = 1;

        }else {
            Log.e("weirdo", "check this case again");
        }




        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.e("onDestroy", " app closed");
        super.onDestroy();
        this.isRuning = false;

    }




}
