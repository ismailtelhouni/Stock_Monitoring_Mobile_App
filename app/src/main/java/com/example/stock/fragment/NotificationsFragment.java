package com.example.stock.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stock.R;
import com.example.stock.activity.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private static final String CHANNEL_ID = "notification_channel";
    private int notificationId = 0;
    private LinearLayout notificationsLayout;
    private DatabaseReference notificationsReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification, container, false);

        notificationsLayout = view.findViewById(R.id.notifications_layout);
        notificationsReference = FirebaseDatabase.getInstance().getReference("notification");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sonsor/temperature/last");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("temperature").exists()) {
                    int temp = dataSnapshot.child("temperature").getValue(Integer.class);
                    if (temp > 35) {
                        String currentTime = getCurrentTime();
                        showNotification("High Temperature Alert", "Temperature is " + temp + "°C");
                        saveNotificationToFirebase("High Temperature Alert", "Temperature is " + temp + "°C", currentTime);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        loadStoredNotifications();

        return view;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showNotification(String title, String message) {
        // Create an explicit intent for an activity in your app
        Intent intent = new Intent(getContext(), NotificationsFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create a notification
        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            builder = new Notification.Builder(getContext())
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }
        if (builder != null) {
            notificationManager.notify(notificationId++, builder.build());
        }
    }


    private void saveNotificationToFirebase(String title, String message, String currentTime) {
        String key = notificationsReference.push().getKey();
        Map<String, String> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("message", message);
        notification.put("time", currentTime);
        if (key != null) {
            notificationsReference.child(key).setValue(notification);
        }
    }

    private void loadStoredNotifications() {
        Query query = notificationsReference.orderByKey().limitToLast(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationsLayout.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String message = snapshot.child("message").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);
                    addNotificationToLayout(title, message, time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void addNotificationToLayout(String title, String message, String time) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView notificationView = (CardView) inflater.inflate(R.layout.item_notification, notificationsLayout, false);

        TextView titleView = notificationView.findViewById(R.id.notification_title);
        TextView messageView = notificationView.findViewById(R.id.notification_message);
        TextView timeView = notificationView.findViewById(R.id.notification_time);
        ImageView iconView = notificationView.findViewById(R.id.notification_icon);

        titleView.setText(title);
        messageView.setText(message);
        timeView.setText(time);

        notificationsLayout.addView(notificationView, 0);  // Ajouter la vue en haut
    }
}
