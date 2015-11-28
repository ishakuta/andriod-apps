package com.ishakuta.ivan.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final int SCORE_THREE_POINTS = 3;
    final int SCORE_TWO_POINTS   = 2;
    final int SCORE_FREE_THROW   = 1;

    int scoreTeamA = 0;
    int scoreTeamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }

    /**
     * @param view button
     */
    public void btnThreePointClick(View view) {
        if (R.id.btn_team_a_three == view.getId()) {
            scoreTeamA += SCORE_THREE_POINTS;
            displayForTeamA(scoreTeamA);
        } else {
            scoreTeamB += SCORE_THREE_POINTS;
            displayForTeamB(scoreTeamB);
        }
    }

    /**
     * @param view button
     */
    public void btnTwoPointClick(View view) {

        if (R.id.btn_team_a_two == view.getId()) {
            scoreTeamA += SCORE_TWO_POINTS;
            displayForTeamA(scoreTeamA);
        } else {
            scoreTeamB += SCORE_TWO_POINTS;
            displayForTeamB(scoreTeamB);
        }

    }

    /**
     * @param view button
     */
    public void btnFreeThrowClick(View view) {
        if (R.id.btn_team_a_free == view.getId()) {
            scoreTeamA += SCORE_FREE_THROW;
            displayForTeamA(scoreTeamA);
        } else {
            scoreTeamB += SCORE_FREE_THROW;
            displayForTeamB(scoreTeamB);
        }
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    public void btnReset(View view) {
        scoreTeamA = 0;
        scoreTeamB = 0;

        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }
}
