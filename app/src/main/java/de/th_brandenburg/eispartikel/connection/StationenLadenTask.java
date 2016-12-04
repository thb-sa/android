package de.th_brandenburg.eispartikel.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import datenKlassen.Station;
import de.th_brandenburg.eispartikel.Config;
import de.th_brandenburg.eispartikel.presenter.MainPresenter;
import funktionaleKlassen.EinwegClientkommunikator;
import funktionaleKlassen.NeuesObjektListener;
import lombok.AllArgsConstructor;

/**
 * Dieser AsyncTask stellt einen EinwegClientKommunikator zum Server her.
 * Bei jeder neuen Station auf dem Sever wird hier ein Event ausgelöst
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class StationenLadenTask extends AsyncTask<Void, Station, Void> implements NeuesObjektListener<Station> {
    private MainPresenter mainPresenter;

    /**
     * Diese Methode wird nach dem Starten ausgeführt.
     * Es wird eine Verbindung zum Server erstellt.
     *
     * @param params
     * @return
     */
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

    /**
     * Wenn der AsyncTask ein neues Update hat (neue Station), wird diese an den Presenter
     * übermittelt.
     *
     * @param values    die Station
     */
    @Override
    protected void onProgressUpdate(Station... values) {
        super.onProgressUpdate(values);
        mainPresenter.neueStation(values[0]);
    }

    /**
     * Methode, die bei jeder neuen Station ausgeführt wird.
     * Die neue Station wird als Progress veröffentlicht
     *
     * @param station   die neue Station
     */
    @Override
    public void neuesAustauschobjekt(Station station) {
        publishProgress(station);
    }
}
