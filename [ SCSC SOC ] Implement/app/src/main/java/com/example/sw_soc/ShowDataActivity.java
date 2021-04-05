package com.example.sw_soc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShowDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        Intent intent = getIntent();
        String area = intent.getExtras().getString("area");

        TextView text1 = (TextView)findViewById(R.id.textView1);
        TextView text2 = (TextView)findViewById(R.id.textView2);
        TextView text3 = (TextView)findViewById(R.id.textView3);
        ImageView img1 = (ImageView)findViewById(R.id.dataText1);
        ImageView img2 = (ImageView)findViewById(R.id.dataText2);
        ImageView img3 = (ImageView)findViewById(R.id.dataText3);
        ImageView img4 = (ImageView)findViewById(R.id.dataText4);

        text1.setText(area+" 인구밀도");
        text2.setText(area+" 유입인구");
        switch (area){
            case "북구":
                img1.setImageResource(R.drawable.bukgu_density);
                img2.setImageResource(R.drawable.bukgu_come);
                img3.setImageResource(R.drawable.bukgu_1);
                img4.setImageResource(R.drawable.bukgu_2);
                break;
            case "동구":
                img1.setImageResource(R.drawable.donggu_density);
                img2.setImageResource(R.drawable.donggu_come);
                img3.setImageResource(R.drawable.donggu_1);
                img4.setImageResource(R.drawable.donggu_2);
                break;
            case "서구":
                img1.setImageResource(R.drawable.seogu_density);
                img2.setImageResource(R.drawable.seogu_come);
                img3.setImageResource(R.drawable.seogu_1);
                img4.setImageResource(R.drawable.seogu_2);
                break;
            case "중구":
                img1.setImageResource(R.drawable.zoonggu_density);
                img2.setImageResource(R.drawable.junggu_come);
                img3.setImageResource(R.drawable.junggu_1);
                img4.setImageResource(R.drawable.junggu_2);
                break;
            case "달서구":
                img1.setImageResource(R.drawable.dalseogu_density);
                img2.setImageResource(R.drawable.dalseogu_come);
                img3.setImageResource(R.drawable.dalseogu_1);
                img4.setImageResource(R.drawable.dalseogu_2);
                break;
            case "수성구":
                img1.setImageResource(R.drawable.sosunggu_density);
                img2.setImageResource(R.drawable.susunggu_come);
                img3.setImageResource(R.drawable.suseonggu_1);
                img4.setImageResource(R.drawable.suseonggu_2);
                break;
            case "남구":
                img1.setImageResource(R.drawable.namgu_density);
                img2.setImageResource(R.drawable.namgu_come);
                img3.setImageResource(R.drawable.namgu_1);
                img4.setImageResource(R.drawable.namgu_2);
                break;
            case "달성군":
                img1.setImageResource(R.drawable.dalsunggun_density);
                img2.setImageResource(R.drawable.dalsung_come);
                img3.setImageResource(R.drawable.dalseonggun_1);
                img4.setImageResource(R.drawable.dalseonggun_2);
                break;
        }

    }
}