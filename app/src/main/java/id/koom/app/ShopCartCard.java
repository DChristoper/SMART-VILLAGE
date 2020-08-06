package id.koom.app;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import id.koom.app.R;

import id.koom.app.adapter.AdapterShopCartCard;
import id.koom.app.helper.OffDBHelper;
import id.koom.app.model.ShopCart;
import id.koom.app.utils.ViewAnimation;

import java.util.List;

public class ShopCartCard extends AppCompatActivity {

    private View parent_view;

    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_text, bt_toggle_input;
    private Button bt_hide_text, bt_save_input, bt_hide_input;
    private View lyt_expand_text, lyt_expand_input;
    private RecyclerView recyclerView;
    private AdapterShopCartCard mAdapter;
    private Context ctx;
    private TextView hargaTotal;
    private AppCompatButton btnCheckout;
    private Spinner spiCicilan;
    private TextView totalCicilan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart_card);
//        setBackgroundTintList(ctx.getResources().getColorStateList(R.color.blue_grey_600));
        hargaTotal = (TextView) findViewById(R.id.totalHarga);
        btnCheckout = findViewById(R.id.btnCheckout);
        parent_view = findViewById(R.id.snackbar);
        spiCicilan = (Spinner) findViewById(R.id.spinnerCicilan);
        totalCicilan = (TextView) findViewById(R.id.totalCicilan);

        initComponent();
    }

    public void checkout(View view) {
        Intent intent = new Intent(ShopCartCard.this, AlamatPengirim.class);
        startActivity(intent);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OffDBHelper a = new OffDBHelper(this);

        final List<ShopCart> items = a.findAll();

        if(items.size() < 1){
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundResource(R.drawable.btn_rounded_green);
            btnCheckout.setTextColor(Color.parseColor("#cccccc"));
        } else {
            btnCheckout.setEnabled(true);
        }

        mAdapter = new AdapterShopCartCard(ShopCartCard.this, items, hargaTotal,
                                            btnCheckout, parent_view, spiCicilan,
                                            totalCicilan);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    private void toggleSectionInput(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_input, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_input);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_input);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
}

