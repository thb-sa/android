package de.th_brandenburg.eispartikel.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.th_brandenburg.eispartikel.R;
import de.th_brandenburg.eispartikel.Util;
import de.th_brandenburg.eispartikel.presenter.NeueWertePresenter;

/**
 * Activity zum Anlegen neuer Werte
 */
public class NeueWerteActivity extends AppCompatActivity {
    private NeueWertePresenter presenter;

    @BindView(R.id.tvStationsName)
    TextView tvStationsName;

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    /**
     * initialisieren der Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neue_werte);
        ButterKnife.bind(this);
        configView();

        presenter = new NeueWertePresenter(this);
    }

    /**
     * Methode, die ausgeührt wird, wenn "senden" geklickt wurde
     */
    @OnClick(R.id.btnSenden)
    protected void senden() {
        String tag = Util.fuehrendeNull(datePicker.getDayOfMonth());
        String monat = Util.fuehrendeNull(datePicker.getMonth() + 1);
        String jahr = String.valueOf(datePicker.getYear());
        String datum = tag + "." + monat + "." + jahr;
        int wert = numberPicker.getValue();

        presenter.sendenClicked(datum, wert);
    }

    /**
     * Methode zum Anzeigen des Stationsnamen
     *
     * @param stationName   der Stationenname
     */
    public void setStationName(String stationName) {
        tvStationsName.setText("Station: " + stationName);
    }

    /**
     * Konfigurieren der UI Komponenten
     *  - Min und Max Wert des Spinners
     *  - kein Tag aus der Zukunft erlaubt
     */
    private void configView() {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        // Erlaube höchstens aktuelles Datum
        datePicker.setMaxDate(System.currentTimeMillis());
    }
}
