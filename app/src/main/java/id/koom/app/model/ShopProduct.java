package id.koom.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShopProduct implements Serializable {

    @SerializedName("nama_barang")
    @Expose
    public String title;

    @SerializedName("harga")
    @Expose
    public int price;

    @SerializedName("gambar")
    @Expose
    public String image;

    @SerializedName("stok")
    @Expose
    public String stock;

    public ShopProduct() {
    }

    public String getImage() { return image; }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStock() { return stock; }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
