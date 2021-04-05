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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindIdActivity extends AppCompatActivity {

    String id = "";

    int count;

    Button searchBtn;
    EditText edit_name;
    EditText edit_number;
    EditText edit_hint;


    List<User> userList = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference varRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        searchBtn = (Button) findViewById(R.id.searchButton);
        edit_name = (EditText) findViewById(R.id.nameText);
        edit_number = (EditText) findViewById(R.id.numberText);
        edit_hint = (EditText) findViewById(R.id.hintText);

        varRef = database.getReference("User");
        varRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                    count += 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_name.getText().toString().length() == 0 ||
                        edit_number.getText().toString().length() == 0 ||
                        edit_hint.getText().toString().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindIdActivity.this);
                    builder.setMessage("입력되지 않은 정보가 있습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else{
                    for (int i = 0; i < count; i++) {
                        String curname, curnumber, curhint;
                        curname = userList.get(i).name;
                        curnumber = userList.get(i).phoneNumber;
                        curhint = userList.get(i).hint;
                        if (curname.equals(edit_name.getText().toString()) &&
                                curnumber.equals(edit_number.getText().toString()) &&
                                curhint.equals(edit_hint.getText().toString())) {
                            id = userList.get(i).id;
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindIdActivity.this);
                            builder.setMessage("회원님의 ID는 " + id + " 입니다.");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent searchIntent = new Intent(FindIdActivity.this, LoginActivity.class);
                                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    FindIdActivity.this.startActivity(searchIntent);
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            break;
                        }
                        if (i == count - 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FindIdActivity.this);
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
                }
            }
        });
    }
}