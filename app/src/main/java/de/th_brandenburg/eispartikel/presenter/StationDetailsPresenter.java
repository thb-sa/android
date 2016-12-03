package de.th_brandenburg.eispartikel.presenter;

import android.content.Intent;

import java.util.concurrent.ConcurrentHashMap;

import datenKlassen.Aenderungsmeldung;
import datenKlassen.Station;
import datenKlassen.Tageswerte;
import de.th_brandenburg.eispartikel.activities.NeueWerteActivity;
import de.th_brandenburg.eispartikel.activities.StationChartActivity;
import de.th_brandenburg.eispartikel.activities.StationDetailsActivity;
import de.th_brandenburg.eispartikel.connection.StationsAenderungTask;
import funktionaleKlassen.NeuesObjektListener;

public class StationDetailsPresenter implements NeuesObjektListener<Aenderungsmeldung> {
    private StationDetailsActivity activity;
    private Station station;

    public StationDetailsPresenter(StationDetailsActivity activity) {
        this.activity = activity;

        station = (Station) activity.getIntent().getSerializableExtra("station");
        activity.showVorgabe(station.getVorgabewert());
        activity.setTitle(station.getStationID());
        showWerte();

        StationsAenderungTask stationsAenderungTask = new StationsAenderungTask(this);
        stationsAenderungTask.execute();
    }

    /**
     * neue Aenderungsmeldung vom Server
     *
     * @param aenderungsmeldung
     */
    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderungsmeldung) {
        ConcurrentHashMap<String, Tageswerte> werte = station.getAktuelleWerte();
        werte.put(aenderungsmeldung.getDatum(), aenderungsmeldung.getTageswerte());
        station.setAktuelleWerte(werte);
        showWerte();
    }

    /**
     * Methode, die ausgeführt wird wenn in View auf "neuer Wert" geklickt wurde
     */
    public void onNewWertClicked() {
        Intent intent = new Intent(activity, NeueWerteActivity.class);
        intent.putExtra("station", station);
        activity.startActivity(intent);
    }

    /**
     * Aktuallisiere Werteliste in View
     */
    private void showWerte() {
        activity.showWerte(station.getAktuelleWerte());
    }

    /**
     * Methode, die ausgeführt wird, wenn auf "Diagramm" geklickt wird
     */
    public void onDiagramClicked() {
        Intent intent = new Intent(activity, StationChartActivity.class);
        intent.putExtra("station", station);
        activity.startActivity(intent);
    }
}
