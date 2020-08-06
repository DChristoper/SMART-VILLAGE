package id.koom.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import id.koom.app.API.ApiInterface;

import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;
import id.koom.app.helper.RequestHandler;
import id.koom.app.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;


public class PaymentSuccessDialog extends AppCompatActivity {

    private AppCompatButton show_dialog;
    private ProgressBar progress_bar;
    Button btNotif;
    SharedPrefManager SPManager;
    TextView jPinjam;
    TextView jCicilan;
    NumberFormat format;
    int totalPinjam = 0;
    int totalCicil = 0;
    String Nik;
    ProgressDialog progressDialog;
    Dialog dialog;
    OffDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success_dialog);

        SPManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);

        format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("IDR"));

        btNotif = findViewById(R.id.bt_notif);
        jPinjam = findViewById(R.id.j_pengajuan);
        jCicilan = findViewById(R.id.jangkaWaktu);

        totalPinjam = SPManager.getSPTotal();
        totalCicil = SPManager.getSPCicil();
        Nik = SPManager.getSPString("NIK");

        jPinjam.setText(format.format(totalPinjam));
        jCicilan.setText(String.valueOf(totalCicil) + " Bulan");

    }

    public void prosesSelesai(View view) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(PaymentSuccessDialog.this, ShopProductGrid.class);
                startActivity(intent);
            }
        });

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_ADD_TRANSAKSI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(PaymentSuccessDialog.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    if(jsonObject.getString("message").equals("Data Inserted Successfully")){
                        finish();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(PaymentSuccessDialog.this, "Failed to Data",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("NIK", Nik);
                params.put("cicilan", String.valueOf(totalCicil));
                params.put("t_harga", String.valueOf(totalPinjam));
                return params;
            }
        };
        RequestHandler.getInstance(PaymentSuccessDialog.this).addToRequestQueue(stringRequest);

        dialog.show();
        dialog.getWindow().setAttributes(lp);

//        Intent intent = new Intent(PaymentSuccessDialog.this, Dashboard.class);
//        startActivity(intent);
    }
}
