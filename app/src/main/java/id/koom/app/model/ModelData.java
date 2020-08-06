package id.koom.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelData {

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("nama_user")
    @Expose
    private String nama;


    public static final String id_user = "ID_USER";
    public static final String nama_user = "ID_USER";


    public ModelData(String id, String nama, String kelas_mhs) {
        this.idUser = id;
        this.nama = nama;
    }

    public String getidUser()
    {
        return idUser;
    }

    public void setIdUser(String idUser)
    {
        this.idUser = idUser;
    }

    public String getNama()
    {
        return nama;
    }


    public void setNama(String nama)
    {
        this.nama = nama;
    }

}
