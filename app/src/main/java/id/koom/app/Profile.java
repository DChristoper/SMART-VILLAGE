package id.koom.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import id.koom.app.R;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();
        initComponent();
    }

    public void lengkapiAkun(View view) {
        Intent intent = new Intent(Profile.this, FormPengajuan.class);
        startActivity(intent);
    }
    public void cekStatus(View view) {
        Intent intent = new Intent(Profile.this, Order.class);
        startActivity(intent);
    }
    public void mitra(View view) {
        Intent intent = new Intent(Profile.this, Order.class);
        startActivity(intent);
    }
    public void panduan(View view) {
        Intent intent = new Intent(Profile.this, PanduanBayar.class);
        startActivity(intent);
    }
    public void bantuan(View view) {
        Intent intent = new Intent(Profile.this, Bantuan.class);
        startActivity(intent);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.purple_600);
    }

    private void initComponent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
