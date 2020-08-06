package id.koom.app;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import id.koom.app.R;

import id.koom.app.utils.SharedPrefManager;

public class AlamatPengirim extends AppCompatActivity {
    EditText alamat, kota, kd, phone;
    String Alamat, Kota, Kd, Phone;
    Button btKonfirm;
    SharedPrefManager SPManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat_pengirim);

        alamat = (EditText) findViewById(R.id.et_alamat);
        kota = (EditText) findViewById(R.id.et_kota);
        kd = (EditText) findViewById(R.id.et_kd);
        phone = (EditText) findViewById(R.id.et_phone);

        SPManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        btKonfirm = findViewById(R.id.btn_alamat);

        btKonfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alamat = alamat.getText().toString();
                Kota = kota.getText().toString();
                Kd = kd.getText().toString();
                Phone = phone.getText().toString();

                SPManager.saveSPString("Alamat", Alamat);
                SPManager.saveSPString("Kota", Kota);
                SPManager.saveSPString("Kd", Kd);
                SPManager.saveSPString("Phone", Phone);

                Intent intent = new Intent(AlamatPengirim.this, BerkasPoto.class);
                startActivity(intent);


            }

        });

    }
}