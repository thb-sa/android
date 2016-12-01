package de.th_brandenburg.eispartikel.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import datenKlassen.Station;
import de.th_brandenburg.eispartikel.Config;
import de.th_brandenburg.eispartikel.activities.MainActivity;
import funktionaleKlassen.EinwegClientkommunikator;
import funktionaleKlassen.NeuesObjektListener;

public class StationenLadenTask extends AsyncTask<Void, Station, Void> implements NeuesObjektListener<Station> {
    private MainActivity mainActivity;

    public StationenLadenTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i("StationLadenTask", "doInBackground");
        try {
            EinwegClientkommunikator clientkommunikator = new EinwegClientkommunikator(Config.HOST, this, EinwegClientkommunikator.EINWEGKOMMUNIKATION);
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
