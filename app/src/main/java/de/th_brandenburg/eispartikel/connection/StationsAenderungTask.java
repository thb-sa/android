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

/**
 * AsyncTask, der eine Bidirektional Verbindung zum Server hält
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class StationsAenderungTask extends AsyncTask<Void, Aenderungsmeldung, Void> implements NeuesObjektListener<Aenderungsmeldung> {

    @NonNull
    private NeuesObjektListener<Aenderungsmeldung> neuesObjektListener;

    private ZweiwegeClientkommunikator kommunikator;

    /**
     * Initalisierung
     * hier wird die Verbindung zum Server aufgebaut
     *
     * @param params
     * @return
     */
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

    /**
     * Methode, die bei einem neuen Update ausgeführt wird
     * Die neue Aenderungsmeldung wird an den Presenter übergeben
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Aenderungsmeldung... values) {
        super.onProgressUpdate(values);
        neuesObjektListener.neuesAustauschobjekt(values[0]);
    }


    /**
     * Methode, die bei einer neuen Aenderungsmeldung ausgeführt wird.
     * Jede Aenderung wird als Progress veröffentlicht.
     *
     * @param aenderungsmeldung
     */
    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderungsmeldung) {
        publishProgress(aenderungsmeldung);
    }

}
