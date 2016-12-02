package de.th_brandenburg.eispartikel.connection;

import android.os.AsyncTask;

import java.io.IOException;

import datenKlassen.Aenderungsmeldung;
import datenKlassen.StationAenderung;
import de.th_brandenburg.eispartikel.Config;
import funktionaleKlassen.NeuesObjektListener;
import funktionaleKlassen.ZweiwegeClientkommunikator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(suppressConstructorProperties = true)
public class StationsAenderungTask extends AsyncTask<Void, Aenderungsmeldung, Void> implements NeuesObjektListener<Aenderungsmeldung> {

    @NonNull
    private NeuesObjektListener<Aenderungsmeldung> neuesObjektListener;

    private ZweiwegeClientkommunikator kommunikator;

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
        neuesObjektListener.neuesAustauschobjekt(values[0]);
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
