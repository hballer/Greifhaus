package de.greifhaus.greifhaus;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.util.Calendar;

public class TimesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView dayName,dayDates;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                dayName = (TextView) findViewById(R.id.textMo);
                dayDates = (TextView) findViewById(R.id.textMoTimes);
                break;

            case Calendar.TUESDAY:
                dayName = (TextView) findViewById(R.id.textDi);
                dayDates = (TextView) findViewById(R.id.textDiTimes);
                break;

            case Calendar.WEDNESDAY:
                dayName = (TextView) findViewById(R.id.textMi);
                dayDates = (TextView) findViewById(R.id.textMiTimes);
                break;
            case Calendar.THURSDAY:
                dayName = (TextView) findViewById(R.id.textDo);
                dayDates = (TextView) findViewById(R.id.textDoTimes);
                break;
            case Calendar.FRIDAY:
                dayName = (TextView) findViewById(R.id.textFr);
                dayDates = (TextView) findViewById(R.id.textFrTimes);
                break;
            case Calendar.SATURDAY:
                dayName = (TextView) findViewById(R.id.textSa);
                dayDates = (TextView) findViewById(R.id.textSaTimes);
                break;
            case Calendar.SUNDAY:
                dayName = (TextView) findViewById(R.id.textSo);
                dayDates = (TextView) findViewById(R.id.textSoTimes);
                break;
            default:
                dayName = (TextView) findViewById(R.id.textMo);
                dayDates = (TextView) findViewById(R.id.textMoTimes);
                break;
        }
        dayName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        dayName.setTypeface(null, Typeface.BOLD);
        dayDates.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        dayDates.setTypeface(null, Typeface.BOLD);
    }
}
