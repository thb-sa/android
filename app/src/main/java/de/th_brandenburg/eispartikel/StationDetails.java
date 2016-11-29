package de.th_brandenburg.eispartikel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import datenKlassen.Station;
import datenKlassen.Tageswerte;

public class StationDetails extends AppCompatActivity {
    private Station station;

    @BindView(R.id.lvWerte)
    ListView lvWerte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);
        ButterKnife.bind(this);

        station = (Station) getIntent().getSerializableExtra("station");
        getSupportActionBar().setTitle(station.getStationID());
        showWerte();
    }

    private void showWerte() {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Tageswerte> wert : station.getAktuelleWerte().entrySet()) {
            list.add(wert.getKey() + ": " + wert.getValue().getAktuellerWert());
        }
        String[] werte = list.toArray(new String[0]);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, werte);
        lvWerte.setAdapter(arrayAdapter);
    }
}
