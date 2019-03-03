package com.example.cerdastb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

public class FormNama extends AppCompatActivity {

    Bundle bundle;
    public String nama;
    EditText gt_nama;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nama);

        gt_nama = (EditText)findViewById(R.id.cNama);
        submit = (Button)findViewById(R.id.btnSubmit);

        bundle = new Bundle();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isEmpty(gt_nama.getText().toString())) {

                    Toast.makeText(FormNama.this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();

                } else{
                    bundle.putString("namaPasien",gt_nama.getText().toString());
                    Intent intent = new Intent(FormNama.this,form_Biodata.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });




    }
}
