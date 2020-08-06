package id.koom.app.helper;

import androidx.annotation.NonNull;

import id.koom.app.model.ShopProduct;

import java.util.List;

public interface ApiCallback {
    void onSuccess(@NonNull List<ShopProduct> value);
}
