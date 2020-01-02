package com.example.quizactivitylifecycle;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mNotAnsweredTrue;

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setNotAnsweredTrue(boolean notAnsweredTrue) { mNotAnsweredTrue = notAnsweredTrue; }

    public boolean getNotAnsweredTrue() { return mNotAnsweredTrue; }

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mNotAnsweredTrue = true;
    }
}
