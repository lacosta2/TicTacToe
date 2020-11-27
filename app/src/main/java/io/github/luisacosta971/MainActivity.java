package io.github.luisacosta971;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] button = new Button[3][3];
    private TextView textp1;
    private TextView textp2;

    private int player1points;
    private int player2points;
    private boolean turn = true;
    private int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textp1 = findViewById(R.id.text_view_p1);
        textp2 = findViewById(R.id.text_view_p2);
        for(int i = 0;i < 3;i++) {
            for(int j = 0;j < 3;j++) {
                String b = "button_" + i + j;
                int resID = getResources().getIdentifier(b,"id",getPackageName());
                button[i][j] = findViewById(resID);
                button[i][j].setOnClickListener(this);
            }
        }
        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")) {
            return;
        }
        if(turn) {
            ((Button)v).setTextColor(Color.BLACK);
            ((Button)v).setText("X");
        } else {
            ((Button)v).setTextColor(Color.RED);
            ((Button)v).setText("O");
        }
        round++;

        if(win()) {
            if(turn) {
                player1wins();
            } else {
                player2wins();
            }
        } else if (round == 9) {
            draw();
        } else {
            turn = !turn;
        }
    }

    private boolean win() {
        String[][] board = new String[3][3];

        for(int i = 0;i < 3;i++) {
            for(int j = 0;j < 3;j++) {
                board[i][j] = button[i][j].getText().toString();
            }
        }
        //checks rows:
        for(int i = 0;i < 3;i++) {
            if(board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("")) {
                return true;
            }
        }
        //checks columns:
        for(int i = 0;i < 3;i++) {
            if(board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("")) {
                return true;
            }
        }
        //checks top left diagonal
        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("")) {
            return true;
        }
        //checks top right diagonal
        if(board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].equals("")) {
            return true;
        }
        
        return false;
    }
    private void player1wins() {
        player1points++;
        Toast.makeText(this,"Player 1 Wins!",Toast.LENGTH_SHORT).show();
        updatePointsText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        },100);
    }

    private void player2wins() {
        player2points++;
        Toast.makeText(this,"Player 2 Wins!",Toast.LENGTH_SHORT).show();
        updatePointsText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        },100);
    }

    private void draw() {
        Toast.makeText(this,"Draw.",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        },1000);
    }

    private void updatePointsText() {
        textp1.setText("Player 1: " + player1points);
        textp2.setText("Player 2: " + player2points);
    }

    private void resetBoard() {
        for(int i = 0;i < 3;i++) {
            for(int j = 0;j < 3;j++) {
                button[i][j].setText("");
            }
        }
        round = 0;
        turn = true;
    }

    private void resetGame() {
        player1points = 0;
        player2points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("round", round);
        outState.putInt("player1points", player1points);
        outState.putInt("player2points", player2points);
        outState.putBoolean("turn", turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        round = savedInstanceState.getInt("round");
        player1points = savedInstanceState.getInt("player1points");
        player2points = savedInstanceState.getInt("player2points");
        turn = savedInstanceState.getBoolean("turn");
    }
}