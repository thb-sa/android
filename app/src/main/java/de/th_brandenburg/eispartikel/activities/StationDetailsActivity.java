package de.th_brandenburg.eispartikel.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, station.getAktuelleWerte());
        lvWerte.setAdapter(adapter);
    }

    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderungsmeldung) {
        ConcurrentHashMap<String, Tageswerte> werte = station.getAktuelleWerte();
        werte.put(aenderungsmeldung.getDatum(), aenderungsmeldung.getTageswerte());
        station.setAktuelleWerte(werte);
        showWerte();
    }

    public class CustomArrayAdapter extends ArrayAdapter<ConcurrentHashMap<String, Tageswerte>> {
        private final Context context;
        private final ConcurrentHashMap<String, Tageswerte> values;
        private final String[] dates;

        CustomArrayAdapter(Context context, ConcurrentHashMap<String, Tageswerte> werte) {
            super(context, -1);
            this.context = context;
            this.values = werte;
            this.dates = values.keySet().toArray(new String[0]);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.werte_row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.tv);

            String datum = dates[position];
            Tageswerte wert = values.get(datum);

            String text = datum + ": " + wert.getAktuellerWert();
            String abweichung = "(" + Util.plusMinus(wert.getAbweichung()) + " / " + Util.plusMinus(wert.getRelativeAbweichung()) + "%)";

            textView.setText(text + " " + abweichung);
            textView.setTextColor(wert.getAbweichung() >= 0 ? Color.GREEN : Color.RED);

            return rowView;
        }

        @Override
        public int getCount() {
            return dates.length;
        }
    }
}
