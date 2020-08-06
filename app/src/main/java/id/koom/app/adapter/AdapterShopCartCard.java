package id.koom.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import id.koom.app.FormPengajuan;

import id.koom.app.R;

import id.koom.app.data.DataGenerator;
import id.koom.app.helper.ApiCallback;
import id.koom.app.helper.OffDBHelper;
import id.koom.app.helper.StockCallback;
import id.koom.app.model.ShopCart;
import id.koom.app.model.ShopProduct;
import id.koom.app.utils.Connection;
import id.koom.app.utils.SharedPrefManager;
import id.koom.app.utils.Tools;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class AdapterShopCartCard extends RecyclerView.Adapter<AdapterShopCartCard.CategoryViewHolder> {

    private List<ShopCart> items = new ArrayList<>();
    private Context ctx;
    private TextView hargaTotal;
    private TextView totalCicilan;
    private View parent;
    private AppCompatButton btnCheckout;
    final DataGenerator dt = new DataGenerator();
    List<ShopProduct> itemss;
    private Connection conn;
    SharedPrefManager SPManager;
    Spinner spiCicilan;
    ArrayAdapter<String> spinAdapter;
    String itemSpinner[] = {"32","46","64"};
    String itemSpi = "1";

    public AdapterShopCartCard(Context context, List<ShopCart> items,
                               TextView hargaTotal, AppCompatButton btnCheckout, View parent,
                               Spinner spiCicilan, TextView totalCicilan) {
        this.items = items;
        this.ctx = context;
        this.hargaTotal = hargaTotal;
        this.btnCheckout = btnCheckout;
        this.parent = parent;
        this.SPManager = new SharedPrefManager(context);
        this.spiCicilan = spiCicilan;
        this.totalCicilan = totalCicilan;

        spinAdapter =
                new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, itemSpinner);
        spinAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        this.spiCicilan.setAdapter(spinAdapter);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView price;
        public ImageButton kurang;
        public ImageButton tambah;
        public ImageButton hapus;
        public TextView quantity;
        public TextView stock;
        public View lyt_parent;
        CategoryViewHolder(@NonNull View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            price = (TextView) v.findViewById(R.id.price);
            kurang = (ImageButton) v.findViewById(R.id.kurang);
            tambah = (ImageButton) v.findViewById(R.id.tambah);
            hapus = (ImageButton) v.findViewById(R.id.hapus);
            quantity = (TextView) v.findViewById(R.id.quantity);
            stock = (TextView) v.findViewById(R.id.stock);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_product_cart_card, parent, false);

        return new CategoryViewHolder(itemRow);
    }

    int a;
    String c = "8";

    public void cekStock(StockCallback myCallback, int position){
        if(conn.checkConnection(ctx)){
            dt.getShoppingProduct(new ApiCallback(){
                @Override
                public void onSuccess(List<ShopProduct> itemi){
                    //do stuff here with the list from the request
                    itemss = itemi;
                    for(int i = 0; i < itemss.size(); i++){
                        if(itemss.get(i).getTitle().equals(items.get(position).getTitle())){
                            items.get(position).setStock(itemss.get(i).getStock());
                            c = items.get(position).getStock();
                            Log.d("Adapter Cart stock", c);
                            if(c != null){
                                myCallback.onStock(c);
                            }
                        }
                    }
                }
            });
        }
    }

    int totalH;
    int totalC;

    @Override
    public void onBindViewHolder(@NonNull final AdapterShopCartCard.CategoryViewHolder holder, final int position) {
        final OffDBHelper db = new OffDBHelper(ctx);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("IDR"));

        spiCicilan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSpi = itemSpinner[i];
                cicilanTotal();
                totalCicilan.setText(format.format(totalC));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                itemSpi = itemSpinner[0];
                cicilanTotal();
                totalCicilan.setText(format.format(totalC));
            }
        });

        holder.title.setText(items.get(position).getTitle());
        holder.price.setText(format.format(items.get(position).getPrice()));

        total.add(position);
        hitungTotal(items.get(position).getPrice(), 0, Integer.parseInt(items.get(position).getJml()), position);
        totalCheckout();
        hargaTotal.setText(format.format(totalH));

        itemSpi = spiCicilan.getSelectedItem().toString();
        cicilanTotal();
        totalCicilan.setText(format.format(totalC));

        Tools.displayImageOriginal(ctx, holder.image, items.get(position).getImage());

        holder.quantity.setText(String.valueOf(items.get(position).getJml()));

        holder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = Integer.valueOf(String.valueOf(holder.quantity.getText()));
                a = a+1;

                if(conn.checkConnection(ctx)){
                    cekStock(new StockCallback() {
                        @Override
                        public void onStock(String value) {

                            if(a >= Integer.parseInt(value)){
                                holder.quantity.setText(value);
                                hitungTotal(items.get(position).getPrice(), 0, Integer.parseInt(value), position);
                                db.updateJml(items.get(position), 1, Integer.parseInt(value));

                                totalCheckout();
                                hargaTotal.setText(format.format(totalH));
                                a = Integer.parseInt(value);

                                itemSpi = spiCicilan.getSelectedItem().toString();
                                cicilanTotal();
                                totalCicilan.setText(format.format(totalC));

                                Snackbar.make(parent, "Stok tidak mencukupi", Snackbar.LENGTH_SHORT).show();
                            } else {
                                holder.quantity.setText(String.valueOf(a));

                                hitungTotal(items.get(position).getPrice(), 1, a, position);

                                db.updateJml(items.get(position), 1, 0);

                                totalCheckout();
                                hargaTotal.setText(format.format(totalH));

                                itemSpi = spiCicilan.getSelectedItem().toString();
                                cicilanTotal();
                                totalCicilan.setText(format.format(totalC));
                            }
                        }
                    }, position);
                } else {
                    Toast.makeText(ctx, "No connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = Integer.valueOf(String.valueOf(holder.quantity.getText()));
                a = a-1;

                if(a <= 0){
                    holder.quantity.setText(String.valueOf(0));
                    db.updateJml(items.get(position), 0, 0);
                } else {
                    hitungTotal(items.get(position).getPrice(), 1, a, position);

                    holder.quantity.setText(String.valueOf(a));
                    db.updateJml(items.get(position), -1, 0);

                    totalCheckout();
                    hargaTotal.setText(format.format(totalH));

                    itemSpi = spiCicilan.getSelectedItem().toString();
                    cicilanTotal();
                    totalCicilan.setText(format.format(totalC));
                }
            }
        });

        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.hapus.setColorFilter(Color.parseColor("#666666"));
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Konfirmasi Hapus");
                builder.setMessage("Apa anda yakin ingin menghapusnya?");
                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ShopCart items_delete = items.get(position);
                        int total_delete = total.get(position);

                        Snackbar snackbar = Snackbar.make(parent, "Item terhapus", Snackbar.LENGTH_LONG)
                                .setAction("BATAL", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        db.save(items_delete, Integer.parseInt(items_delete.getJml()));
                                        items.add(items_delete);

                                        total.add(total_delete);
                                        totalCheckout();
                                        hargaTotal.setText(format.format(totalH));

                                        notifyDataSetChanged();
                                        Snackbar.make(parent, "Item batal dihapus", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                        snackbar.show();

                        db.delete(items.get(position).getTitle());
                        items.remove(position);
                        notifyDataSetChanged();
                        List<ShopCart> newShopCart = db.findAll();

                        total.remove(position);
                        totalCheckout();

                        hargaTotal.setText(format.format(totalH));

//                      updateList(newShopCart);
                    }
                }).setNegativeButton("BATAL", null);
                builder.show();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, FormPengajuan.class);
                intent.putExtra("totalHarga", format.format(totalH));
                SPManager.saveSPTotal(totalH);
                SPManager.saveSPCicil(Integer.parseInt(spiCicilan.getSelectedItem().toString()));
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<Integer> total = new ArrayList<Integer>();;

    public void hitungTotal(int price, int qty, int jml, int position){
        if(qty == 0 && jml != 1) {
            if(!total.isEmpty()) {
                total.set(position, price * jml);
            }
        } else if(qty == 0){
            if(!total.isEmpty()){
                total.set(position, price * jml);
            } else{}
        } else if(qty == 1){
            if(!total.isEmpty()) {
                total.set(position, price * jml);
            }
        }
    }

    void totalCheckout(){
        totalH = 0;
        for(int i = 0; i < total.size(); i++){
            totalH += total.get(i);
        }
    }

    public void updateList (List<ShopCart> items) {
        if (items != null && items.size() > 0) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void cicilanTotal(){
        totalC = 1;
        totalC = totalH / Integer.parseInt(itemSpi);
    }

}
