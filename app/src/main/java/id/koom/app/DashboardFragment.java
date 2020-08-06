package id.koom.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;
import id.koom.app.utils.SharedPrefManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;


public class DashboardFragment extends Fragment {

//    @BindView(R.id.btnWarung) FloatingActionButton btnWarung;
    FloatingActionButton btnWarung;
    Context ctx;
    TextView email_phone;
    OffDBHelper db;
    private FirebaseAuth mAuth;
    SharedPrefManager SPManager;

    public DashboardFragment(Context ctx) {
        // Required empty public constructor
        this.ctx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_dashboard, container, false);

        db = new OffDBHelper(getActivity());
        SPManager = new SharedPrefManager(getActivity());

        ButterKnife.bind(this, fragment);
        btnWarung = (FloatingActionButton) fragment.findViewById(R.id.btnWarung);
        email_phone = fragment.findViewById(R.id.email_phone);

        if(SPManager.getSPString("email") != ""){
            email_phone.setText("Email : " + SPManager.getSPString("email"));
        } else if(SPManager.getSPString("phone") != ""){
            email_phone.setText("No. HP : " + SPManager.getSPString("phone"));
        } else {
            email_phone.setText("Anda belum login");
        }

        btnWarung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ShopProductGrid.class);
                startActivity(intent);
            }
        });

        return fragment;
    }

}


