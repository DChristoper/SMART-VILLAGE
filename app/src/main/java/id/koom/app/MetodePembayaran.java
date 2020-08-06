package id.koom.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Button;

import id.koom.app.R;


public class MetodePembayaran extends AppCompatActivity {
            RadioButton BCA,BNI, BRI,mandiri, Lampung,  vaBCA, vaBNI, vaBRI, vamandiri, valampung,Indomaret, Alfamart;
            Button metode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode_pembayaran);

        BCA=(RadioButton) findViewById(R.id.tf_BCA);
        BNI=(RadioButton) findViewById(R.id.BNI);
        BRI=(RadioButton) findViewById(R.id.BRI);
        mandiri=(RadioButton) findViewById(R.id.Mandiri);
        Lampung=(RadioButton) findViewById(R.id.lampung);
        vaBCA = (RadioButton) findViewById(R.id.va_BCA);
        vaBNI = (RadioButton) findViewById(R.id.va_BNI);
        vaBRI = (RadioButton) findViewById(R.id.va_BRI);
        vamandiri = (RadioButton) findViewById(R.id.va_mandiri);
        valampung = (RadioButton) findViewById(R.id.va_lampung);
        Indomaret=(RadioButton) findViewById(R.id.indomaret);
        Alfamart=(RadioButton) findViewById(R.id.alfamart);



        BCA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(true);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });

        BNI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(true);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });

        BRI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(true);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });

        mandiri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(true);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        Lampung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(true);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        vaBCA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(true);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        vaBNI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(true);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        vaBRI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(true);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        vamandiri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(true);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        valampung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(true);
                Indomaret.setChecked(false);
                Alfamart.setChecked(false);
            }
        });
        Indomaret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(true);
                Alfamart.setChecked(false);
            }
        });
        Alfamart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BCA.setChecked(false);
                BNI.setChecked(false);
                BRI.setChecked(false);
                mandiri.setChecked(false);
                Lampung.setChecked(false);
                vaBCA.setChecked(false);
                vaBNI.setChecked(false);
                vaBRI.setChecked(false);
                vamandiri.setChecked(false);
                valampung.setChecked(false);
                Indomaret.setChecked(false);
                Alfamart.setChecked(true);
            }
        });




    }
}