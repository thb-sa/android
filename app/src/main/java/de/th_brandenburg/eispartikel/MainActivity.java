package de.th_brandenburg.eispartikel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import datenKlassen.Station;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Station> stations = new ArrayList<>();

    @BindView(R.id.lvStations)
    ListView lvStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        connect();
    }

    protected void connect() {
        Toast.makeText(this, "connect", Toast.LENGTH_SHORT).show();
        StationenLadenTask task = new StationenLadenTask(this);
        task.execute();
    }

    protected void showStation(Station station) {
        stations.add(station);
        showStations();
    }

    private void showStations() {
        ArrayList<String> stationNames = new ArrayList<>();
        for(Station station: stations) {
            stationNames.add(station.getStationID());
        }
        String[] names = stationNames.toArray(new String[0]);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lvStations.setAdapter(arrayAdapter);
    }
}
