package id.koom.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.koom.app.R;

import id.koom.app.adapter.AdapterListNotifikasi;
import id.koom.app.helper.OffDBHelper;
import id.koom.app.model.Transaksi;
import id.koom.app.utils.SharedPrefManager;
import id.koom.app.widget.LineItemDecoration;

import java.util.List;

public class fragmen_notifikasi extends Fragment {
    private RecyclerView recyclerView;
    private AdapterListNotifikasi mAdapter;
    private List<Transaksi> trs;
    Context ctx;
    SharedPrefManager SPManager;
    OffDBHelper db;

    public fragmen_notifikasi(Context ctx){
//        this.mMessageReceiver = mMessageReceiver;
        this.ctx = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragmen_notifikasi, container, false);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SPManager = new SharedPrefManager(getActivity());
        db = new OffDBHelper(getActivity());

//        item_tr = msg.getList();

        trs = db.getNotif(db.getUser());

        recyclerView = (RecyclerView) view.findViewById(R.id.recViewNotif);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new LineItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        mAdapter = new AdapterListNotifikasi(getActivity(), trs);

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//        mAdapter = new AdapterListNotifikasi(getActivity(), item_tr);
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

    }

}
