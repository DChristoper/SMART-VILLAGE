package id.koom.app;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import id.koom.app.R;

public class StatusPembayaran extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pembayaran);
    }

    public void pembayaran(View view) {
        Intent intent = new Intent(StatusPembayaran.this, Order.class);
        startActivity(intent);
    }
}
