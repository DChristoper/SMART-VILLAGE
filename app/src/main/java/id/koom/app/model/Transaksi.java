package id.koom.app.model;

public class Transaksi {
    public String title;
    public String message;
    public String tanggal_now;
    public String tanggal_t;
    public String cicilan;
    public String t_harga;
    public String status;

    public String getTanggal_now() {
        return tanggal_now;
    }

    public void setTanggal_now(String tanggal_now) {
        this.tanggal_now = tanggal_now;
    }

    public String getTanggal_t() {
        return tanggal_t;
    }

    public void setTanggal_t(String tanggal_t) {
        this.tanggal_t = tanggal_t;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCicilan() {
        return cicilan;
    }

    public void setCicilan(String cicilan) {
        this.cicilan = cicilan;
    }

    public String getT_harga() {
        return t_harga;
    }

    public void setT_harga(String t_harga) {
        this.t_harga = t_harga;
    }
}
