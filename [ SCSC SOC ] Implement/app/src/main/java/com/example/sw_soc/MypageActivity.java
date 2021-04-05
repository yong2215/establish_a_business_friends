package com.example.sw_soc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MypageActivity extends AppCompatActivity {

    String correct_pw = "";
    String correct_id = "";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference Ref;

    String compPw = "";
    String compNumber = "";
    String compHint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Intent intent = getIntent();
        final String id2 = intent.getExtras().getString("id");
        final String pw2 = intent.getExtras().getString("pw");
        final String name2 = intent.getExtras().getString("name");

        Button editBtn = (Button)findViewById(R.id.editButton);
        Button outBtn = (Button)findViewById(R.id.outButton);
        TextView homeBtn = (TextView)findViewById(R.id.homeButton);
        final TextView name = (TextView)findViewById(R.id.nameText);
        final TextView id = (TextView)findViewById(R.id.idText);
        final EditText pw = (EditText)findViewById(R.id.passwordText);
        final EditText number = (EditText)findViewById(R.id.numberText);
        final EditText hint = (EditText)findViewById(R.id.hintText);



        Ref = database.getReference("User");
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                while(child.hasNext()){
                    if(child.next().getKey().equals(id2)){
                        DatabaseReference infoPw = database.getReference("User/" + id2);
                        infoPw.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final User user = snapshot.getValue(User.class);
                                correct_id = user.id;
                                correct_pw = user.pw;
                                if(correct_id.equals(id2) && correct_pw.equals(pw2)){
                                    name.setText(user.name);
                                    id.setText(user.id);
                                    pw.setText(user.pw);
                                    number.setText(user.phoneNumber);
                                    hint.setText(user.hint);
                                    compPw = user.pw;
                                    compNumber = user.phoneNumber;
                                    compHint = user.hint;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(MypageActivity.this, MainActivity.class);
                homeIntent.putExtra("id",id2);
                homeIntent.putExtra("pw",pw2);
                homeIntent.putExtra("name",name2);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MypageActivity.this.startActivity(homeIntent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pw.getText().toString().getBytes().length >=25){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
                    builder.setMessage("Password를 25자 이내로 입력하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else if (compHint.equals(hint.getText().toString()) &&  compNumber.equals(number.getText().toString())&& compPw.equals(pw.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
                    builder.setMessage("수정한 내용이 없습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {
                    Ref = database.getReference("User");
                    Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                            while(child.hasNext()){
                                if(child.next().getKey().equals(id2)){
                                    final DatabaseReference infoPw = database.getReference("User/" + id2);
                                    infoPw.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            final User user = snapshot.getValue(User.class);
                                            correct_id = user.id;
                                            correct_pw = user.pw;
                                            if(correct_id.equals(id2) && correct_pw.equals(pw2)){
                                                infoPw.child("pw").setValue(pw.getText().toString());
                                                infoPw.child("phoneNumber").setValue(number.getText().toString());
                                                infoPw.child("hint").setValue(hint.getText().toString());
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent editIntent = new Intent(MypageActivity.this, MainActivity.class);
                    editIntent.putExtra("id",id2);
                    editIntent.putExtra("pw",pw2);
                    editIntent.putExtra("name",name2);
                    editIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MypageActivity.this.startActivity(editIntent);
                    Toast.makeText(getApplicationContext(),"정보가 수정되었습니다.",Toast.LENGTH_LONG).show();
                }

            }
        });

        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MypageActivity.this);
                builder.setMessage("정말로 탈퇴하시겠습니까?");
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Ref = database.getReference("User");
                        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                                while(child.hasNext()){
                                    if(child.next().getKey().equals(id2)){
                                        DatabaseReference infoPw = database.getReference("User/" + id2);
                                        infoPw.removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Intent outIntent = new Intent(MypageActivity.this, LoginActivity.class);
                        MypageActivity.this.startActivity(outIntent);
                        Toast.makeText(getApplicationContext(),"탈퇴가 완료되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();

            }
        });
    }
}