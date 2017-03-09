package com.pna.johan.calculadoracosw;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Typeface latoFont = Typeface.createFromAsset(this.getAssets(), "Lato.ttf");
    //Object.setTypeface(latoFont);

    public TextView cDsiplay;
    public char operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hide Action Bar
        getSupportActionBar().hide();

        cDsiplay = (TextView) findViewById(R.id.cdisplay);
    }

    public void deleteAll(View view) {
        cDsiplay.setText("");
    }

    public void solveOperation(View view) {
        if(cDsiplay.getText() != null || !cDsiplay.getText().equals("")){
            double res = eval(cDsiplay.getText().toString());
            cDsiplay.setText(String.valueOf(res));
        }
    }

    public void addNumber(View view) {
        switch (view.getId()){
            case R.id.one:
                agregarNumero("1");
                break;
            case R.id.two:
                agregarNumero("2");
                break;
            case R.id.three:
                agregarNumero("3");
                break;
            case R.id.four:
                agregarNumero("4");
                break;
            case R.id.five:
                agregarNumero("5");
                break;
            case R.id.six:
                agregarNumero("6");
                break;
            case R.id.seven:
                agregarNumero("7");
                break;
            case R.id.eigth:
                agregarNumero("8");
                break;
            case R.id.nine:
                agregarNumero("9");
                break;
            case R.id.zero:
                agregarNumero("0");
                break;
            case R.id.comma:
                agregarNumero(".");
                break;
            case R.id.division:
                agregarNumero("÷");
                break;
            case R.id.multiplication:
                agregarNumero("x");
                break;
            case R.id.substract:
                agregarNumero("-");
                break;
            case R.id.add:
                agregarNumero("+");
                break;
        }
    }

    private void agregarNumero(String n) {
        if(cDsiplay.getText().toString() != null || !cDsiplay.getText().toString().equals("")){
            String actualText = cDsiplay.getText().toString();
            char[] toArrayText = actualText.toCharArray();
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < toArrayText.length; i++){
                sb.append(""+toArrayText[i]);
            }
            sb.append(n);
            cDsiplay.setText(sb.toString());
        }else{
            cDsiplay.setText(n);
        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('x')) x *= parseFactor(); // multiplication
                    else if (eat('÷')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                System.out.println(x);
                return x;
            }
        }.parse();
    }
}
