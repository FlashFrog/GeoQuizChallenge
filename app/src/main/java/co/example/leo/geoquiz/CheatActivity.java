package co.example.leo.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.leo.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN = "com.example.leo.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;

    private Button mShowAnswer;

    private boolean isCheat = false;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent i = new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mAnswerTextView = (TextView)findViewById(R.id.answerTextView);
        mShowAnswer = (Button)findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText("true");
                }else{
                    mAnswerTextView.setText("false");
                }
                setAnswerShownResult(true);
            }
        });
        if(savedInstanceState != null){
            //取出保存的内容 如果为true 可以在执行一次setAnswerShownResult()方法。
            boolean b = savedInstanceState.getBoolean("cheat");
            if(b){
                setAnswerShownResult(true);
            }
        }
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        //如果查看过答案 将isCheat设置为True
        isCheat = isAnswerShown;
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //旋转的时候进行保存
        savedInstanceState.putBoolean("cheat",isCheat);
    }
}
