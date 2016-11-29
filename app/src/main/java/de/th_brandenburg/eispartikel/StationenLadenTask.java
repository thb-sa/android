package de.th_brandenburg.eispartikel;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import datenKlassen.Station;
import funktionaleKlassen.EinwegClientkommunikator;
import funktionaleKlassen.NeuesObjektListener;

class StationenLadenTask extends AsyncTask<Void, Station, Void> implements NeuesObjektListener<Station> {
    private final static String HOST = "54.89.87.213";
    private MainActivity mainActivity;

    StationenLadenTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i("StationLadenTask", "doInBackground");
        try {
            EinwegClientkommunikator clientkommunikator = new EinwegClientkommunikator(HOST, this, EinwegClientkommunikator.EINWEGKOMMUNIKATION);
            clientkommunikator.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Station... values) {
        super.onProgressUpdate(values);
        mainActivity.showStation(values[0]);
    }

    @Override
    public void neuesAustauschobjekt(Station station) {
        publishProgress(station);
    }
}
