package de.th_brandenburg.eispartikel.connection;

import android.os.AsyncTask;

import java.io.IOException;

import datenKlassen.Aenderungsmeldung;
import datenKlassen.StationAenderung;
import de.th_brandenburg.eispartikel.Config;
import de.th_brandenburg.eispartikel.activities.MainActivity;
import funktionaleKlassen.NeuesObjektListener;
import funktionaleKlassen.ZweiwegeClientkommunikator;

public class StationsAenderungTask extends AsyncTask<Void, Aenderungsmeldung, Void> implements NeuesObjektListener<Aenderungsmeldung> {
    private MainActivity mainActivity;
    private ZweiwegeClientkommunikator kommunikator;

    public StationsAenderungTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            kommunikator = new ZweiwegeClientkommunikator(Config.HOST, this, ZweiwegeClientkommunikator.ZWEIWEGEKOMMUNIKATION);
            kommunikator.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Aenderungsmeldung... values) {
        super.onProgressUpdate(values);
        mainActivity.updateStation(values[0]);
    }


    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderungsmeldung) {
        publishProgress(aenderungsmeldung);
    }

    public void sende(StationAenderung aenderung) {
        try {
            kommunikator.versende(aenderung);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
