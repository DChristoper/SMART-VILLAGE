package id.koom.app.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.koom.app.R;

import id.koom.app.model.Transaksi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class AdapterListRiwayat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transaksi> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Transaksi obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListRiwayat(Context context, List<Transaksi> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal;
        public TextView t_harga;
        public TextView cicilan;
        public TextView status;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tanggal = (TextView) v.findViewById(R.id.txtTanggal);
            t_harga = (TextView) v.findViewById(R.id.txtT_harga);
            cicilan = (TextView) v.findViewById(R.id.txtCicilan);
            status = (TextView) v.findViewById(R.id.txtStatus);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(Currency.getInstance("IDR"));

            Transaksi tr = items.get(position);
            view.tanggal.setText(tr.getTanggal_t());
            view.cicilan.setText("Cicilan " + tr.getCicilan() + "x");

            if(tr.getStatus().equals("0")){
                view.status.setText("Ditolak");
            } else if(tr.getStatus().equals("1")){
                view.status.setText("Diterima");
            }

            int total = Integer.parseInt(tr.getT_harga());

            view.t_harga.setText("Jumlah Pinjaman : " + format.format(total));
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}