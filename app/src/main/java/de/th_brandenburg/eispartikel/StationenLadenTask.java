package de.th_brandenburg.eispartikel;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import datenKlassen.Kommunikator;
import datenKlassen.NeueStationenListener;
import datenKlassen.Station;

public class StationenLadenTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Kommunikator kommunikator = new Kommunikator("192.168.56.1");
            kommunikator.setNeueStationListener(new NeueStationenListener() {
                @Override
                public void neueStation(Station station) {
                    Log.i("MainActivity", "neue Station: " + station.getStationID());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
