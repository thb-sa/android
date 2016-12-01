package de.th_brandenburg.eispartikel.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import datenKlassen.Aenderungsmeldung;
import datenKlassen.Station;
import datenKlassen.Tageswerte;
import de.th_brandenburg.eispartikel.R;
import de.th_brandenburg.eispartikel.connection.StationenLadenTask;
import de.th_brandenburg.eispartikel.connection.StationsAenderungTask;
import funktionaleKlassen.NeuesObjektListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NeuesObjektListener<Aenderungsmeldung> {
    private ArrayList<Station> stations = new ArrayList<>();

    @BindView(R.id.lvStations)
    ListView lvStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        connect();
        lvStations.setOnItemClickListener(this);
    }

    protected void connect() {
        Toast.makeText(this, "connect", Toast.LENGTH_SHORT).show();
        StationenLadenTask ladenTask = new StationenLadenTask(this);
        ladenTask.execute();

        StationsAenderungTask aenderungTask = new StationsAenderungTask(this);
        aenderungTask.execute();
    }

    public void showStation(Station station) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Station station = stations.get(position);
        Intent intent = new Intent(this, StationDetailsActivity.class);

        intent.putExtra("station", station);
        //intent.putExtra("id", station.getStationID());
        //intent.putExtra("vorgabewert", station.getVorgabewert());
        //intent.putExtra("aktuelleWerte", station.getAktuelleWerte());
        startActivity(intent);
    }

    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderung) {
        for(Station station: stations) {
            if(station.getStationID().equals(aenderung.getStationID())) {
                ConcurrentHashMap<String, Tageswerte> werte = station.getAktuelleWerte();
                werte.put(aenderung.getDatum(), aenderung.getTageswerte());
            }
        }
    }
}
