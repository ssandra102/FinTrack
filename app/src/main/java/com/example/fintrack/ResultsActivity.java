package com.example.fintrack;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.fintrack.databinding.ActivityResultsBinding;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    TextView gotoAnalytics, gotoCamera, goToUser, retrieve, add;
    EditText product, category;
    ListView listView, listView1;
    DatabaseReference rootRef, demoRef;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        gotoAnalytics =(TextView) findViewById(R.id.gotoAnalytics);
        gotoCamera = (TextView) findViewById(R.id.gotoCamera);
        goToUser = (TextView) findViewById(R.id.goToUser);
        listView = findViewById(R.id.listview);
        listView1 = findViewById(R.id.listview1);

        add = (TextView) findViewById(R.id.add);
        product = (EditText) findViewById(R.id.product);
        category = (EditText) findViewById(R.id.Category);

        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("data");

        gotoAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ResultsActivity.this, Analytics.class);
                startActivity(intent);
            }
        });
        gotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ResultsActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
        goToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ResultsActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_items, list);
        ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.list_items1, list1);

        listView.setAdapter(adapter);
        listView1.setAdapter(adapter1);
        demoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                list1.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if (!((snapshot.getValue().toString()).equals("0"))) {
                        list1.add(snapshot.getKey().toString());
                        list.add(snapshot.getValue().toString());
                    }

                }
                adapter1.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResultsActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });

        add.setOnClickListener((v) -> {
            String text = product.getText().toString();
            String cat = category.getText().toString();

            if(text.isEmpty()) {
                Toast.makeText(ResultsActivity.this, "No product entered", Toast.LENGTH_SHORT).show();
            } else if(cat.isEmpty()){
                Toast.makeText(ResultsActivity.this, "No category entered", Toast.LENGTH_SHORT).show();

            } else {
                // Provide custom child ref in place of "Product" and "category"
                FirebaseDatabase.getInstance().getReference().child("Product").child("product").setValue(text);
                FirebaseDatabase.getInstance().getReference().child("Product").child("category").setValue(cat);
                Toast.makeText(ResultsActivity.this, "Successfully added product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}