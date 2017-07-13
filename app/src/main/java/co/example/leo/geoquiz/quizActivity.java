package co.example.leo.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class quizActivity extends AppCompatActivity {


    private static final String TAG = "QuizActivity";

    private static final String KEY_INDEX = "index";

    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;

    private Button mFalseButton;

    private Button mNextButton;

    private Button mCheatButton;

    private boolean mIsCheater;

    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans,false),
            new Question(R.string.question_mideast,true),
            new Question(R.string.question_africa,true),
            new Question(R.string.qusetion_americas,true),
            new Question(R.string.question_asia,false)
    };

    private int mCurrentIndex = 0;

    private boolean[] mBooleen = new boolean[5];//采用一个boolean数组用来题目是否作弊过。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton = (Button)findViewById(R.id.TrueButton);
        mFalseButton = (Button)findViewById(R.id.FalseButton);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkAnswer(true);
            }
        });
        mFalseButton = (Button)findViewById(R.id.FalseButton);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkAnswer(false);
            }
        });
        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                Log.d(TAG, "onClick: " + mCurrentIndex);
                int requestion = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(requestion);
            }
        });
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(quizActivity.this,answerIsTrue);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean("quiz");
        }
        updateQuestion();

    }

    private void updateQuestion(){
        int requestion = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(requestion);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        if(mBooleen[mCurrentIndex]){  //如果mBoolean数组中当前位置为True 则toast提示。
            Toast.makeText(this,"Cheat!",Toast.LENGTH_SHORT).show();
        }else{
            if(userPressedTrue == answerIsTrue){
                Toast.makeText(this,"Bingo",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"False",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState: ");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putBoolean("quiz",mIsCheater);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return ;
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mBooleen[mCurrentIndex] = CheatActivity.wasAnswerShown(data); //CheatActivity返回数据的时候赋值给mBoolean数组 下标为当前题目位置
        }
    }
}
