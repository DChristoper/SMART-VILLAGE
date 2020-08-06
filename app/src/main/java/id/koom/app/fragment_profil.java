package id.koom.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;
import id.koom.app.utils.SharedPrefManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.ButterKnife;

public class fragment_profil extends Fragment {
    LinearLayout akun, pengajuan, mitra, panduan, bantuan, logout;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private GoogleApiClient mGoogleApiClient;
    private OffDBHelper db;
    private TextView txtLogout;
    SharedPrefManager SPManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.activity_profile, container, false);
        ButterKnife.bind(this, fragment);

        SPManager = new SharedPrefManager(getActivity());
        db = new OffDBHelper(getActivity());

        akun = (LinearLayout) fragment.findViewById(R.id.data_lengkap);
        pengajuan=(LinearLayout) fragment.findViewById(R.id.cek_status);
        mitra=(LinearLayout) fragment.findViewById(R.id.mitra_koom);
        panduan=(LinearLayout) fragment.findViewById(R.id.panduan);
        logout = (LinearLayout) fragment.findViewById(R.id.logOut);
        txtLogout = fragment.findViewById(R.id.txtLogout);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mGoogleApiClient.connect();
//        bantuan=(LinearLayout) fragment.findViewById(R.id.bantuan);
//        logout=(LinearLayout) fragment.findViewById(R.id.logout);

        akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), FormPengajuan.class);
                startActivity(intent);
            }
        });
        pengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                order_fragment fragment2 = new order_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new order_fragment());
                fragmentTransaction.commit();
            }
        });
        mitra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg1) {
                order_fragment fragment2 = new order_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new order_fragment());
                fragmentTransaction.commit();
            }
        });
        panduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), PanduanBayar.class);
                startActivity(intent);
            }
        });

        String uid = db.getUser();

        if(uid == null){
            logout.setVisibility(View.INVISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (mGoogleApiClient.isConnected()) {
                    db.delUser();
                    SPManager.delSPString("email");
                    SPManager.delSPString("phone");

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
//        bantuan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg1) {
//                Intent intent = new Intent(getActivity(), Bantuan.class);
//                startActivity(intent);
//            }
//        });


        return fragment;
    }
}



