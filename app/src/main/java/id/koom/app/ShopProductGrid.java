package id.koom.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import id.koom.app.R;

import id.koom.app.adapter.AdapterGridShopProductCard;
import id.koom.app.data.DataGenerator;
import id.koom.app.helper.ApiCallback;
import id.koom.app.helper.OffDBHelper;
import id.koom.app.model.ShopCart;
import id.koom.app.model.ShopProduct;
import id.koom.app.utils.Connection;
import id.koom.app.utils.Tools;
import id.koom.app.widget.SpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ShopProductGrid extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private AdapterGridShopProductCard mAdapter;
    private OffDBHelper db;
    List<ShopCart> items = new ArrayList<>();
    List<ShopProduct> itemss;
    private Connection conn;

    public TextView e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product_grid);
        parent_view = findViewById(R.id.parent_view);
//        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("data");

        final Intent notificationIntent = new Intent(this, ShopProductDetails.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        db = new OffDBHelper(ShopProductGrid.this);

        items = db.findAll();

        initToolbar();
        initComponent();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id.koom.app.utils.Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        final DataGenerator a = new DataGenerator();
//        final List<ShopProduct> items = a.getShoppingProduct();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(conn.checkConnection(ShopProductGrid.this)){
                    itemss.clear();
                    recyclerView.removeAllViews();

                    a.getShoppingProduct(new ApiCallback(){
                        @Override
                        public void onSuccess(List<ShopProduct> itemi){
                            //do stuff here with the list from the request
                            itemss = itemi;
                            Log.d("Grid1", String.valueOf(itemss.size()));

                            mAdapter = new AdapterGridShopProductCard(ShopProductGrid.this, itemss);
                            recyclerView.setAdapter(mAdapter);

                            // on item list clicked
                            mAdapter.setOnItemClickListener(new AdapterGridShopProductCard.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, ShopProduct obj, int position) {
                                    ShopProduct product = itemss.get(position);
                                    Intent intent = new Intent(ShopProductGrid.this, ShopProductDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("product", product);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            mAdapter.setOnMoreButtonClickListener(new AdapterGridShopProductCard.OnMoreButtonClickListener() {
                                @Override
                                public void onItemClick(View view, ShopProduct obj, MenuItem item) {

                                }
                            });
                        }
                    });

                    pullToRefresh.setRefreshing(false);
                } else {
                    Toast.makeText(ShopProductGrid.this, "No connection", Toast.LENGTH_LONG).show();
                }


            }
        });

        if(conn.checkConnection(this)){
            a.getShoppingProduct(new ApiCallback(){
                @Override
                public void onSuccess(List<ShopProduct> itemi){
                    //do stuff here with the list from the request
                    itemss = itemi;
                    Log.d("Grid1", String.valueOf(itemss.size()));

                    mAdapter = new AdapterGridShopProductCard(ShopProductGrid.this, itemss);
                    recyclerView.setAdapter(mAdapter);

                    // on item list clicked
                    mAdapter.setOnItemClickListener(new AdapterGridShopProductCard.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, ShopProduct obj, int position) {
                            ShopProduct product = itemss.get(position);
                            Intent intent = new Intent(ShopProductGrid.this, ShopProductDetails.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("product", product);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    mAdapter.setOnMoreButtonClickListener(new AdapterGridShopProductCard.OnMoreButtonClickListener() {
                        @Override
                        public void onItemClick(View view, ShopProduct obj, MenuItem item) {

                        }
                    });
                }
            });
        } else {
            Toast.makeText(ShopProductGrid.this, "No connection", Toast.LENGTH_LONG).show();
        }

//        Log.d("Grid", String.valueOf(itemss.size()));

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
        Log.d("Grid ", "Menu");

        return true;
    }

    public String a;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                a = data.getStringExtra("jumlah_beli");
                Log.d("Grid ", "Result");
            }
        }
        Log.d("Grid ", "Result");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, ShopCartCard.class));

        }else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        Log.d("Grid ", "Resume");
        items = db.findAll();
//        e.setText(String.valueOf(items.size()));

        if(e == null){
        } else {
            e.setText(String.valueOf(items.size()));
        }

        Log.d("Grid ", String.valueOf(e));
    }

}