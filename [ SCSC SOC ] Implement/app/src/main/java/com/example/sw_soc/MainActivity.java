package com.example.sw_soc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("id");
        final String pw = intent.getExtras().getString("pw");
        final String name = intent.getExtras().getString("name");

        TextView infoBtn = (TextView)findViewById(R.id.infoButton);
        Button categoryBtn = (Button) findViewById(R.id.categoryButton);
        Button bookmarkBtn = (Button) findViewById(R.id.bookmarkButton);
        Button searchBtn = (Button) findViewById(R.id.searchButton);
        Button logoutBtn = (Button) findViewById(R.id.logoutButton);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mypageIntent = new Intent(MainActivity.this, MypageActivity.class);
                mypageIntent.putExtra("id",id);
                mypageIntent.putExtra("pw",pw);
                mypageIntent.putExtra("name",name);
                MainActivity.this.startActivity(mypageIntent);
            }
        });

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                categoryIntent.putExtra("id",id);
                categoryIntent.putExtra("pw",pw);
                categoryIntent.putExtra("name",name);
                MainActivity.this.startActivity(categoryIntent);
            }
        });
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkActivity.class);
                bookmarkIntent.putExtra("id",id);
                bookmarkIntent.putExtra("pw",pw);
                bookmarkIntent.putExtra("name",name);
                MainActivity.this.startActivity(bookmarkIntent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this, Map2Activity.class);
                searchIntent.putExtra("id",id);
                searchIntent.putExtra("pw",pw);
                searchIntent.putExtra("name",name);
                MainActivity.this.startActivity(searchIntent);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(logoutIntent);
            }
        });
    }
}