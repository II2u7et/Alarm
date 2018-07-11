package will.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    AlarmManager alarm_manager;
    Number hour;
    Number minute;
    TextView update_text;
    TimePicker alarm_timePicker;
    Context context;
    PendingIntent pending_intent;
    int toneOpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timePicker = (TimePicker) findViewById(R.id.timePicker);
        update_text = (TextView) findViewById(R.id.updateTxt);

        final Calendar calendar = Calendar.getInstance();



        Button set_alarm = (Button) findViewById(R.id.setBtn);
        Button off_alarm = (Button) findViewById(R.id.offBtn);

        final Intent my_Intent = new Intent(MainActivity.this, Alarm_Reciever.class);



        Spinner spinner = (Spinner) findViewById(R.id.spinnerTone);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.toneArray, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        set_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY,alarm_timePicker.getHour());
                calendar.set(Calendar.MINUTE,alarm_timePicker.getMinute());

                int hour = alarm_timePicker.getHour();
                int minute = alarm_timePicker.getMinute();

                String hour_str = String.valueOf(hour);
                String minute_str = String.valueOf(minute);

                if( hour > 12){
                    hour_str = String.valueOf(hour -12);
                }

                if( minute < 10){
                    minute_str = "0" + String.valueOf(minute);
                }
                set_alarm_text("Alarm set to: " + hour_str + " : " + minute_str);

                my_Intent.putExtra("extra", "on");
                my_Intent.putExtra("toneID", toneOpt);

               pending_intent = PendingIntent.getBroadcast(MainActivity.this,0,my_Intent,PendingIntent.FLAG_UPDATE_CURRENT);

               alarm_manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pending_intent);
            }
        });

        off_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set_alarm_text("Alarm off!");

                alarm_manager.cancel(pending_intent);

                my_Intent.putExtra("extra", "off");
                my_Intent.putExtra("toneID", toneOpt);

                sendBroadcast(my_Intent);
            }
        });


    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        toneOpt = (int) id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
