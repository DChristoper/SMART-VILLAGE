package id.koom.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import id.koom.app.API.ApiInterface;

import id.koom.app.R;

import id.koom.app.adapter.AdapterListRiwayat;
import id.koom.app.helper.OffDBHelper;
import id.koom.app.helper.RequestHandler;
import id.koom.app.model.Transaksi;
import id.koom.app.widget.LineItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Riwayat_fragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterListRiwayat mAdapter;
    TextView txtNothing;
    OffDBHelper db;
    List<Transaksi> trs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.riwayat_fragment, container, false);

        db = new OffDBHelper(getActivity());
        trs = new ArrayList<Transaksi>();
        txtNothing = fragment.findViewById(R.id.nothing);

        recyclerView = (RecyclerView) fragment.findViewById(R.id.recViewRiwa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiInterface.URL_GET_TRANSAKSI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonMainNode = jsonObject.optJSONArray("data");

                    if(jsonMainNode.length() >= 1){
                        txtNothing.setVisibility(View.INVISIBLE);
                    }

                    for(int i = 0; i<jsonMainNode.length();i++){
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Transaksi tr = new Transaksi();
                        String tanggal = jsonChildNode.optString("tgl_transaksi");
                        String cicilan = jsonChildNode.optString("cicilan");
                        String t_harga = jsonChildNode.optString("t_harga");
                        String status = jsonChildNode.optString("stts_pinjam");

                        Log.d("List Transaksi", tanggal + cicilan + t_harga + status);

                        tr.setTitle("");
                        tr.setMessage("");
                        tr.setTanggal_now("");
                        tr.setTanggal_t(tanggal);
                        tr.setCicilan(cicilan);
                        tr.setT_harga(t_harga);
                        tr.setStatus(status);

                        trs.add(tr);
                    }
                    Log.d("List Transaksi Size", String.valueOf(trs.size()));

                    mAdapter = new AdapterListRiwayat(getActivity(), trs);

                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    if (jsonObject.getString("message").equals("Success")) {
                        Log.d("MainApp", "TAMPIL");
                    } else if (jsonObject.getString("message").equals("Failed")) {
                        Log.d("MainApp", "GAGAL");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainApp", "RESPONSE FAILED");
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", db.getUser());
                return params;
            }
        };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

        return fragment;

    }
}
