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
import de.th_brandenburg.eispartikel.presenter.NeueWertePresenter;

public class NeueWerteActivity extends AppCompatActivity {
    private NeueWertePresenter presenter;

    @BindView(R.id.tvStationsName)
    TextView tvStationsName;

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neue_werte);
        ButterKnife.bind(this);
        configView();

        presenter = new NeueWertePresenter(this);
    }

    @OnClick(R.id.btnSenden)
    protected void senden() {
        String datum = datePicker.getDayOfMonth() + "." + datePicker.getMonth() + '.' + datePicker.getYear();
        int wert = numberPicker.getValue();

        presenter.sendenClicked(datum, wert);
    }

    public void setStationName(String stationName) {
        tvStationsName.setText("Station: " + stationName);
    }

    private void configView() {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        // Erlaube höchstens aktuelles Datum
        datePicker.setMaxDate(System.currentTimeMillis());
    }
}
