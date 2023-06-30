package com.example.fintrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fintrack.databinding.ActivityAnalyticsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class Analytics extends AppCompatActivity {


    TextView gotoResults, goToUser, days,daysD,months,monthsM,total,totalT;
    DatabaseReference rootRef, demoRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        gotoResults = (TextView) findViewById(R.id.gotoResults);
        goToUser = (TextView) findViewById(R.id.goToUser);
        daysD = (TextView) findViewById(R.id.daysD);
        monthsM = (TextView) findViewById(R.id.monthsM);
        totalT = (TextView) findViewById(R.id.totalT);
        Button fetchButton = findViewById(R.id.btnFetch);

        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("expenditure");

        gotoResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Analytics.this, ResultsActivity.class);
                startActivity(intent);
            }
        });
        goToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Analytics.this, SecondActivity.class);
                startActivity(intent);
            }
        });


        demoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String daysd = snapshot.child("days").getValue().toString();
                System.out.println(daysd);
                daysD.setText(daysd);
                String monthsm = snapshot.child("months").getValue().toString();
                monthsM.setText(monthsm);
                System.out.println(monthsm);

                String totalt = snapshot.child("total").getValue().toString();
                totalT.setText(totalt);
                System.out.println(totalt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Analytics.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void addNotification() {
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.abc)
//                        .setContentTitle("Notifications Example")
//                        .setContentText("This is a test notification");
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
//
//        // Add as notification
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
//    }

}