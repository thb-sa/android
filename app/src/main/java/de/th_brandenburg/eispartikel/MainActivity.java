package de.th_brandenburg.eispartikel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnConnect)
    protected void connect() {
        Toast.makeText(this, "connect", Toast.LENGTH_SHORT).show();
        StationenLadenTask task = new StationenLadenTask();
        task.execute();
    }
}
