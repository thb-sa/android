package de.th_brandenburg.eispartikel.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import datenKlassen.Aenderungsmeldung;
import datenKlassen.Station;
import datenKlassen.Tageswerte;
import de.th_brandenburg.eispartikel.R;
import de.th_brandenburg.eispartikel.Util;
import de.th_brandenburg.eispartikel.connection.StationsAenderungTask;
import funktionaleKlassen.NeuesObjektListener;

public class StationDetailsActivity extends AppCompatActivity implements NeuesObjektListener<Aenderungsmeldung> {
    private Station station;

    @BindView(R.id.lvWerte)
    ListView lvWerte;

    @BindView(R.id.tvVorgabe)
    TextView tvVorgabe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);
        ButterKnife.bind(this);

        StationsAenderungTask stationsAenderungTask = new StationsAenderungTask(this);
        stationsAenderungTask.execute();

        station = (Station) getIntent().getSerializableExtra("station");
        getSupportActionBar().setTitle(station.getStationID());
        showVorgabe();
        showWerte();
    }
    
    @OnClick(R.id.tvVorgabe)
    protected void newWert() {
        Intent intent = new Intent(this, NeueWerteActivity.class);
        intent.putExtra("station", station);
        startActivity(intent);
    }

    private void showVorgabe() {
        tvVorgabe.setText("Vorgabe: " + station.getVorgabewert());
    }

    private void showWerte() {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Tageswerte> eintrag : station.getAktuelleWerte().entrySet()) {
            String datum = eintrag.getKey();
            Tageswerte wert = eintrag.getValue();

            String text = datum + ": " + wert.getAktuellerWert();
            String abweichung = "(" + Util.plusMinus(wert.getAbweichung()) + " / " + Util.plusMinus(wert.getRelativeAbweichung()) + "%)";

            list.add(text + " " + abweichung);
        }
        String[] werte = list.toArray(new String[0]);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, werte);
        lvWerte.setAdapter(arrayAdapter);
    }

    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderungsmeldung) {
        ConcurrentHashMap<String, Tageswerte> werte = station.getAktuelleWerte();
        werte.put(aenderungsmeldung.getDatum(), aenderungsmeldung.getTageswerte());
        station.setAktuelleWerte(werte);
        showWerte();
    }
}
