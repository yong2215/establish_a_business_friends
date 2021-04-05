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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FindPwActivity extends AppCompatActivity {

    EditText edit_id;
    EditText edit_name;
    EditText edit_hint;
    EditText edit_number;

    String pw;
    String name;
    String hint;
    String phoneNumber;

    User user;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference idRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        Button searchBtn = (Button) findViewById(R.id.searchButton);
        edit_id = (EditText) findViewById(R.id.idText);
        edit_name = (EditText) findViewById(R.id.nameText);
        edit_number = (EditText) findViewById(R.id.numberText);
        edit_hint = (EditText) findViewById(R.id.hintText);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idRef = database.getReference("User");
                idRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> child = snapshot.getChildren().iterator();

                        while (child.hasNext())
                        {
                            if (child.next().getKey().equals(edit_id.getText().toString())) {
                                DatabaseReference myRef2 = database.getReference("User/" + edit_id.getText().toString()); // 해당 아이디의 테이블 참조
                                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user = snapshot.getValue(User.class);
                                        pw = user.pw;
                                        name = user.name;
                                        hint = user.hint;
                                        phoneNumber = user.phoneNumber;

                                        if (edit_id.getText().toString().length() == 0 || edit_name.getText().toString().length() == 0 ||
                                                edit_number.getText().toString().length() == 0 || edit_hint.getText().toString().length() == 0) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPwActivity.this);
                                            builder.setMessage("작성되지 않은 정보가 있습니다.");
                                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.show();
                                        }
                                        else if (name.equals(edit_name.getText().toString()) && phoneNumber.equals(edit_number.getText().toString()) && hint.equals(edit_hint.getText().toString())) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPwActivity.this);
                                            builder.setMessage("회원님의 비밀번호는 " + pw + " 입니다.");
                                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent searchIntent = new Intent(FindPwActivity.this, LoginActivity.class);
                                                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    FindPwActivity.this.startActivity(searchIntent);
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.show();
                                        }
                                        else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPwActivity.this);
                                            builder.setMessage("회원정보가 없습니다.");
                                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                return;
                            }
                        }
                        if (!child.hasNext()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindPwActivity.this);
                            builder.setMessage("회원정보가 없습니다. 회원가입 하시겠습니까?");
                            builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent searchIntent = new Intent(FindPwActivity.this, SignUpActivity.class);
                                    FindPwActivity.this.startActivity(searchIntent);
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent searchIntent = new Intent(FindPwActivity.this, LoginActivity.class);
                                    FindPwActivity.this.startActivity(searchIntent);
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}