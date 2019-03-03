package com.example.cerdastb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cerdastb.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Pretest extends AppCompatActivity implements View.OnClickListener{

    int progressValue = 0;
    int index = 0, score = 0, correctAnswer, thisQuestion = 0, totalQuestion;

    ProgressBar progressBar;
    Button btn_jawabanA, btn_jawabanB, btn_jawabanC;
    TextView pertanyaan_text, txt_score, txt_totalQuestion;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest);

        //Casting
        txt_score = (TextView) findViewById(R.id.textScore);
        txt_totalQuestion = (TextView) findViewById(R.id.txt_totalQuestion);
        btn_jawabanA = (Button) findViewById(R.id.btn_jawabanA);
        btn_jawabanB = (Button) findViewById(R.id.btn_jawabanB);
        btn_jawabanC = (Button) findViewById(R.id.btn_jawabanC);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        pertanyaan_text = (TextView) findViewById(R.id.pertanyaan_text);

        btn_jawabanA.setOnClickListener(this);
        btn_jawabanB.setOnClickListener(this);
        btn_jawabanC.setOnClickListener(this);
        //End Casting


    }

    @Override
    public void onClick(View v) {

        Button clickedButten = (Button) v;
        if (clickedButten.getText().equals(Common.questionList.get(index).getJawabanBenar())) {

            //jawaban benar
            score += 10;
            correctAnswer++;
            showQuestion(++index); //jawaban selanjutnya
        } else {

            //jawaban salah
            score += 0;
            correctAnswer += 0;
            showQuestion(++index); //jawaban selanjutnya
        }
        txt_score.setText(String.format("%d", score));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txt_totalQuestion.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressValue++;
            progressBar.setProgress(progressValue);

            pertanyaan_text.setText(Common.questionList.get(index).getPertanyaan());

            btn_jawabanA.setText(Common.questionList.get(index).getJawabanA());
            btn_jawabanB.setText(Common.questionList.get(index).getJawabanB());
            btn_jawabanC.setText(Common.questionList.get(index).getJawabanC());

        } else {

            DatabaseReference table_user = database.getReference("User");
            table_user.child(Common.currentUser.getNoHandphone()).child("preTest").setValue(String.valueOf(score));

            String Score_Bagus = "Bagus Sekali !";
            String Score_KurangBagus = "Anda Kurang Belajar !!";

            AlertDialog.Builder al = new AlertDialog.Builder(Pretest.this);

            if(score > 80) {
                al.setTitle(Score_Bagus)
                        .setMessage("Score Anda : " + score)
                        .setIcon(R.drawable.ic_healt)
                        .setCancelable
                                (false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Common.currentUser.getJenisTb().equals("Non TB")){
                                    startActivity(new Intent(Pretest.this, Home.class));
                                    finish();
                                }else {
                                    Intent pretest = new Intent(Pretest.this, TanggalDiagnosa.class);
                                    startActivity(pretest);
                                    finish();
                                }
                            }
                        });
            } else if (score < 80) {
                al.setTitle(Score_KurangBagus)
                        .setMessage("Score Anda : " + score)
                        .setIcon(R.drawable.ic_healt)
                        .setCancelable
                                (false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (Common.currentUser.getJenisTb().equals("Non TB")){
                                    startActivity(new Intent(Pretest.this, Home.class));
                                    finish();

                                }else {
                                    Intent pretest = new Intent(Pretest.this, TanggalDiagnosa.class);
                                    startActivity(pretest);
                                    finish();
                                }
                            }
                        });
            }
            AlertDialog alertDialog = al.create();

            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();
        progressBar.setMax(totalQuestion);

        showQuestion(index);

    }
}
