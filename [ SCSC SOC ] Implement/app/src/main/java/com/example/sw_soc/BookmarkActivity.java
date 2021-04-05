package com.example.sw_soc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Intent intent = getIntent();
        final String id = intent.getExtras().getString("id");
        final String name = intent.getExtras().getString("name");

        TextView subTitle = (TextView)findViewById(R.id.subtitletext);
        TextView brandName1 = (TextView)findViewById(R.id.dataText1);
        TextView brandName2 = (TextView)findViewById(R.id.dataText2);
        TextView brandName3 = (TextView)findViewById(R.id.dataText3);
        TextView brandName4 = (TextView)findViewById(R.id.dataText4);
        TextView brandName5 = (TextView)findViewById(R.id.dataText5);
        TextView brandName6 = (TextView)findViewById(R.id.dataText6);
        TextView brandName7 = (TextView)findViewById(R.id.dataText7);
        TextView brandName8 = (TextView)findViewById(R.id.dataText8);
        TextView brandName9 = (TextView)findViewById(R.id.dataText9);
        TextView brandName10 = (TextView)findViewById(R.id.dataText10);
        TextView brandName11 = (TextView)findViewById(R.id.dataText11);
        TextView brandName12 = (TextView)findViewById(R.id.dataText12);
        TextView brandName13 = (TextView)findViewById(R.id.dataText13);
        TextView brandName14 = (TextView)findViewById(R.id.dataText14);
        TextView brandName15 = (TextView)findViewById(R.id.dataText15);
        TextView brandName16 = (TextView)findViewById(R.id.dataText16);
        TextView brandName17 = (TextView)findViewById(R.id.dataText17);
        TextView brandName18 = (TextView)findViewById(R.id.dataText18);
        TextView brandName19 = (TextView)findViewById(R.id.dataText19);
        TextView brandName20 = (TextView)findViewById(R.id.dataText20);
        ImageView img1 = (ImageView)findViewById(R.id.img1);
        ImageView img2 = (ImageView)findViewById(R.id.img2);
        ImageView img3 = (ImageView)findViewById(R.id.img3);
        ImageView img4 = (ImageView)findViewById(R.id.img4);
        ImageView img5 = (ImageView)findViewById(R.id.img5);
        ImageView img6 = (ImageView)findViewById(R.id.img6);
        ImageView img7 = (ImageView)findViewById(R.id.img7);
        ImageView img8 = (ImageView)findViewById(R.id.img8);
        ImageView img9 = (ImageView)findViewById(R.id.img9);
        ImageView img10 = (ImageView)findViewById(R.id.img10);
        ImageView img11 = (ImageView)findViewById(R.id.img11);
        ImageView img12 = (ImageView)findViewById(R.id.img12);
        ImageView img13 = (ImageView)findViewById(R.id.img13);
        ImageView img14 = (ImageView)findViewById(R.id.img14);
        ImageView img15 = (ImageView)findViewById(R.id.img15);
        ImageView img16 = (ImageView)findViewById(R.id.img16);
        ImageView img17 = (ImageView)findViewById(R.id.img17);
        ImageView img18 = (ImageView)findViewById(R.id.img18);
        ImageView img19 = (ImageView)findViewById(R.id.img19);
        ImageView img20 = (ImageView)findViewById(R.id.img20);
        final TextView[] brandArr = {brandName1,brandName2,brandName3,brandName4,brandName5,brandName6,brandName7,brandName8,brandName9,brandName10,brandName11,brandName12,brandName13,brandName14,brandName15,brandName16,brandName17,brandName18,brandName19,brandName20};
        final ImageView[] imgArr = {img1,img2,img3,img4,img5,img6,img7,img8,img9,img10,img11,img12,img13,img14,img15,img16,img17,img18,img19,img20};
        final int[] clickNum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        final List list = new ArrayList<>(); // brandname

        subTitle.setText(name+"님의 즐겨찾기");

        TextView resetBtn = (TextView)findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                for(int u=0; u < 20; u++){
                    clickNum[u] = 0;
                    brandArr[u].setText("");
                    imgArr[u].setImageResource(R.drawable.white);
                }
                Ref = database.getReference("User/"+id);
                Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                        while(child.hasNext()){
                            if (child.next().getKey().equals("bookmark")){
                                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                DatabaseReference Ref2 = database2.getReference("User/"+id+"/bookmark");
                                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        for(DataSnapshot dataSnapshot2: snapshot2.getChildren())
                                            list.add(dataSnapshot2.getKey());
                                        int i =0;
                                        for(i=0; i<list.size() && i<20; i++){
                                            brandArr[i].setText(list.get(i).toString()); // 브랜드 이름 추가
                                            clickNum[i] = 1;
                                            imgArr[i].setImageResource(R.drawable.full);
                                            final int finalI = i;
                                            imgArr[i].setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                                                    builder.setMessage("삭제하시겠습니까?");
                                                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (clickNum[finalI] == 1){ // 즐겨찾기 DB에서 제거하는 코드추가
                                                                    FirebaseDatabase database4 = FirebaseDatabase.getInstance();
                                                                    DatabaseReference Ref4 = database4.getReference("User/"+id+"/bookmark/"+list.get(finalI));
                                                                    Ref4.removeValue();
                                                                    imgArr[finalI].setImageResource(R.drawable.white);
                                                                    clickNum[finalI] = 0;
                                                                    brandArr[finalI].setText("");
                                                                }

                                                            }
                                                        });
                                                        builder.show();
                                                    }

                                            });
                                        }
                                        for (i=list.size(); i<20 ;i++){
                                            imgArr[i].setImageResource(R.drawable.white);
                                            clickNum[i] = 0;
                                            brandArr[i].setText("");
                                            final int finalI1 = i;
                                            imgArr[i].setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error2) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });



        Ref = database.getReference("User/"+id);
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                while(child.hasNext()){
                    if (child.next().getKey().equals("bookmark")){
                        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        DatabaseReference Ref2 = database2.getReference("User/"+id+"/bookmark");
                        Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                for(DataSnapshot dataSnapshot2: snapshot2.getChildren())
                                    list.add(dataSnapshot2.getKey());
                                int i =0;
                                for(i=0; i<list.size() && i <20; i++){
                                    brandArr[i].setText(list.get(i).toString()); // 브랜드 이름 추가
                                    clickNum[i] = 1;
                                    imgArr[i].setImageResource(R.drawable.full);
                                    final int finalI = i;
                                    imgArr[i].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                                            builder.setMessage("삭제하시겠습니까?");
                                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (clickNum[finalI] == 1){ // 즐겨찾기 DB에서 제거하는 코드추가
                                                        FirebaseDatabase database4 = FirebaseDatabase.getInstance();
                                                        DatabaseReference Ref4 = database4.getReference("User/"+id+"/bookmark/"+list.get(finalI));
                                                        Ref4.removeValue();
                                                        imgArr[finalI].setImageResource(R.drawable.white);
                                                        clickNum[finalI] = 0;
                                                        brandArr[finalI].setText("");
                                                    }

                                                }
                                            });
                                            builder.show();

                                        }

                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error2) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}