package de.th_brandenburg.eispartikel.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import datenKlassen.Tageswerte;
import de.th_brandenburg.eispartikel.R;
import de.th_brandenburg.eispartikel.Util;
import de.th_brandenburg.eispartikel.presenter.StationDetailsPresenter;

/**
 * Diese Activity zeigt Details zu einer Station an
 */
public class StationDetailsActivity extends AppCompatActivity {
    private StationDetailsPresenter presenter;
    private boolean enableDiagramm = false;

    @BindView(R.id.lvWerte)
    ListView lvWerte;

    @BindView(R.id.tvVorgabe)
    TextView tvVorgabe;

    /**
     * initialisieren
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);
        ButterKnife.bind(this);

        presenter = new StationDetailsPresenter(this);
    }

    /**
     * Diese Methode rendert das Menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.station_details, menu);

        // Deaktiviert bei Bedarf den Menupunkt Diagramm
        menu.getItem(0).setEnabled(enableDiagramm);
        return true;
    }

    /**
     * Methode, die ausgeführt wird, wenn ein Menu Eintrag geklickt wurde.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemDiagram:
                presenter.onDiagramClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Methode, die ausgeführt wird, wenn der "+" Button geedrückt wurde
     */
    @OnClick(R.id.fabNeueWerte)
    protected void onNewWertClicked() {
        presenter.onNewWertClicked();
    }

    /**
     * Zeigt den Vorgabe wert an
     *
     * @param vorgabewert   der anzuzeigende Vorgabewert
     */
    public void showVorgabe(int vorgabewert) {
        tvVorgabe.setText("Vorgabe: " + vorgabewert);
    }

    /**
     * Die Methode setzt den Title in der oberen Leiste
     *
     * @param title
     */
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * Die Methode zeigt die Werte in der Liste an
     *
     * @param werte ein ConucrrentHashMap mit den Werten für die jeweiligen Tage
     */
    public void showWerte(ConcurrentHashMap<String, Tageswerte> werte) {
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, werte);
        lvWerte.setAdapter(adapter);
    }

    /**
     * Diese Methode steuert, ob das Diagramm betracht werden kann oder nicht
     *
     * @param enable    ob das Diagramm betrachtet werden soll
     */
    public void setDiagramButton(boolean enable) {
        if (this.enableDiagramm != enable) {
            this.enableDiagramm = enable;
            invalidateOptionsMenu();
        }
    }

    /**
     * Eine ArrayAdapter, der für das Darstellen der Werte verantwortlich ist
     */
    public class CustomArrayAdapter extends ArrayAdapter<ConcurrentHashMap<String, Tageswerte>> {
        private final Context context;
        private final ConcurrentHashMap<String, Tageswerte> values;
        private final List<String> dates;

        /**
         * Konstuktor für CustomArrayAadapter
         *
         * @param context   Der Context (z.B. eine Activity)
         * @param werte
         */
        CustomArrayAdapter(Context context, ConcurrentHashMap<String, Tageswerte> werte) {
            super(context, -1);
            this.context = context;
            this.values = werte;
            this.dates = Util.oderedTaqe(werte);

            // Zeige neueste Werte zu erst:
            Collections.reverse(this.dates);
        }

        /**
         * Erzeugen eines Listeneintrages
         *
         * @param position      die Position in der Liste
         * @param convertView
         * @param parent
         * @return              die für diese Position anzuzeigende View
         */
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.werte_row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.tv);

            String datum = dates.get(position);
            Tageswerte wert = values.get(datum);

            String text = datum + ": " + wert.getAktuellerWert();
            String abweichung = "(" + Util.plusMinus(wert.getAbweichung()) + " / " + Util.plusMinus(wert.getRelativeAbweichung()) + "%)";

            textView.setText(text + " " + abweichung);
            switch (wert.getDarstellung()) {
                case NIEDRIG:
                    textView.setTextColor(Color.RED);
                    break;
                case HOCH:
                    textView.setTextColor(Color.GREEN);
                    break;
            }

            return rowView;
        }

        /**
         * Gibt die Anzahl an Listeneinträgen
         *
         * @return  anzahl
         */
        @Override
        public int getCount() {
            return dates.size();
        }
    }
}
