package id.koom.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;
import id.koom.app.model.ShopCart;
import id.koom.app.model.ShopProduct;
import id.koom.app.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ShopProductDetails extends AppCompatActivity {
    FloatingActionButton fabButton;
    public int sum = 0;
    TextView e;
    ShopProduct product = new ShopProduct();
    TextView tvProductName;
//    TextView tvProductDesc;
    TextView tvProductPrice;
    ImageView ivProductImage;
    TextView tvProductStock;
    private Context ctx;
    private OffDBHelper db;
    List<ShopCart> items = new ArrayList<>();
    private Dialog dialog;
    private String quantity;
    private SpinnerAdapter spinAdapter;
    int[] number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product_details);

        Bundle data = getIntent().getExtras();
        product = (ShopProduct) data.getSerializable("product");

        tvProductName = (TextView) findViewById(R.id.nama_produk);
//        tvProductDesc = (TextView) findViewById(R.id.desk_produk);
        tvProductPrice = (TextView) findViewById(R.id.price);
        ivProductImage = (ImageView) findViewById(R.id.image);
        tvProductStock = (TextView) findViewById(R.id.stok_produk);

        db = new OffDBHelper(ShopProductDetails.this);

        items = db.findAll();

        setProductProperties();

        initToolbar();

//        dialog = new Dialog(ShopProductDetails.this);
//        dialog.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        dialog.setTitle(R.string.judul_dialog);
//        dialog.setContentView(R.layout.popup_shop_cart_quantity);
//        dialog.setCancelable(true);

        // inisiasi array
//        number = new int[Integer.parseInt(product.getStock())];

//        for(int i=0; i<Integer.parseInt(product.getStock()); i++){
//            number[i] = i+1;
//        }

//        spinAdapter = new AdapterSpinnerShopDetails(getApplicationContext(), number);

//        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
//        final Button btn_ok = (Button) dialog.findViewById(R.id.button1);
//        final TextView txt_spinner = (TextView) spinner.findViewById(R.id.textView1);

//        spinner.setAdapter(spinAdapter);

//        btn_ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Log.d("Details", String.valueOf(number[(int) spinner.getSelectedItem()]));
//                int jml = number[(int) spinner.getSelectedItem()];
//                db.save(product, jml);
//
//                toast.show();
//
//
//                e.setText(String.valueOf(items.size()));
//            }
//        });

//        e.setText("10");
    }

    private void setProductProperties() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("IDR"));

        tvProductName.setText(product.getTitle());
        tvProductPrice.setText(format.format(product.getPrice()));
        tvProductStock.setText(product.getStock());
        Glide.with(ShopProductDetails.this).load(product.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivProductImage);
//        Tools.displayImageOriginal(ctx, ivProductImage, product.getImage());
//        ivProductImage.setImageResource(this.getResources().getIdentifier(product.getImage(), "string", this.getPackageName()));
    }

    public String sum_s;

    public void tambahItem(View v) {
        int jml = 1;

        fabButton = (FloatingActionButton)findViewById(R.id.fab);
        e = (TextView)findViewById(R.id.cart_badge);

        Log.d("Details ", String.valueOf(e));

        fabButton.setVisibility(View.GONE);

//        dialog.show();

        if(quantity != null){
            Log.d("Details ", quantity);
        }

        Toast toast = Toast.makeText(this, "Berhasil Ditambahkan", Toast.LENGTH_LONG);
        toast.show();

        db.save(product, jml);
        items = db.findAll();

        e.setText(String.valueOf(items.size()));
    }

    private void initToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart_setting, menu);

        // membuat app:actionlayout bisa diklik
        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.action_cart);
        item.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                m.performIdentifierAction(item.getItemId(), 0);
            }
        });

        e = item.getActionView().findViewById(R.id.cart_badge);

        e.setText(String.valueOf(items.size()));

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("jumlah_beli", sum_s);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
//            Intent intentt = new Intent(ShopProductDetails.this, ShopProductGrid.class);
//            intentt.setAction(Intent.ACTION_MAIN);
//            intentt.addCategory(Intent.CATEGORY_LAUNCHER);
//            intentt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentt);
        }else if (item.getItemId() == R.id.action_cart) {
            Intent intent = new Intent(ShopProductDetails.this, ShopCartCard.class);
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
