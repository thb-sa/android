package de.th_brandenburg.eispartikel.presenter;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.Serializable;

import datenKlassen.Aenderungsmeldung;
import datenKlassen.Station;
import datenKlassen.StationAenderung;
import de.th_brandenburg.eispartikel.Config;
import de.th_brandenburg.eispartikel.activities.NeueWerteActivity;
import funktionaleKlassen.NeuesObjektListener;
import funktionaleKlassen.ZweiwegeClientkommunikator;

public class NeueWertePresenter {
    private NeueWerteActivity activity;
    private Station station;

    public NeueWertePresenter(NeueWerteActivity activity) {
        this.activity = activity;
        station = (Station) activity.getIntent().getSerializableExtra("station");
        activity.setStationName(station.getStationID());
    }

    public void sendenClicked(String datum, int wert) {
        StationAenderung stationAenderung = new StationAenderung(station.getStationID(), datum, wert);

        NeuerWertTask neuerWertTask = new NeuerWertTask();
        neuerWertTask.execute(stationAenderung);
    }

    private class NeuerWertTask extends AsyncTask<StationAenderung, Aenderungsmeldung, Void> implements NeuesObjektListener {
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
            activity.finish();
        }
    }
}
