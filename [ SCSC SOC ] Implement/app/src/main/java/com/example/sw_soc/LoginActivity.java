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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    int count;

    EditText edit_id;
    EditText edit_pw;

    List<User> userList = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference infoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button)findViewById(R.id.loginButton);
        TextView findIdBtn = (TextView)findViewById(R.id.idButton);
        TextView findPwBtn = (TextView)findViewById(R.id.pwButton);
        TextView SignUpBtn = (TextView)findViewById(R.id.signUpButton);
        edit_id = (EditText) findViewById(R.id.idText);
        edit_pw = (EditText) findViewById(R.id.passwordText);

        infoRef = database.getReference("User");
        infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
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


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_id.getText().toString().length() == 0 || edit_pw.getText().toString().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("입력되지 않은 정보가 있습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {
                    for (int i = 0; i < count; i++) {
                        String curId, curPw;
                        curId = userList.get(i).id;
                        curPw = userList.get(i).pw;
                        if (curId.equals(edit_id.getText().toString()) &&
                                curPw.equals(edit_pw.getText().toString())) {
                            Intent searchIntent = new Intent(LoginActivity.this, MainActivity.class);
                            searchIntent.putExtra("id",userList.get(i).id);
                            searchIntent.putExtra("pw",userList.get(i).pw);
                            searchIntent.putExtra("name",userList.get(i).name);
                            LoginActivity.this.startActivity(searchIntent);
                            break;
                        }
                        if (i == count - 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("ID 혹은 Password가 일치하지 않거나" + "\n" + "가입되지 않은 사용자입니다.");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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

        findIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findIdIntent = new Intent(LoginActivity.this, FindIdActivity.class);
                LoginActivity.this.startActivity(findIdIntent);
            }
        });

        findPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPwIntent = new Intent(LoginActivity.this, FindPwActivity.class);
                LoginActivity.this.startActivity(findPwIntent);
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(SignUpIntent);
            }
        });

    }
}