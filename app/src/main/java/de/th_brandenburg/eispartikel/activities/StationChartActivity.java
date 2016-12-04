package de.th_brandenburg.eispartikel.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import datenKlassen.Station;
import datenKlassen.Tageswerte;
import de.th_brandenburg.eispartikel.R;
import de.th_brandenburg.eispartikel.Util;

public class StationChartActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_chart);
        Station station = (Station) getIntent().getSerializableExtra("station");

        ButterKnife.bind(this);

        List<Entry> entries = new ArrayList<>();
        List<Entry> sollEntries = new ArrayList<>();

        for(String tag: Util.oderedTaqe(station.getAktuelleWerte())) {
            Tageswerte tageswerte = station.getAktuelleWerte().get(tag);
            long timestamp = Util.dateToTimestamp(tag);
            entries.add(new Entry(timestamp, tageswerte.getAktuellerWert()));
            sollEntries.add(new Entry(timestamp, station.getVorgabewert()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Tageswerte");
        LineDataSet dataSet2 = new LineDataSet(sollEntries, "Soll Werte");

        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawFilled(true);

        dataSet2.setValueTextColor(Color.WHITE);
        dataSet2.setColor(Color.RED);


        chart.setData(new LineData(dataSet, dataSet2));
        chart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new HourAxisFormatter());

        chart.invalidate(); // refresh
    }

    class HourAxisFormatter extends IndexAxisValueFormatter {
        private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return dateFormat.format(new Date((long) value));
        }
    }
}
