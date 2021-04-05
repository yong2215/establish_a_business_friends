package com.example.sw_soc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference catRef;

    String brandName;

    List<String>brandList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("id");
        final String pw = intent.getExtras().getString("pw");
        final String name = intent.getExtras().getString("name");

        TextView homeBtn = (TextView)findViewById(R.id.homeButton);
        Button cafe = (Button)findViewById(R.id.cafeBtn);
        Button chicken = (Button)findViewById(R.id.chickenBtn);
        Button pizza = (Button)findViewById(R.id.pizzaBtn);
        Button flour = (Button)findViewById(R.id.flourBtn);
        Button fastfood = (Button)findViewById(R.id.fastBtn);
        Button drink = (Button)findViewById(R.id.drinkBtn);
        Button lodging = (Button)findViewById(R.id.lodgingBtn);
        Button conv = (Button)findViewById(R.id.convBtn);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, brandList);
        final ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(CategoryActivity.this, MainActivity.class);
                mainIntent.putExtra("id",id);
                mainIntent.putExtra("pw",pw);
                mainIntent.putExtra("name",name);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CategoryActivity.this.startActivity(mainIntent);
            }
        });

        fastfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/패스트푸드");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/주류");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        lodging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/숙소");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        conv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/편의점");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/카페");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/치킨");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/피자");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        flour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catRef = database.getReference("BrandName/분식");
                catRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        setBrandList(snapshot);
                        goToMap(listView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void setBrandList(DataSnapshot snapshot) {
        brandList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            String name = dataSnapshot.getValue(String.class);
            brandList.add(name);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void goToMap(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Map1Activity.class);
                brandName = brandList.get(position);
                intent.putExtra("brandname", brandName);
                startActivity(intent);
            }
        });
    }
}