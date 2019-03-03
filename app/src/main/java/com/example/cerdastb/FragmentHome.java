package com.example.cerdastb;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cerdastb.Common.Common;
import com.example.cerdastb.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;


public class FragmentHome extends Fragment {


    TextView txt_nama, txt_kota, txt_tanggalLahir, txt_jkel, txt_jtb, nama, txt_minumObat,txt_diagnosa,txt_noHP;
    ImageView ic_gender;
    SimpleDateFormat dateFormatter;
    String minumObat="lol",jkel;
    DatabaseReference table_user = FirebaseDatabase.getInstance().getReference("User");

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Views
        txt_nama = view.findViewById(R.id.txt_nama);
        txt_diagnosa = view.findViewById(R.id.txt_diagnosa);
        txt_noHP= view.findViewById(R.id.txt_noHP);
        txt_kota = view.findViewById(R.id.txt_kota);
        txt_tanggalLahir = view.findViewById(R.id.txt_tanggalLahir);
        txt_jkel = view.findViewById(R.id.txt_jkel);
        txt_jtb = view.findViewById(R.id.txt_jtb);
        txt_minumObat = view.findViewById(R.id.txt_minumObat);
        ic_gender = view.findViewById(R.id.ic_gender);

        TypedArray avatarImg = getResources().obtainTypedArray(R.array.genderImg);


        Paper.init(getActivity());
        String User = Paper.book().read(Common.User_Key);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar newCalendar = Calendar.getInstance();


        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.child(User).getValue(Users.class);
                String mObat = user.getMinumObat();
                if(mObat.equals(dateFormatter.format(newCalendar.getTime()))){
                    minumObat = "Sudah";
                }
                else{
                    minumObat = "Belum";
                }

                txt_nama.setText(user.getNama());
                txt_noHP.setText(user.getNoHandphone());
                txt_kota.setText(user.getKota());
                txt_tanggalLahir.setText(user.getTanggalLahir());
                txt_jkel.setText(user.getJkel());
                txt_jtb.setText(user.getJenisTb());
                txt_diagnosa.setText(user.getTanggalDiagnosa());
                txt_minumObat.setText(minumObat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        jkel = Common.currentUser.getJkel().toString();
        if (jkel.equals("Pria")){
            ic_gender.setImageResource(avatarImg.getResourceId(0,-1));
        }else {
            ic_gender.setImageResource(avatarImg.getResourceId(1,-1));
        }

    }

}
