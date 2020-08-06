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

public class FormPengajuanTambahan extends AppCompatActivity {
    EditText tujuan, pd_bulanan, pg_bulanan, s_pendapatan, profesi;
    String Tujuan, Pd_bulanan, Pg_bulanan, S_pendapatan, Profesi;
    Button BtFormTambahan;
    ProgressDialog progressDialog;
    SharedPrefManager SPManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pengajuan_tambahan);

        tujuan = (EditText) findViewById(R.id.ET_tujuan);
        pd_bulanan = (EditText) findViewById(R.id.ET_pdBulanan);
        pg_bulanan = (EditText) findViewById(R.id.ET_pgBulanan);
        s_pendapatan = (EditText) findViewById(R.id.ET_sPendapatan);
        profesi = (EditText) findViewById(R.id.ET_profesi);

        progressDialog = new ProgressDialog(this);

        BtFormTambahan = (Button) findViewById(R.id.Bt_lanjutkan);

        SPManager = new SharedPrefManager(this);

        BtFormTambahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tujuan = tujuan.getText().toString();
                Pd_bulanan = pd_bulanan.getText().toString();
                Pg_bulanan = pg_bulanan.getText().toString();
                S_pendapatan = s_pendapatan.getText().toString();
                Profesi = profesi.getText().toString();

                SPManager.saveSPString("Tujuan", Tujuan);
                SPManager.saveSPString("Pd_Bulanan", Pd_bulanan);
                SPManager.saveSPString("Pg_Bulanan", Pg_bulanan);
                SPManager.saveSPString("S_Pendapatan", S_pendapatan);
                SPManager.saveSPString("Profesi", Profesi);

                Intent intent = new Intent(FormPengajuanTambahan.this, AlamatPengirim.class);
                startActivity(intent);

            }
        });

    }

}
