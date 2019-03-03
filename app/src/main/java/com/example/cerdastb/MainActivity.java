package com.example.cerdastb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cerdastb.Common.Common;
import com.example.cerdastb.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        //Check Remember
        String User = Paper.book().read(Common.User_Key);
        if(User !=null){
            Thread time = new Thread() {
                public void run() {
                    try {
                        sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        finish();
                    }
                }
            };
            time.start();
            Login(User);
        }else{
            final Intent i = new Intent(MainActivity.this, OnBoard.class);

            Thread time = new Thread() {
                public void run() {
                    try {
                        sleep(4000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(i);
                        finish();
                    }
                }
            };
            time.start();
        }
    }


    private void Login(final String User) {
        //        init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                        check exist user
                if (dataSnapshot.child(User).exists()) {

//                        Get user information
                    Users users = dataSnapshot.child(User).getValue(Users.class);
                        Intent home = new Intent(MainActivity.this,
                                Home.class);
                        startActivity(home);
                        users.setNoHandphone(User);
                        Common.currentUser = users;
                    }

                else{
                    Toast.makeText(MainActivity.this, "User not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

