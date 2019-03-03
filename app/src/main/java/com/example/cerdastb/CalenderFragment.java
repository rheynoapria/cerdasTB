package com.example.cerdastb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.allyants.notifyme.NotifyMe;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.cerdastb.Common.Common;
import com.example.cerdastb.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class CalenderFragment extends Fragment {
    ImageView img_minumObat, img_kunjunganDokter;
    CalendarView calendarView;
    TextView text, txt_today, txt_minumObat, txt_kunjunganDokter;
    Button btn_minumObat, btn_kunjungan, btn_ok, btn_simpan, btn_batal;
    private SimpleDateFormat dateFormatter;
    private String KEY_DATE, KEY_MONTH;
    private int date;
    private int month;
    private String selectedDate, mObat;

    String User = Paper.book().read(Common.User_Key);
    String kuj;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    List<EventDay> events = new ArrayList<>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("User");
    final DatabaseReference table_kunjunganDokter = database.getReference("KunjunganDokter");
    final Calendar newCalendar = Calendar.getInstance();
    public static final int NOTIFICATION_ID = 1;

    public CalenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Views
        text = view.findViewById(R.id.text);
        txt_today = view.findViewById(R.id.txt_today);
        txt_minumObat = view.findViewById(R.id.txt_minumObat);
        img_minumObat = view.findViewById(R.id.img_minumObat);
        img_kunjunganDokter = view.findViewById(R.id.img_kunjunganDokter);
        txt_kunjunganDokter = view.findViewById(R.id.txt_kunjunganDokter);

        final Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //if the user has taken medicine
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.child(User).getValue(Users.class);

                newCalendar.set(Calendar.HOUR_OF_DAY,6);
                newCalendar.set(Calendar.MINUTE,0);
                newCalendar.set(Calendar.SECOND,0);

                kuj = user.getKunjunganDokter();
                if (user.getMinumObat().equals(dateFormatter.format(newCalendar.getTime()))) {
                    img_minumObat.setImageResource(R.drawable.checked);
                    txt_minumObat.setText("Anda sudah minum obat");
                }else{

                    NotifyMe notifyMe = new NotifyMe.Builder(getActivity().getApplicationContext())
                            .title("Anda belum minum obat hari ini")
                            .content("Ayo minum obat, agar cepat sembuh")
                            .color(255,0,0,255)
                            .led_color(255,255,255,255)
                            .time(newCalendar)
                            .addAction(new Intent(),"Snooze",false)
                            .key("test")
                            .addAction(new Intent(),"Dismiss",true,false)
                            .addAction(new Intent(),"Done")
                            .large_icon(R.drawable.medicine)
                            .build();

                    img_minumObat.setImageResource(R.drawable.medicine);
                    txt_minumObat.setText("Anda belum minum obat");
                }

                if (user.getKunjunganDokter().equals(dateFormatter.format(newCalendar.getTime()))){
                    table_user.child(User).child("kunjunganDokter").setValue("00-00-0000");

                    NotifyMe notifyMe = new NotifyMe.Builder(getActivity().getApplicationContext())
                            .title("Ayo lakukan kunjungan hari ini")
                            .content("Anda mempunyai jadwal kunjungan pada hari ini")
                            .color(255,0,0,255)
                            .led_color(255,255,255,255)
                            .time(newCalendar)
                            .addAction(new Intent(),"Snooze",false)
                            .key("test")
                            .addAction(new Intent(),"Dismiss",true,false)
                            .addAction(new Intent(),"Done")
                            .large_icon(R.drawable.doctor)
                            .build();

                }else if (user.getKunjunganDokter().equals("00-00-0000")) {
                    img_kunjunganDokter.setImageResource(R.drawable.doctor);
                    txt_kunjunganDokter.setText("Atur tanggal kunjungan dokter");
                }else{
                    img_kunjunganDokter.setImageResource(R.drawable.checked);
                    txt_kunjunganDokter.setText("Anda Mempunya Jadwal pada tanggal "+user.getKunjunganDokter());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        for (int i = -1; i < 4; i++) {
//            Calendar calendars = Calendar.getInstance();
//            calendars.add(Calendar.THURSDAY, i );
//            events.add(new EventDay(calendars, R.drawable.medicine));
//        }

        com.applandeo.materialcalendarview.CalendarView calendarView = view.findViewById(R.id.calendarView);

        txt_today.setText("Hari ini : "+android.text.format.DateFormat.format("EEEE", newCalendar.getTime()) +", " +dateFormatter.format(newCalendar.getTime()));

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();

                selectedDate = dateFormatter.format(eventDay.getCalendar().getTime());

                DialogForm(selectedDate);
                Calendar calendars = Calendar.getInstance();
                events.add(new EventDay(clickedDayCalendar, R.drawable.medicine));
            }
        });
        calendarView.setEvents(events);
    }

    private void DialogForm(String selectedDate){

        android.app.AlertDialog.Builder al = new android.app.AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.minunm_obat, null);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        al.setTitle(dateFormatter.format(newCalendar.getTime()))
                .setView(dialogView)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(true);

        android.app.AlertDialog alertDialog = al.create();

        btn_minumObat = dialogView.findViewById(R.id.btn_minumObat);
        btn_kunjungan = dialogView.findViewById(R.id.btn_kunjungan);

        alertDialog.show();

        btn_minumObat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedDate.equals(dateFormatter.format(newCalendar.getTime()))){
                    alertDialog.dismiss();
                    informasi("Anda telah selesai minum obat");

                    Paper.init(getActivity());
                    table_user.child(User).child("minumObat").setValue(selectedDate);
                    img_minumObat.setImageResource(R.drawable.checked);
                    txt_minumObat.setText("Anda Sudah minum obat");
                }

                else{
                    alertDialog.dismiss();
                    informasi("Anda tidak bisa minum obat selain hari ini");
                }
            }
        });

        btn_kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                kunjunganDokterDialog(selectedDate);
            }
        });
    }

    public void informasi(String s) {

        android.app.AlertDialog.Builder al = new android.app.AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.informasi, null);

        al.setTitle("Informasi")
                .setView(dialogView)
                .setMessage(s)
                .setCancelable(true);

        android.app.AlertDialog alertDialog = al.create();
        alertDialog.show();

        btn_ok = dialogView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void kunjunganDokterDialog(String selectedDate) {

        android.app.AlertDialog.Builder al = new android.app.AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.kunjungandokter, null);

        al.setTitle(selectedDate)
                .setView(dialogView)
                .setCancelable(true);

        android.app.AlertDialog alertDialog = al.create();
        alertDialog.show();

        btn_simpan = dialogView.findViewById(R.id.btn_simpan);
        btn_batal = dialogView.findViewById(R.id.btn_batal);
        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Paper.init(getActivity());
                Users user = dataSnapshot.child(User).getValue(Users.class);
                mObat = user.getKunjunganDokter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mObat.equals("00-00-0000")){
                    Paper.init(getActivity());
                    table_user.child(User).child("kunjunganDokter").setValue(selectedDate);
                    alertDialog.dismiss();
                    informasi("Kunjungan Berhasil dibuat");

                    img_kunjunganDokter.setImageResource(R.drawable.checked);
                    txt_kunjunganDokter.setText("Anda Mempunya Jadwal di hari "+android.text.format.DateFormat.format("EEEE", newCalendar.getTime())+", "+ selectedDate);

                }else{

                    informasi("Anda harus menyelesaikan kunjungan pada tanggal "+kuj+" atau membatalkanya");

                }
                alertDialog.dismiss();
            }
        });
        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mObat.equals(selectedDate)) {

                    Paper.init(getActivity());
                    table_user.child(User).child("kunjunganDokter").setValue("00-00-0000");
                    informasi("Kunjungan Berhasil dibatalkan");

                }else{

                    informasi("Maaf anda tidak bisa membatalkan kunjungan yang anda tidak jadwalkan");
                }
                alertDialog.dismiss();
            }
        });

    }
}
