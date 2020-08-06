package id.koom.app.rest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import id.koom.app.MainApp;
import id.koom.app.R;

import id.koom.app.helper.OffDBHelper;
import id.koom.app.model.Transaksi;
import id.koom.app.utils.SharedPrefManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyMessageService extends FirebaseMessagingService {

    public String TAG = "FIREBASE MESSAGING";
    public List<Transaksi> tsk = new ArrayList<>();
    Map<String, String> info;
    SharedPrefManager SPManager;
    OffDBHelper db;

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {

        broadcaster = LocalBroadcastManager.getInstance(this);
        SPManager = new SharedPrefManager(this);
        db = new OffDBHelper(this);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Transaksi trs = new Transaksi();
            trs.setTitle(remoteMessage.getData().get("title"));
            trs.setMessage(remoteMessage.getData().get("message"));
            trs.setTanggal_now(remoteMessage.getData().get("tgl_now"));
            trs.setTanggal_t(remoteMessage.getData().get("tgl_transaksi"));
            trs.setCicilan(remoteMessage.getData().get("cicilan"));
            trs.setT_harga(remoteMessage.getData().get("t_harga"));
            trs.setStatus(remoteMessage.getData().get("status"));

            String uid = db.getUser();

            db.saveNotif(trs, uid);

            Intent intent = new Intent("MyData");
            intent.putExtra("title", remoteMessage.getData().get("title"));
            intent.putExtra("body", remoteMessage.getData().get("message"));
            intent.putExtra("tanggal", remoteMessage.getData().get("tanggal"));
            broadcaster.sendBroadcast(intent);

            SPManager.saveSPString("title", remoteMessage.getData().get("title"));
            SPManager.saveSPString("uid", remoteMessage.getData().get("uid"));

            String msg = remoteMessage.getData().get("message");
            String sts = remoteMessage.getData().get("status");

            sendNotification(remoteMessage.getData().get("title"), msg + " " + sts);

//            startActivity(new Intent(this, ShopProductGrid.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    private void sendNotification(String title, String body) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationManager notificationManager =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel("default",
                        "Channel name",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Channel description");
                notificationManager.createNotificationChannel(channel);
            } else {
                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(this, MainApp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Notif", "Ada");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                Notification notif = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, notif);
            }
    }
}
