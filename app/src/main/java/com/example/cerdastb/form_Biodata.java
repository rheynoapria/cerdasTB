package com.example.cerdastb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cerdastb.Common.Common;
import com.example.cerdastb.Model.Question;
import com.example.cerdastb.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ChoiceFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import io.paperdb.Paper;

import static android.text.TextUtils.isEmpty;

public class form_Biodata extends AppCompatActivity {

    String gender="",jenisTB="";
    EditText nomorHP,edt_tgllahir,edt_kota;
    String no_hp="+62",number="";
    Button btn_start;
    private SimpleDateFormat dateFormatter;
    DatePickerDialog datePickerDialog;
    int pYear,  pMonth, pDay;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form__biodata);
        
        //views
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        nomorHP = (EditText)findViewById(R.id.noHP);
        edt_tgllahir = (EditText)findViewById(R.id.bornDate);
        edt_kota = (EditText)findViewById(R.id.asalKota);
        btn_start = (Button)findViewById(R.id.btnStart);
        Paper.init(this);
        Intent in = getIntent();
        Bundle b = in.getExtras();

        String nama = b.getString("namaPasien");
        System.out.print(nama);
        loadQuestion();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Save Biodata
                Paper.book().write(Common.User_Key, "+62"+nomorHP.getText().toString());
                Paper.book().write(Common.User_Name, nama);

                final DatabaseReference table_users = database.getReference("User");
                if (isEmpty(nomorHP.getText().toString()) || isEmpty(edt_tgllahir.getText().toString()) || isEmpty(edt_kota.getText().toString()) ) {

                    Toast.makeText(form_Biodata.this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();

                }else {
                    String pasien = nama;
                    Users users = new Users(jenisTB, gender,edt_kota.getText().toString(),"00-00-0000","00-00-0000", pasien, "+62"+nomorHP.getText().toString(), "0", "0","00-00-0000", edt_tgllahir.getText().toString());
                    table_users.child("+62"+nomorHP.getText().toString()).setValue(users);
                    users.setNoHandphone("+62"+nomorHP.getText().toString());
                    Common.currentUser = users;
                    showDialog();
                }

            }
        });
        

    }

    public void genderClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender = "Pria";
                break;
            case R.id.radio_female:
                if (checked)
                    gender = "Wanita";
                break;
        }
    }

    public void TBclicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_TB:
                if (checked)
                    jenisTB = "TB";
                break;
            case R.id.radio_TB_MDR:
                if (checked)
                    jenisTB = "TB MDR Regimen Jangka Pendek (12 Bulan)";
                break;

            case R.id.radio_TB_MDR2:
                if (checked)
                    jenisTB = "TB MDR Regimen Individu (24 Bulan)";
                break;

            case R.id.radio_non_tb:
                if (checked)
                    jenisTB = "Non TB";
                break;
        }
    }

    public void showDateDialog(View view) {

        final Calendar newCalendar = Calendar.getInstance();
        pYear = newCalendar.get(Calendar.YEAR);
        pMonth = newCalendar.get(Calendar.MONTH);
        pDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {


                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);


                edt_tgllahir.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());// TODO: used to hide future date,month and year

        datePickerDialog.show();
    }

    private void showDialog(){
        AlertDialog.Builder al = new AlertDialog.Builder(this);

        al.setTitle("Ayo Pretest!")
                .setMessage("Ayo Ikuti pretest Untuk melihat seberapa jauh Kamu tahu tentang penyakit TB ")
                .setIcon(R.drawable.ic_healt)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent pretest = new Intent(form_Biodata.this, Pretest.class);
                        startActivity(pretest);
                        finish();

                    }
                });
        AlertDialog alertDialog = al.create();

        alertDialog.show();
    }

    private void loadQuestion() {

        if (Common.questionList.size() > 0) {
            Common.questionList.clear();
        }

        final DatabaseReference table_pertanyaan = database.getReference("Question");
        table_pertanyaan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Question ques = postSnapshot.getValue(Question.class);
                    Common.questionList.add(ques);
                }

                Collections.shuffle(Common.questionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
