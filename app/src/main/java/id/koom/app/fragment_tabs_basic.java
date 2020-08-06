package id.koom.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import id.koom.app.R;

import butterknife.ButterKnife;

public class fragment_tabs_basic extends Fragment {

    Button bayarsekarang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout., container, false);


        View fragment = inflater.inflate(R.layout.fragment_tabs_basic, container, false);
        ButterKnife.bind(this, fragment);
        bayarsekarang = (Button) fragment.findViewById(R.id.bayarsekarang);
        bayarsekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), MetodePembayaran.class);
                startActivity(intent);
            }
        });
        return fragment;

    }
}