package id.koom.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.koom.app.R;
import id.koom.app.model.Transaksi;

import java.util.List;

public class AdapterListNotifikasi extends RecyclerView.Adapter<AdapterListNotifikasi.ViewHolder> {

    private Context ctx;
    private List<Transaksi> items;
    private DialogInterface.OnClickListener onClickListener = null;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AdapterListNotifikasi(Context mContext, List<Transaksi> items){
        this.ctx = mContext;
        this.items = items;
        selected_items = new SparseBooleanArray();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, message, date, image_letter;
        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            message = (TextView) view.findViewById(R.id.message);
            date = (TextView) view.findViewById(R.id.date);
            image_letter = (TextView) view.findViewById(R.id.image_letter);
            lyt_checked = (RelativeLayout) view.findViewById(R.id.lyt_checked);
            lyt_image = (RelativeLayout) view.findViewById(R.id.lyt_image);
            lyt_parent = (View) view.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public AdapterListNotifikasi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListNotifikasi.ViewHolder holder, int position) {
        final Transaksi tsk = items.get(position);

        // displaying text view data
        holder.title.setText(tsk.getTitle());
        holder.message.setText(tsk.getMessage() + " " + tsk.getStatus());
        holder.date.setText(tsk.getTanggal_now());

//        holder.lyt_parent.setActivated(selected_items.get(position, false));

//        notifyDataSetChanged();

//        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onClickListener == null) return;
//                onClickListener.onItemClick(v, tsk, position);
//            }
//        });
//
//        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (onClickListener == null) return false;
//                onClickListener.onItemLongClick(v, tsk, position);
//                return true;
//            }
//        });

//        toggleCheckedIcon(holder, position);
    }

    private void toggleCheckedIcon(ViewHolder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.lyt_image.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

    public interface OnClickListener {
        void onItemClick(View view, Transaksi obj, int pos);

        void onItemLongClick(View view, Transaksi obj, int pos);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
