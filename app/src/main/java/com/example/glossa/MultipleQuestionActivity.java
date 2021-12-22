package com.example.glossa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class MultipleQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MultipleQActivity";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://glossa-4dc57-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    boolean isUsed = false;
    private Button option1,
            option2,
            option3,
            option4,
            selected;
    private TextView question,
            qNum;
    private List<Question> questionList;
    private int currQues,
            score;
    private String testNo;
    private boolean isCompleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_multiple);
        question = findViewById(R.id.question);
        qNum = findViewById(R.id.quesCount);
        currQues = 0;

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                testNo = null;
            } else {
                testNo= extras.getString("testNo");
            }
        } else {
            testNo = (String) savedInstanceState.getSerializable("testNo");
        }

        questionList = new ArrayList<>();
        System.out.println(testNo);
        getQuestionsList(testNo);
       //System.out.println(db.collection("Testing/Level1/Test1").get(Source.valueOf("QU1")));
        score = 0;

    }

    private void getQuestionsList(String newString) {


        System.out.println(newString);

        if (newString.equals("Proficiency")) {
            System.out.println("entered if get questions");
            mDatabase.child(newString).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String question = "", A = "", B = "", C = "", D = "", answer = "";
                    int counter = 0;
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot child : children.getChildren()) {
                            if (child.getKey().equals("Question")) {
                                question = child.getValue().toString();
                                counter++;
                                System.out.println(question + " " + counter);
                            } else if (child.getKey().equals("answer")) {
                                answer = child.getValue() + "";
                                counter++;
                                System.out.println(answer + " " + counter);
                            } else if (child.getKey().equals("A")) {
                                A = child.getValue().toString();
                                counter++;
                                System.out.println(A + " " + counter);

                            } else if (child.getKey().equals("B")) {
                                B = child.getValue().toString();
                                counter++;
                                System.out.println(B + " " + counter);
                            } else if (child.getKey().equals("C")) {
                                C = child.getValue().toString();
                                counter++;
                                System.out.println(C + " " + counter);
                            } else if (child.getKey().equals("D")) {
                                D = child.getValue().toString();

                                counter++;
                                System.out.println(D + " " + counter);
                            }
                        }
                        questionList.add(new MultipleChoiceQuestion(question, A, B, C, D, answer));
                        System.out.println(answer);
                    }
                    setFirstQuestion();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            mDatabase.child("A1-1").child(newString).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String question = "", A = "", B = "", C = "", D = "", answer = "";
                    int counter = 0;
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        for (DataSnapshot child : children.getChildren()) {
                            if (child.getKey().equals("Question")) {
                                question = child.getValue().toString();
                                counter++;
                                //Systemout.println(question + " " +  counter);
                            } else if (child.getKey().equals("answer")) {
                                answer = child.getValue() + "";
                                counter++;
                                //Systemout.println(answer + " " +  counter);
                            } else if (child.getKey().equals("A")) {
                                A = child.getValue().toString();
                                counter++;
                                //Systemout.println(A + " " +  counter);

                            } else if (child.getKey().equals("B")) {
                                B = child.getValue().toString();
                                counter++;
                                //Systemout.println(B + " " +  counter);
                            } else if (child.getKey().equals("C")) {
                                C = child.getValue().toString();
                                counter++;
                                //Systemout.println(C + " " +  counter);
                            } else if (child.getKey().equals("D")) {
                                D = child.getValue().toString();
                                counter++;
                                //Systemout.println(D + " " +  counter);
                            }
                        }
                        questionList.add(new MultipleChoiceQuestion(question, A, B, C, D, answer));
                    }
                    setFirstQuestion();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
        //System.out.println(questionList.size());
        //String data = firebasedatabase().ref('indexCards').orderByChild('word').equalTo('noun')

//        questionList.add(new MultipleChoiceQuestion("Question 1", "A", "B", "C","D","4"));
//        questionList.add(new MultipleChoiceQuestion("Question 2", "b", "c", "a","d","4"));
//        questionList.add(new MultipleChoiceQuestion("Question 3", "y", "n", "k","s","4"));
//        questionList.add(new MultipleChoiceQuestion("Question 4", "r", "t", "b","v","4"));
//        questionList.add(new MultipleChoiceQuestion("Question 5", "w", "e", "n","x","4"));

        //
        //}


    private void setFirstQuestion() {
        question.setText(questionList.get(0).getQuestion());
        option1.setText(((MultipleChoiceQuestion)questionList.get(0)).getOption1());
        option2.setText(((MultipleChoiceQuestion)questionList.get(0)).getOption2());
        option3.setText(((MultipleChoiceQuestion)questionList.get(0)).getOption3());
        option4.setText(((MultipleChoiceQuestion)questionList.get(0)).getOption4());

        qNum.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));
    }
//
//      public void onClick(View v){
//        isUsed = true;
//        if(isUsed){
//            selected.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//
//        selected = findViewById(v.getId());
//        selected.setBackgroundColor(Color.parseColor("#03f0fc"));
//
//
//    }

    @Override
    public void onClick(View v) {
        int answer = 0;

        switch (v.getId()){
            case R.id.option1 :
                answer = 1;
                break;
            case R.id.option2 :
                answer = 2;
                break;
            case R.id.option3 :
                answer = 3;
                break;
            case R.id.option4 :
                answer = 4;
                break;
            default:
        }

        //answer = 4;
        checkAnswer(v,answer);

    }

    private void checkAnswer(View view, int answer) {
        if(questionList.get(currQues).checkAnswer(answer)){

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;


        } else {
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            if(questionList.get(currQues).getAnswer().equals("1")){
                option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if(questionList.get(currQues).getAnswer().equals("2")){
                option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if (questionList.get(currQues).getAnswer().equals("3")){
                option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if(questionList.get(currQues).getAnswer().equals("4")){
                option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
        }


        nextQuestion(view, answer);

    }

    private void nextQuestion( View view, int selected) {
        //Toast.makeText(this, "Entered", Toast.LENGTH_SHORT).show();
        if(currQues == questionList.size()-1){
            //display score - activity
            if(testNo.equals("Proficiency")){
                Intent intent = new Intent(MultipleQuestionActivity.this, ProficiencyScoreActivity.class);
                //ysdddddintent.putExtra("Score",String.valueOf(score) + "/" + String.valueOf(questionList.size()));
                intent.putExtra("Score",score );
                intent.putExtra("List size", questionList.size());
                startActivity(intent);
                MultipleQuestionActivity.this.finish();

            } else{
                Intent intent = new Intent(MultipleQuestionActivity.this, ScoreActivity.class);
                intent.putExtra("Score",String.valueOf(score) + "/" + String.valueOf(questionList.size()));
                if(!isCompleted && score > questionList.size()/2){
                    System.out.println("entered");
                    register.getUser().updateCompletedTest();
                    System.out.println("comp tests" + register.getUser().getCompletedTests());
                }
                startActivity(intent);
                MultipleQuestionActivity.this.finish();

            }


        } else {
            currQues++;
            playAnimation(question,0,0);
            playAnimation(option1,0,1);
            playAnimation(option2,0,2);
            playAnimation(option3,0,3);
            playAnimation(option4,0,4);

            qNum.setText(String.valueOf(currQues+1) + "/" + String.valueOf(questionList.size()));
//            currQues++;
//            ((TextView)findViewById(R.id.question)).setText(questionList.get(currQues).getQuestion());
//
//            ((Button)view).setText(questionList.get(currQues).getOption1());
//            ((Button)view).setText(questionList.get(currQues).getOption2());
//            ((Button)view).setText(questionList.get(currQues).getOption3());
//            ((Button)view).setText(questionList.get(currQues).getOption4());
//
//            qNum.setText(String.valueOf(currQues+1) + "/" + String.valueOf(questionList.size()));


//            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
//            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
//            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
//            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));


        }
    }

    private void playAnimation(View view, int value, int component) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(500)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    switch (component) {
                        case 0:
                            ((TextView) view).setText(questionList.get(currQues).getQuestion());
                            break;
                        case 1:
                            ((Button) view).setText(((MultipleChoiceQuestion)questionList.get(currQues)).getOption1());
                            break;
                        case 2:
                            ((Button) view).setText(((MultipleChoiceQuestion)questionList.get(currQues)).getOption2());
                            break;
                        case 3:
                            ((Button) view).setText(((MultipleChoiceQuestion)questionList.get(currQues)).getOption3());
                            break;
                        case 4:
                            ((Button) view).setText(((MultipleChoiceQuestion)questionList.get(currQues)).getOption4());
                            break;
                    }
                    playAnimation(view, 1, component);

                    if(component != 0){
                        ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF3700B3")));
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }); {

        }
    }


}