package com.example.saglik.vocapp2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.activeandroid.ActiveAndroid;
import java.util.List;
import java.util.Random;
import static com.example.saglik.vocapp2.Record.getAll;
import static com.example.saglik.vocapp2.Record.getAllRandomized;

// create a new method for getting and setting questions and choices
// create an if loop. Quit if limit is reached , else call above method again

public class QuizActivity extends AppCompatActivity {
    int score = 0;
    int quizSize = 7;
    int questionIndex = 0;
    TextView questionView;
    TextView scoreBar;
    Button[] choices = new Button[4];
    Record question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ActiveAndroid.initialize(this);
        scoreBar = findViewById(R.id.scoreBar);
        final Button buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setEnabled(false);

        //Map buttons to button array
        choices[0] = findViewById(R.id.button1);
        choices[1] = findViewById(R.id.button2);
        choices[2] = findViewById(R.id.button3);
        choices[3] = findViewById(R.id.button4);

        setQuestionAndChoices();

        choices[0].setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                buttonNext.setEnabled(true);
                String question = questionView.getText().toString().toLowerCase();
                question = extractWord(question);
                String answer = choices[0].getText().toString().toLowerCase();
                String correctAnswer = null;
                List<Record> allRecords = getAll();
                for(Record r:allRecords)
                    if(r.french.equals(question))
                        correctAnswer = r.english;

                if(checkIfCorrect(question, answer)){
                    choices[0].setBackgroundColor(Color.GREEN);
                    score++;
                    scoreBar.setText(("Score: "+String.valueOf(score)));
                    Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                }
                else{
                    choices[0].setBackgroundColor(Color.RED);
                    assert correctAnswer != null;
                    Toast.makeText(getApplicationContext(), "INCORRECT! ANSWER IS: "+correctAnswer.toUpperCase(), Toast.LENGTH_SHORT).show();
                }
                if(questionIndex ==quizSize){
                    buttonNext.setText("FINISH");
                    Snackbar.make(v,"QUIZ FINISHED. CLICK FINISH TO QUIT!",Snackbar.LENGTH_INDEFINITE).show();
                }
                for(int k=0; k<4; k++)
                    choices[k].setClickable(false);
            }
        });

        choices[1].setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                buttonNext.setEnabled(true);
                String question = questionView.getText().toString().toLowerCase();
                question = extractWord(question);
                String answer = choices[1].getText().toString().toLowerCase();
                String correctAnswer = null;
                List<Record> allRecords = getAll();
                for(Record r:allRecords)
                    if(r.french.equals(question))
                        correctAnswer = r.english;

                if(checkIfCorrect(question, answer)){
                    choices[1].setBackgroundColor(Color.GREEN);
                    score++;
                    scoreBar.setText(("Score: "+String.valueOf(score)));
                    Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                }
                else{
                    choices[1].setBackgroundColor(Color.RED);
                    assert correctAnswer != null;
                    Toast.makeText(getApplicationContext(), "INCORRECT! ANSWER IS: "+correctAnswer.toUpperCase(), Toast.LENGTH_SHORT).show();
                }
                if(questionIndex ==quizSize){
                    buttonNext.setText("FINISH");
                    Snackbar.make(v,"QUIZ FINISHED. CLICK FINISH TO QUIT!",Snackbar.LENGTH_INDEFINITE).show();
                }
                for(int k=0; k<4; k++)
                    choices[k].setClickable(false);
            }
        });

        choices[2].setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                buttonNext.setEnabled(true);
                String question = questionView.getText().toString().toLowerCase();
                question = extractWord(question);
                String answer = choices[2].getText().toString().toLowerCase();
                String correctAnswer = null;
                List<Record> allRecords = getAll();
                for(Record r:allRecords)
                    if(r.french.equals(question))
                        correctAnswer = r.english;

                if(checkIfCorrect(question, answer)){
                    choices[2].setBackgroundColor(Color.GREEN);
                    score++;
                    scoreBar.setText(("Score: "+String.valueOf(score)));
                    Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                }
                else{
                    choices[2].setBackgroundColor(Color.RED);
                    assert correctAnswer != null;
                    Toast.makeText(getApplicationContext(), "INCORRECT! ANSWER IS: "+correctAnswer.toUpperCase(), Toast.LENGTH_SHORT).show();
                }
                if(questionIndex == quizSize){
                    buttonNext.setText("FINISH");
                    Snackbar.make(v,"QUIZ FINISHED. CLICK FINISH TO QUIT!",Snackbar.LENGTH_INDEFINITE).show();
                }
                for(int k=0; k<4; k++)
                    choices[k].setClickable(false);
            }
        });

        choices[3].setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                buttonNext.setEnabled(true);
                String question = questionView.getText().toString().toLowerCase();
                question = extractWord(question);
                String answer = choices[3].getText().toString().toLowerCase();
                String correctAnswer = null;
                List<Record> allRecords = getAll();
                for(Record r:allRecords)
                    if(r.french.equals(question))
                        correctAnswer = r.english;

                if(checkIfCorrect(question, answer)){
                    choices[3].setBackgroundColor(Color.GREEN);
                    score++;
                    scoreBar.setText(("Score: "+String.valueOf(score)));
                    Toast.makeText(getApplicationContext(), "CORRECT", Toast.LENGTH_SHORT).show();
                }
                else{
                    choices[3].setBackgroundColor(Color.RED);
                    assert correctAnswer != null;
                    Toast.makeText(getApplicationContext(), "INCORRECT! ANSWER IS: "+correctAnswer.toUpperCase(), Toast.LENGTH_SHORT).show();
                }
                if(questionIndex ==quizSize){
                    buttonNext.setText("FINISH");
                    Snackbar.make(v,"QUIZ FINISHED. CLICK FINISH TO QUIT!",Snackbar.LENGTH_INDEFINITE).show();
                }
                for(int k=0; k<4; k++)
                    choices[k].setClickable(false);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonNext.getText().toString().equals("FINISH")){
                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    setQuestionAndChoices();
                    setChoicesColorGray();
                    for(int k=0; k<4; k++)
                        choices[k].setClickable(true);
                    buttonNext.setEnabled(false);
                }
            }
        });
    }

    private String extractWord(String question) {
        //"What does \" ...  \" mean?"
        return question.substring(11,question.length()-18);
    }

    private boolean checkIfCorrect(String question, String answer) {
        List<Record> allRecords = getAll();
        for(Record r:allRecords)
            if(r.french.equals(question) && r.english.equals(answer))
                return true;
        return false;
    }

    @SuppressLint("SetTextI18n")
    public void setQuestionAndChoices (){
        //Increase question number
        questionIndex++;

        //Set question
        List<Record> allRecordsMixed = getAllRandomized();
        question = allRecordsMixed.get(0);
        questionView = findViewById(R.id.questionView);
        questionView.setText("What does \"" + question.french.toUpperCase() + "\" mean in English?");

        //Pick 4 answers including right answer
        String rightAnswer = question.english;
        String wrongAnswer1 = allRecordsMixed.get(1).english;
        String wrongAnswer2 = allRecordsMixed.get(2).english;
        String wrongAnswer3 = allRecordsMixed.get(3).english;

        //Makes sure that right answer will not be displayed always in same button
        int r = generateRandom();
        choices[r%4].setText(rightAnswer);
        choices[(r +1)%4].setText(wrongAnswer1);
        choices[(r +2)%4].setText(wrongAnswer2);
        choices[(r +3)%4].setText(wrongAnswer3);
    }

    private void setChoicesColorGray() {
        choices[0].setBackgroundColor(Color.LTGRAY);
        choices[1].setBackgroundColor(Color.LTGRAY);
        choices[2].setBackgroundColor(Color.LTGRAY);
        choices[3].setBackgroundColor(Color.LTGRAY);
    }

    private int generateRandom() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }

}
