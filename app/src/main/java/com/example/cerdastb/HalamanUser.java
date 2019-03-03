package com.example.cerdastb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cerdastb.Adapter.AdminAdapter;
import com.example.cerdastb.Model.Admin;
import com.example.cerdastb.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HalamanUser extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reference;
    ArrayList<Users> list;
    AdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_user);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        list = new ArrayList<Users>();
        reference = FirebaseDatabase.getInstance().getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users p = dataSnapshot1.getValue(Users.class);
                    list.add(p);
                }
                adapter = new AdminAdapter(HalamanUser.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
