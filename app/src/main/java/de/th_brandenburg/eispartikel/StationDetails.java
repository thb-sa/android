package de.th_brandenburg.eispartikel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import datenKlassen.Station;

public class StationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);

        Station station = (Station) getIntent().getSerializableExtra("station");

        getSupportActionBar().setTitle(station.getStationID());
        Toast.makeText(this, station.getStationID(), Toast.LENGTH_SHORT).show();
    }
}
