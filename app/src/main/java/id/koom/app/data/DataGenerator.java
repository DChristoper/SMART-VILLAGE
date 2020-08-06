package id.koom.app.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import id.koom.app.R;
import id.koom.app.helper.ApiCallback;
import id.koom.app.helper.ServiceApi;
import id.koom.app.model.CardViewImg;
import id.koom.app.model.Image;
import id.koom.app.model.Inbox;
import id.koom.app.model.MusicAlbum;
import id.koom.app.model.MusicSong;
import id.koom.app.model.People;
import id.koom.app.model.ShopCategory;
import id.koom.app.model.ShopProduct;
import id.koom.app.model.Social;
import id.koom.app.utils.MaterialColor;
import id.koom.app.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ResourceType")
public class DataGenerator {

    private static Random r = new Random();

    public static final String ROOT_URL = "http://api.djcorp.co.id/test/";

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    public static List<String> getStringsShort(Context ctx) {
        List<String> items = new ArrayList<>();
        String name_arr[] = ctx.getResources().getStringArray(R.array.strings_short);
        for (String s : name_arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }

    public static List<Integer> getNatureImages(Context ctx) {
        List<Integer> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_product_image);
        for (int i = 0; i < drw_arr.length(); i++) {
            items.add(drw_arr.getResourceId(i, -1));
        }
        Collections.shuffle(items);
        return items;
    }

    public static List<String> getStringsMonth(Context ctx) {
        List<String> items = new ArrayList<>();
        String arr[] = ctx.getResources().getStringArray(R.array.month);
        for (String s : arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data CardViewImg
     *
     * @param ctx   android context
     * @param count total result data
     * @return list of object
     */
    public static List<CardViewImg> getCardImageData(Context ctx, int count) {

        List<CardViewImg> items = new ArrayList<>();

        List<Integer> images = getNatureImages(ctx);
        List<String> titles = getStringsShort(ctx);
        List<String> subtitles = getStringsShort(ctx);

        for (int i = 0; i < count; i++) {
            CardViewImg obj = new CardViewImg();
            obj.image = images.get(getRandomIndex(images.size()));
            obj.title = titles.get(getRandomIndex(titles.size()));
            obj.subtitle = subtitles.get(getRandomIndex(subtitles.size()));
            items.add(obj);
        }
        return items;
    }

    /**
     * Generate dummy data people
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<People> getPeopleData(Context ctx) {
        List<People> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);

        for (int i = 0; i < drw_arr.length(); i++) {
            People obj = new People();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            obj.email = Tools.getEmailFromName(obj.name);
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data social
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Social> getSocialData(Context ctx) {
        List<Social> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.social_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.social_names);

        for (int i = 0; i < drw_arr.length(); i++) {
            Social obj = new Social();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data inbox
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Inbox> getInboxData(Context ctx) {
        List<Inbox> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        String date_arr[] = ctx.getResources().getStringArray(R.array.general_date);

        for (int i = 0; i < drw_arr.length(); i++) {
            Inbox obj = new Inbox();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.from = name_arr[i];
            obj.email = Tools.getEmailFromName(obj.from);
            obj.message = ctx.getResources().getString(R.string.lorem_ipsum);
            obj.date = date_arr[randInt(date_arr.length - 1)];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data image
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Image> getImageDate(Context ctx) {
        List<Image> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_product_image);
        String name_arr[] = ctx.getResources().getStringArray(R.array.sample_images_name);
        String date_arr[] = ctx.getResources().getStringArray(R.array.general_date);
        for (int i = 0; i < drw_arr.length(); i++) {
            Image obj = new Image();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            obj.brief = date_arr[randInt(date_arr.length - 1)];
            obj.counter = r.nextBoolean() ? randInt(500) : null;
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data shopping category
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<ShopCategory> getShoppingCategory(Context ctx) {
        List<ShopCategory> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_category_icon);
        TypedArray drw_arr_bg = ctx.getResources().obtainTypedArray(R.array.shop_category_bg);
        String title_arr[] = ctx.getResources().getStringArray(R.array.shop_category_title);
        String brief_arr[] = ctx.getResources().getStringArray(R.array.shop_category_brief);
        for (int i = 0; i < drw_arr.length(); i++) {
            ShopCategory obj = new ShopCategory();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.image_bg = drw_arr_bg.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.brief = brief_arr[i];
            obj.imageDrw = AppCompatResources.getDrawable(ctx, obj.image);
            items.add(obj);
        }
        return items;
    }

    /**
     * Generate dummy data shopping product
     *
     *
     * @return list of object
     */

    ArrayList<ShopProduct> listShopProduct = new ArrayList<ShopProduct>();
    void setListShopProduct(ArrayList<ShopProduct> listShopProduct){
        this.listShopProduct = listShopProduct;
        Log.d("RESPON", "setList");
    }

    public List<ShopProduct> getList(){
        return listShopProduct;
    }

    public void getShoppingProduct(final ApiCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceApi service = retrofit.create(ServiceApi.class);

        Call<List<ShopProduct>> call = service.getSemuaBarang();
        call.enqueue(new Callback<List<ShopProduct>>() {

            @Override
            public void onResponse(Call<List<ShopProduct>> call, Response<List<ShopProduct>> response) {

                if (response.isSuccessful()) {
//                    ArrayList<ShopProduct> listShopProduct = new ArrayList<ShopProduct>();
                    int jumlah = response.body().size();

                    for (int i = 0; i < jumlah; i++) {

                        ShopProduct data = new ShopProduct();
                        data.setTitle(response.body().get(i).getTitle());
                        data.setPrice(response.body().get(i).getPrice());
                        data.setImage(response.body().get(i).getImage());
                        data.setStock(response.body().get(i).getStock());
                        listShopProduct.add(data);
                        Log.d("RESPON", listShopProduct.get(i).getTitle());
                        Log.d("RESPON", String.valueOf(listShopProduct.size()));
                    }
                    callback.onSuccess(listShopProduct);
                    setListShopProduct(listShopProduct);
                    Log.d("RESPON", String.valueOf(getList().size()));
//                    this.listShopProduct2 = new ArrayList<>(listShopProduct);

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Log.d("RESPON", "onResponse: " + error);
                }
            }


            @Override
            public void onFailure(Call<List<ShopProduct>> call, Throwable t) {
                String error = "Error Retrive Data from Server wwaau!!!\n" + t.getMessage();
                Log.d("RESPON", "onResponse: " + error);
            }

        });

//        return getList();

    }


    /**
     * Generate dummy data music song
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<MusicSong> getMusicSong(Context ctx) {
        List<MusicSong> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.album_cover);
        String song_name[] = ctx.getResources().getStringArray(R.array.song_name);
        String album_name[] = ctx.getResources().getStringArray(R.array.album_name);
        for (int i = 0; i < drw_arr.length(); i++) {
            MusicSong obj = new MusicSong();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.title = song_name[i];
            obj.brief = album_name[i];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data music album
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<MusicAlbum> getMusicAlbum(Context ctx) {
        List<MusicAlbum> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.album_cover);
        String album_name[] = ctx.getResources().getStringArray(R.array.album_name);
        for (int i = 0; i < drw_arr.length(); i++) {
            MusicAlbum obj = new MusicAlbum();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = album_name[i];
            obj.brief = getRandomIndex(15) + " MusicSong (s)";
            obj.color = MaterialColor.getColor(ctx, obj.name, i);
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        return items;
    }

    public static String formatTime(long time) {
        // income time
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);

        // current time
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MMM d", Locale.US);
            }
        } else {
            dateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        }
        return dateFormat.format(time);
    }

    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
