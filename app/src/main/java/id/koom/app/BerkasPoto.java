package id.koom.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import id.koom.app.utils.Tools;
import id.koom.app.utils.ViewAnimation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerkasPoto extends AppCompatActivity {

    String Nik, No_kk, Nama, Ttl, Lama_warung, Jk, Tujuan, Pd_bulanan, Pg_bulanan, S_pendapatan, Profesi, Alamat, Kota, Kd, Phone;
    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    private View parent_view;
    private Date date = null;
    private String time = null;
    SharedPrefManager SPManager;
    ProgressDialog progressDialog;
    String uid;
    OffDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berkas_poto);
        parent_view = findViewById(android.R.id.content);

        db = new OffDBHelper(this);

        SPManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);

        Nik = SPManager.getSPString("NIK");
        No_kk = SPManager.getSPString("No_KK");
        Nama = SPManager.getSPString("Nama");
        Ttl = SPManager.getSPString("Ttl");
        Lama_warung = SPManager.getSPString("Lama_Warung");
        Tujuan = SPManager.getSPString("Tujuan");
        Pd_bulanan = SPManager.getSPString("Pd_Bulanan");
        Pg_bulanan = SPManager.getSPString("Pg_Bulanan");
        S_pendapatan = SPManager.getSPString("S_Pendapatan");
        Profesi = SPManager.getSPString("Profesi");
        Alamat = SPManager.getSPString("Alamat");
        Kota = SPManager.getSPString("Kota");
        Kd = SPManager.getSPString("Kd");
        Phone = SPManager.getSPString("Phone");

        uid = db.getUser();

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        // populate layout field
        view_list.add(findViewById(R.id.lyt_title));
        view_list.add(findViewById(R.id.lyt_description));
        view_list.add(findViewById(R.id.lyt_time));
        view_list.add(findViewById(R.id.lyt_date));
        view_list.add(findViewById(R.id.lyt_confirmation));

        // populate view step (circle in left)
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_title)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_description)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_time)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_date)));
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_confirmation)));

    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.poto_ktp:

                startActivity(new Intent(this, AmbilGambar.class).putExtra("cek", "1"));
                break;

            case R.id.bt_continue_title:

                collapseAndContinue(0);
                break;

            case R.id.bt_continue_description:

                collapseAndContinue(1);
                break;

            case R.id.bt_continue_time:

                collapseAndContinue(2);
                break;

            case R.id.bt_continue_date:

                collapseAndContinue(3);
                break;

            case R.id.bt_add_event:


                progressDialog.setMessage("Loading");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_ADD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(BerkasPoto.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                            if (jsonObject.getString("message").equals("Data Inserted Successfully")) {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(BerkasPoto.this, "Failed to Data", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("NIK", Nik);
                        params.put("No_kk", No_kk);
                        params.put("Nama", Nama);
                        params.put("Ttl", Ttl);
                        params.put("Lama_warung", Lama_warung);
                        params.put("Tujuan", Tujuan);
                        params.put("Pd_bulanan", Pd_bulanan);
                        params.put("Pg_bulanan", Pg_bulanan);
                        params.put("S_pendapatan", S_pendapatan);
                        params.put("Profesi", Profesi);
                        params.put("Alamat", Alamat);
                        params.put("Kota", Kota);
                        params.put("Kd", Kd);
                        params.put("Phone", Phone);
                        params.put("Jk", "L");
                        params.put("Uid", uid);
                        return params;
                    }
                };
                RequestHandler.getInstance(BerkasPoto.this).addToRequestQueue(stringRequest);
                startActivity(new Intent(this, PaymentSuccessDialog.class).putExtra("cek", "1"));
                break;
        }
    }

    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_label_title:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.tv_label_description:
                if (success_step >= 1 && current_step != 1) {
                    current_step = 1;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(1));
                }
                break;
            case R.id.tv_label_time:
                if (success_step >= 2 && current_step != 2) {
                    current_step = 2;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(2));
                }
                break;
            case R.id.tv_label_date:
                if (success_step >= 3 && current_step != 3) {
                    current_step = 3;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(3));
                }
                break;
            case R.id.tv_label_confirmation:
                if (success_step >= 4 && current_step != 4) {
                    current_step = 4;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(4));
                }
                break;
        }
    }

    private void collapseAndContinue(int index) {
        ViewAnimation.collapse(view_list.get(index));
        setCheckedStep(index);
        index++;
        current_step = index;
        success_step = index > success_step ? index : success_step;
        ViewAnimation.expand(view_list.get(index));
    }

    private void collapseAll() {
        for (View v : view_list) {
            ViewAnimation.collapse(v);
        }
    }

    private void setCheckedStep(int index) {
        RelativeLayout relative = step_view_list.get(index);
        relative.removeAllViews();
        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.ic_done);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        relative.addView(img);
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

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}


