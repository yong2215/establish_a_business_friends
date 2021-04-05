package com.example.sw_soc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


public class SignUpActivity extends AppCompatActivity {

    EditText edit_id;
    EditText edit_pw;
    EditText edit_name;
    EditText edit_number;
    EditText edit_hint;
    EditText edit_pw2;

    public static String id;
    String pw;
    String pw2;
    String name;
    String phoneNumber;
    String hint;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button saveBtn = (Button) findViewById(R.id.saveButton);
        edit_id = (EditText) findViewById(R.id.idText);
        edit_pw = (EditText) findViewById(R.id.passwordText);
        edit_pw2 = (EditText) findViewById(R.id.password2Text);
        edit_name = (EditText) findViewById(R.id.nameText);
        edit_number = (EditText) findViewById(R.id.numberText);
        edit_hint = (EditText) findViewById(R.id.hintText);

        edit_id.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().getBytes().length >= 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("ID를 20자 이내로 입력하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

                userRef = database.getReference("User");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> child = snapshot.getChildren().iterator();

                        while (child.hasNext())
                        {
                            if (child.next().getKey().equals(edit_id.getText().toString()))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage("중복된 ID 입니다.");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        edit_pw.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().getBytes().length >= 25) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Password를 25자 이내로 입력하세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });

        edit_pw2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!edit_pw2.getText().toString().equals(edit_pw.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage("Password가 일치하지 않습니다.");
                        edit_pw2.setText(null);
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
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edit_id.getText().toString().length() == 0 || edit_pw.getText().toString().length() == 0 ||
                        edit_name.getText().toString().length() == 0 || edit_number.getText().toString().length() == 0 || edit_hint.getText().toString().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("작성되지 않은 정보가 있습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else {
                    id = edit_id.getText().toString();
                    pw = edit_pw.getText().toString();
                    pw2 = edit_pw2.getText().toString();
                    name = edit_name.getText().toString();
                    phoneNumber = edit_number.getText().toString();
                    hint = edit_hint.getText().toString();

                    userRef = database.getReference("User");
                    userRef.child("" + id).child("id").setValue(id);

                    userRef = userRef.child("" + id);
                    userRef.child("pw").setValue(pw);
                    userRef.child("name").setValue(name);
                    userRef.child("phoneNumber").setValue(phoneNumber);
                    userRef.child("hint").setValue(hint);

                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();

                    Intent saveIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                    saveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    saveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SignUpActivity.this.startActivity(saveIntent);
                }
            }
        });
    }
}