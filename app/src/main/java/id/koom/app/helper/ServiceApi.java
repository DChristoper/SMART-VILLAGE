package id.koom.app.helper;

import id.koom.app.model.ShopProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceApi {
    @GET("data_barang.php")
    Call<List<ShopProduct>> getSemuaBarang();


}