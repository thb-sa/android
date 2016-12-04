package de.th_brandenburg.eispartikel.presenter;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import datenKlassen.Aenderungsmeldung;
import datenKlassen.Station;
import datenKlassen.Tageswerte;
import de.th_brandenburg.eispartikel.activities.MainActivity;
import de.th_brandenburg.eispartikel.activities.StationDetailsActivity;
import de.th_brandenburg.eispartikel.connection.StationenLadenTask;
import de.th_brandenburg.eispartikel.connection.StationsAenderungTask;
import funktionaleKlassen.NeuesObjektListener;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Presenter für die Stationsübersicht
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class MainPresenter implements NeuesObjektListener<Aenderungsmeldung> {

    @NonNull
    private MainActivity mainActivity;

    /**
     * Liste mit Stationen
     */
    private ArrayList<Station> stations = new ArrayList<>();

    /**
     * Mit dem Model verbinden
     */
    public void connect() {
        StationenLadenTask ladenTask = new StationenLadenTask(this);
        ladenTask.execute();

        StationsAenderungTask aenderungTask = new StationsAenderungTask(this);
        aenderungTask.execute();
    }

    /**
     * Neue Station vom Server
     *
     * @param station
     */
    public void neueStation(Station station) {
        stations.add(station);
        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station lhs, Station rhs) {
                return lhs.getStationID().compareTo(rhs.getStationID());
            }
        });
        updateView();
    }

    /**
     * Neue Aenderung vom Server
     *
     * @param aenderung
     */
    @Override
    public void neuesAustauschobjekt(Aenderungsmeldung aenderung) {
        for(Station station: stations) {
            if(station.getStationID().equals(aenderung.getStationID())) {
                ConcurrentHashMap<String, Tageswerte> werte = station.getAktuelleWerte();
                werte.put(aenderung.getDatum(), aenderung.getTageswerte());
            }
        }
        updateView();
    }

    /**
     * In der View wurde eine Station ausgewählt:
     *
     * @param position
     */
    public void stationChoosen(int position) {
        Station station = stations.get(position);
        Intent intent = new Intent(mainActivity, StationDetailsActivity.class);
        intent.putExtra("station", station);
        mainActivity.startActivity(intent);
    }

    /**
     * View neu rendern
     */
    private void updateView() {
        mainActivity.showStations(stations);
    }
}
