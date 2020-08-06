package id.koom.app.data;

public class User {
    String nik, no_kk, nama, ttl, lama_warung, jk;

    public User (String nik, String no_kk, String nama, String lama_warung, String ttl, String jk) {
        this.nik = nik;
        this.no_kk = no_kk;
        this.nama = nama;
        this.ttl = ttl;
        this.lama_warung = lama_warung;
        this.jk = jk;
    }

    public String getNik() { return nik; }
    public String getNo_kk() { return no_kk;}
    public String getNama() { return nama;}
    public String getTtl() { return ttl;}
    public String getLama_warung() { return lama_warung;}
    public String getJk() { return jk;}

}
