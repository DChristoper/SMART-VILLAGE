package id.koom.app.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import id.koom.app.model.ShopCart;
import id.koom.app.model.ShopProduct;
import id.koom.app.model.Transaksi;

import java.util.ArrayList;
import java.util.List;

public class OffDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Belanja";

    private static final String TABLE_KERANJANG = "t_keranjang";
    private static final String TABLE_USER = "t_user";
    private static final String TABLE_NOTIF = "t_notifikasi";

    private static final String KEY_ID = "k_id";
    private static final String KEY_NAMA = "k_nama_produk";
    private static final String KEY_HARGA = "k_harga_produk";
    private static final String KEY_JML = "k_jml_produk";
    private static final String KEY_GAMBAR = "k_gambar_produk";

    private static final String KEY_ID_USER = "k_id_user";
    private static final String KEY_EMAIL_PHONE = "k_email_phone";

    private static final String KEY_TITLE = "k_title";
    private static final String KEY_MESSAGE = "k_message";
    private static final String KEY_TGL_NOW = "k_tgl_now";
    private static final String KEY_TGL_T = "k_tgl_t";
    private static final String KEY_CICILAN = "k_cicilan";
    private static final String KEY_T_HARGA = "k_t_harga";
    private static final String KEY_STATUS = "k_status";

    public OffDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getReadableDatabase();

        Log.d("Database :", "Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KERANJANG_TABLE = "CREATE TABLE " + TABLE_KERANJANG + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMA + " TEXT,"
                + KEY_HARGA + " TEXT," + KEY_JML + " INTEGER,"  + KEY_GAMBAR + " TEXT)";
        db.execSQL(CREATE_KERANJANG_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID_USER + " TEXT PRIMARY KEY," + KEY_EMAIL_PHONE + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_NOTIF_TABLE = "CREATE TABLE " + TABLE_NOTIF + "("
                + KEY_ID_USER + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_MESSAGE + " TEXT," + KEY_TGL_NOW + " TEXT," + KEY_TGL_T + " TEXT,"
                + KEY_CICILAN + " INTEGER," + KEY_T_HARGA + " INTEGER," + KEY_STATUS + " TEXT)";
        db.execSQL(CREATE_NOTIF_TABLE);

        Log.d("Database :", "Dibuat");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERANJANG);

        Log.d("Database :", "Upgrade");

        onCreate(db);
    }

    public void save(ShopProduct product, int jml){
        SQLiteDatabase db=this.getWritableDatabase();
        ShopCart a = new ShopCart();
        a.setTitle(product.getTitle());

        if(cekTitle(product.getTitle())){
//            updateJml(a, jml, 0);
        } else {
            db.execSQL("INSERT INTO "+TABLE_KERANJANG+"("+KEY_NAMA+","+KEY_HARGA +","+KEY_JML+","
                    +KEY_GAMBAR+") VALUES('" +
                    product.getTitle() + "','" +
                    product.getPrice() + "','" +
                    jml + "','" +
                    product.getImage() + "')");

            Log.d("Database :", "Tersimpan");
        }

        db.close();
    }

    public void updateJml(ShopCart cart ,int jml, int stok){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT " + KEY_JML + " FROM "+TABLE_KERANJANG+" WHERE " + KEY_NAMA + " ='" + cart.getTitle() + "'";
        Cursor cursor = db.rawQuery(query, null);

        int jml_sekarang = 0;

        if(cursor.moveToFirst()){
            do{
                jml_sekarang = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        if(jml == 0 && stok == 0) {
            db.execSQL("UPDATE " + TABLE_KERANJANG + " SET " + KEY_JML + "=" + 0 +
                    " WHERE " + KEY_NAMA + "='" + cart.getTitle() + "'");

//            Log.d("Database (Jml)", String.valueOf(jml_sekarang));
        } else if (stok > 0) {
            db.execSQL("UPDATE " + TABLE_KERANJANG + " SET " + KEY_JML + "=" + stok +
                    " WHERE " + KEY_NAMA + "='" + cart.getTitle() + "'");
        } else {
            jml_sekarang += jml;

            db.execSQL("UPDATE " + TABLE_KERANJANG + " SET " + KEY_JML + "=" + jml_sekarang +
                    " WHERE " + KEY_NAMA + "='" + cart.getTitle() + "'");

//            Log.d("Database (Jml)", String.valueOf(jml_sekarang));
        }

//        String jml_ekarang = cursor.getString(0);


    }

    public List<ShopCart> findAll(){
        List<ShopCart> listCart = new ArrayList<ShopCart>();
        String query="SELECT * FROM "+TABLE_KERANJANG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                ShopCart cart = new ShopCart();
                cart.setTitle(cursor.getString(1));
                cart.setPrice(cursor.getInt(2));
                cart.setJml(cursor.getString(3));
                cart.setImage(cursor.getString(4));
                Log.d("Data :", cursor.getString(2));
                listCart.add(cart);
            }while(cursor.moveToNext());
        }

        return listCart;
    }

    public boolean cekTitle(String title) {
        String countQuery = "SELECT  * FROM " + TABLE_KERANJANG + " WHERE " + KEY_NAMA + "='" + title + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(countQuery, null);
//        int count = cursor.getCount();

        Log.d("Database (Count)",  String.valueOf(cur.getCount()));
        if (cur != null && cur.getCount()>0) {
            // duplicate found
            return true;
        }
        return false;
    }

    public void delete(String title){
        //Open the database
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        db.execSQL("DELETE FROM " + TABLE_KERANJANG + " WHERE " + KEY_NAMA + "='" + title + "'");

        //Close the database
        db.close();
    }

    public void saveUser(String uid, String email_phone){
        SQLiteDatabase db=this.getWritableDatabase();

            db.execSQL("INSERT INTO "+TABLE_USER+"("+KEY_ID_USER+","+KEY_EMAIL_PHONE +") VALUES('" +
                    uid + "','" +
                    email_phone + "')");

        db.close();
    }

    public String getUser(){
        String query="SELECT * FROM "+TABLE_USER;
        String uid = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            uid = cursor.getString(0);
//            do{
//
//            }while(cursor.moveToNext());
        }

        return uid;
    }

    public void delUser(){
        //Open the database
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        db.execSQL("DELETE FROM " + TABLE_USER);

        //Close the database
        db.close();
    }

    public void saveNotif(Transaksi trs, String uid){
        SQLiteDatabase db=this.getWritableDatabase();

        String INSERT_NOTIF = "INSERT INTO "+TABLE_NOTIF+"("+KEY_ID_USER+","+KEY_TITLE
                +","+KEY_TGL_NOW+","+KEY_TGL_T+","+KEY_MESSAGE+","+KEY_CICILAN+","+KEY_T_HARGA
                +","+KEY_STATUS +") VALUES('"
                + uid + "','" + trs.getTitle() + "','" + trs.getTanggal_now() + "','"
                + trs.getTanggal_t() + "','" + trs.getMessage() + "','"
                + trs.getCicilan() + "','" + trs.getT_harga() + "','"
                + trs.getStatus() + "')";

        db.execSQL(INSERT_NOTIF);

        db.close();
    }

    public List<Transaksi> getNotif(String uid){
        List<Transaksi> listTrs = new ArrayList<Transaksi>();
        String query="SELECT * FROM "+TABLE_NOTIF+" WHERE " + KEY_ID_USER+" = '"+uid+"'";

        SQLiteDatabase dbsq = this.getReadableDatabase();
        Cursor cursor=dbsq.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Transaksi tsk = new Transaksi();
                tsk.setTitle(cursor.getString(1));
                tsk.setMessage(cursor.getString(2));
                tsk.setTanggal_now(cursor.getString(3));
                tsk.setTanggal_t(cursor.getString(4));
                tsk.setCicilan(cursor.getString(5));
                tsk.setT_harga(cursor.getString(6));
                tsk.setStatus(cursor.getString(7));
                listTrs.add(tsk);
            }while(cursor.moveToNext());
        }

        return listTrs;

    }

}