package de.th_brandenburg.eispartikel.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import datenKlassen.Aenderungsmeldung;
import datenKlassen.Station;
import datenKlassen.StationAenderung;
import de.th_brandenburg.eispartikel.Config;
import de.th_brandenburg.eispartikel.R;
import funktionaleKlassen.NeuesObjektListener;
import funktionaleKlassen.ZweiwegeClientkommunikator;

public class NeueWerteActivity extends AppCompatActivity {
    Station station;

    @BindView(R.id.tvStationsName)
    TextView tvStationsName;

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neue_werte);
        ButterKnife.bind(this);

        station = (Station) getIntent().getSerializableExtra("station");

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        // Erlaube höchstens aktuelles Datum
        datePicker.setMaxDate(System.currentTimeMillis());

        tvStationsName.setText("Station: " + station.getStationID());
    }

    @OnClick(R.id.btnSenden)
    protected void senden() {
        int wert = numberPicker.getValue();
        String datum = datePicker.getDayOfMonth() + "." + datePicker.getMonth() + '.' + datePicker.getYear();
        StationAenderung stationAenderung = new StationAenderung(station.getStationID(), datum, wert);

        NeuerWertTask neuerWertTask = new NeuerWertTask();
        neuerWertTask.execute(stationAenderung);
    }

    class NeuerWertTask extends AsyncTask<StationAenderung, Aenderungsmeldung, Void> implements NeuesObjektListener {
        private ZweiwegeClientkommunikator kommunikator;

        @Override
        protected Void doInBackground(StationAenderung... params) {
            StationAenderung stationAenderung = params[0];

            try {
                kommunikator = new ZweiwegeClientkommunikator(Config.HOST, this, ZweiwegeClientkommunikator.ZWEIWEGEKOMMUNIKATION);
                kommunikator.start();
                kommunikator.versende(stationAenderung);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void neuesAustauschobjekt(Serializable serializable) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
        }
    }
}
