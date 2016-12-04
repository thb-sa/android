package de.th_brandenburg.eispartikel.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import datenKlassen.Station;
import de.th_brandenburg.eispartikel.R;
import de.th_brandenburg.eispartikel.presenter.MainPresenter;

/**
 * Diese Activity listet alle Stationen auf
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    MainPresenter presenter;

    @BindView(R.id.lvStations)
    ListView lvStations;

    /**
     * Methode die beim erstellen der Activity ausgeführt wird
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
        presenter.connect();

        lvStations.setOnItemClickListener(this);
    }

    /**
     * zeigt in der Liste die Stationen an
     *
     * @param stations  anzuzeigene Stationen
     */
    public void showStations(ArrayList<Station> stations) {
        ArrayList<String> stationNames = new ArrayList<>();
        for(Station station: stations) {
            stationNames.add(station.getStationID());
        }
        String[] names = stationNames.toArray(new String[0]);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lvStations.setAdapter(arrayAdapter);
    }

    /**
     * Methode, die ausgeführt wird, wenn eine Station ausgewählt wurde
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.stationChoosen(position);
    }
}
