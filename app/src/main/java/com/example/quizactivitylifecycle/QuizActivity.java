/*
From the book Android Programming: The Big Nerd Ranch Guide 3rd Edition
Continue building upon the trivia app, including overrides and preserving
data through changes in device orientation. Log calls during different parts
of the activity lifecycle,

Included challenges:
Preventing Repeat Answers
Graded Quiz

By Maxx Mudd 12/24/2019
* */
package com.example.quizactivitylifecycle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

// Cool tip: use Alt+Enter to summon IntelliJ and automatically add missing classes
// Side note: using autocompletion will automatically import classes

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";       // constant for identifying source in logs
    private static final String KEY_INDEX = "index";
    private static final String USER_SCORE = "0";
    private static final String TOTAL_ANSWERS = "0";
    private static final String BUTTON_STATE = "state";
    private static final String[] ANSWERED_STATE = new String[] { "state", "state", "state", "state", "state", "state"};

    private Button mTrueButton;
    private Button mFalseButton; // m for member prefix is naming convention from book
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    // create array of questions with their corresponding answers
    private Question[] mQuestionsBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;          // index of questions
    private int mUserScore = 0;             // score tally
    private int mTotalAnswers = 0;          // answer tally
    private boolean mButtonState = true;    // default button state
    private boolean[] mNotAnsweredArr =  {true, true, true, true, true, true}; // state of questions

    // do this upon creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");      // log onCreate event (must import log class)
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {                                           // check if a saved instance exists
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);     // assign saved data to local vars
            mUserScore = savedInstanceState.getInt(USER_SCORE, 0);
            mTotalAnswers = savedInstanceState.getInt(TOTAL_ANSWERS, 0);
            mButtonState = savedInstanceState.getBoolean(BUTTON_STATE);
            mNotAnsweredArr = savedInstanceState.getBooleanArray(String.valueOf(ANSWERED_STATE));

        }

        mTrueButton = (Button) findViewById(R.id.true_button);   // create true button, set as listener, create onClick
        mTrueButton.setEnabled(mQuestionsBank[mCurrentIndex].getNotAnsweredTrue());
        mTrueButton.setOnClickListener(new View.OnClickListener() { // <-- anonymous inner class, has no name, created
            @Override                                               // in argument for single use
            public void onClick(View v) {
                mQuestionsBank[mCurrentIndex].setNotAnsweredTrue(false);  // disable buttons after answering
                mNotAnsweredArr[mCurrentIndex] = false;
                setButtonState(false);
                checkAnswer(true); // create and display toast
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button); // create false button, set it as listener
        mFalseButton.setEnabled(mQuestionsBank[mCurrentIndex].getNotAnsweredTrue());
        mFalseButton.setOnClickListener(new View.OnClickListener() { // and create onClick method
            @Override
            public void onClick(View v) {
                mQuestionsBank[mCurrentIndex].setNotAnsweredTrue(false);  // disable buttons after answering
                mNotAnsweredArr[mCurrentIndex] = false;
                setButtonState(false);
                checkAnswer(false); // creates and displays a toast
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view); // create text view area and prime it for
        updateQuestion();                                                     // displaying questions

        mNextButton = (ImageButton) findViewById(R.id.next_button);      // create next button, set it as listener
        mNextButton.setOnClickListener(new View.OnClickListener() { // and create onClick method
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length; // use modulus to index next question,
                int question = mQuestionsBank[mCurrentIndex].getTextResId(); // allows wrap around
                mQuestionTextView.setText(question);                         // select and display next question
                setButtonState(mNotAnsweredArr[mCurrentIndex]);

            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);      // create prev button, set it as listener
        mPrevButton.setOnClickListener(new View.OnClickListener() {      // and create onClick method
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionsBank.length); // use modulus to wrap around to next
                mCurrentIndex %= mQuestionsBank.length;                      // question and ensuring index is positive
                int question = mQuestionsBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);                         // select and display prev question
                setButtonState(mNotAnsweredArr[mCurrentIndex]);

            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view); // allow clicking text to move onto
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {     // next question
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;  // use modulus to wrap around array
                int question = mQuestionsBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);                          // select and display next question
                setButtonState(mNotAnsweredArr[mCurrentIndex]);
            }
        });

        updateQuestion();   // update the question
    }

    @Override
    public void onStart() {                             // override various lifecycle methods and
        super.onStart();                                // log each call
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {        // save index, button, and answer states between
        super.onSaveInstanceState(savedInstanceState);                  // orientation change
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(USER_SCORE, mUserScore);
        savedInstanceState.putInt(TOTAL_ANSWERS, mTotalAnswers);
        savedInstanceState.putBoolean(BUTTON_STATE, mButtonState);
        savedInstanceState.putBooleanArray(String.valueOf(ANSWERED_STATE), mNotAnsweredArr);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    // method that updates the current question
    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();        // select and update question text based on
        mQuestionTextView.setText(question);                                // questions index
        setButtonState(mQuestionsBank[mCurrentIndex].getNotAnsweredTrue());
    }

    // method that checks answer
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();    // check if the answer is true

        int messageResId = 0;

        if(userPressedTrue == answerIsTrue) {           // select a toast to display
            messageResId = R.string.correct_toast;
            ++mUserScore;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();  // display toast
        ++mTotalAnswers;                                                       // add to answer tally

        if(mTotalAnswers == 6) {
            float mScore = ((float) mUserScore / 6) * 100;                                  // calculate and display
            mScore = Math.round(mScore);                                                    // user score with toast
            String scoreMessage = "Score: " + (int) mScore + "%";
            Toast.makeText(this, scoreMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void setButtonState(boolean butState) {
        mTrueButton.setEnabled(butState);   // set state of both buttons
        mFalseButton.setEnabled(butState);
        if(butState == false) {                                         // if question has been answered, disable it
            mQuestionsBank[mCurrentIndex].setNotAnsweredTrue(false);
        }
    }
}
