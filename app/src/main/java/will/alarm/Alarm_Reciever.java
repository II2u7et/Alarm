package will.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm_Reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Log.e("Hi, receiver here","whatever");

        String getString = intent.getExtras().getString("extra");

        Log.e("Status",getString);

        Integer toneChoice = intent.getExtras().getInt("toneID");
        Log.e("Tone ID: ", toneChoice.toString());

        Intent service_intent = new Intent(context, RingtonePlayService.class);

        service_intent.putExtra("extra",getString);

        service_intent.putExtra("toneID", toneChoice);

        context.startService(service_intent);

    }
}
