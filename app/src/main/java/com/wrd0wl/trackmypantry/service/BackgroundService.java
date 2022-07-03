package com.wrd0wl.trackmypantry.service;

import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES;
import static com.wrd0wl.trackmypantry.constants.Constants.CHANNEL_ID;
import static com.wrd0wl.trackmypantry.constants.Constants.CHANNEL_NAME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wrd0wl.trackmypantry.MainActivity;
import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.repository.PantryRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {
    Timer timer;
    TimerTask timerTask;
    Context context;
    SharedPreferences sharedPreferences;
    PantryRepository pantryRepository;
    long delay = 1000L*10;
    long period = 1000L*60*60*24;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
         return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        context = this;
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        pantryRepository = new PantryRepository(context);
        initTimerTask();
        return START_STICKY;
    }

    private void initTimerTask(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                List<ItemData> itemDataList;
                itemDataList = pantryRepository.getItemListRanOut(sharedPreferences.getString("email", null));
                if(itemDataList.size() > 0){
                    createNotification("Consumed!", "You have some products that were ran out. Please, update your pantry!", 1);
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(new Date());
                itemDataList = pantryRepository.getItemListExpired(sharedPreferences.getString("email", null), date);
                if(itemDataList.size() > 0){
                    createNotification("Expired!", "You have some expired products. Please, remove them from your pantry!", 2);
                }

                Date todayDate = new Date();
                String today = dateFormat.format(todayDate);
                String week = dateFormat.format(new Date(todayDate.getTime() + 7 * period));
                itemDataList = pantryRepository.getItemListExpiring(sharedPreferences.getString("email", null), today, week);
                if(itemDataList.size() > 0){
                    createNotification("Expiring!", "You have some products that will be expired soon. Please, don't forget to consume them!", 3);
                }
            }
        };
        timer.schedule(timerTask, delay, period);
    }

    private void createNotification(String title, String text, int notificationId){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setSmallIcon(R.drawable.ic_shopping_cart)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        startForeground(notificationId, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
