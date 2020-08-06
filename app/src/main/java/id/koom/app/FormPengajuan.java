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

public class FormPengajuan extends AppCompatActivity {

    EditText nik, no_kk, nama, ttl, lama_warung, jk;
    String Nik, No_kk, Nama, Ttl, Lama_warung, Jk;
    Button btnKonfirmasi;
    ProgressDialog progressDialog;
    SharedPrefManager SPManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pengajuan);

        nama = (EditText) findViewById(R.id.nama);
        ttl = (EditText) findViewById(R.id.ttl);
        nik = (EditText) findViewById(R.id.nik);
        no_kk = (EditText) findViewById(R.id.no_KK);
        lama_warung = (EditText) findViewById(R.id.lama_warung);

        progressDialog = new ProgressDialog(this);

        btnKonfirmasi = (Button) findViewById(R.id.btn_Konfirmasi);

        SPManager = new SharedPrefManager(this);

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nik = nik.getText().toString();
                No_kk = no_kk.getText().toString();
                Nama = nama.getText().toString();
                Ttl = ttl.getText().toString();
                Lama_warung = lama_warung.getText().toString();

                SPManager.saveSPString("NIK", Nik);
                SPManager.saveSPString("No_KK", No_kk);
                SPManager.saveSPString("Nama", Nama);
                SPManager.saveSPString("Ttl", Ttl);
                SPManager.saveSPString("Lama_Warung", Lama_warung);

                Intent intent = new Intent(FormPengajuan.this, FormPengajuanTambahan.class);
                startActivity(intent);
            }
    });
    }
}

