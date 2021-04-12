package com.TiLab;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PrimaryController {

    @FXML
    private TextField text;

    @FXML
    private TextField result;

    @FXML
    private Button encode;

    @FXML
    private TextField firstNum;

    @FXML
    private TextField secondNum;

    @FXML
    private TextField outputText;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField exp;

    @FXML
    private CheckBox checkBoxLetters;


    //
    public long firstNumber,secondNumber;
    public String inputText;
     static int Letters=63;

    @FXML
    void initialize() {

    }

    @FXML
    void expCheck(MouseEvent event) {
        if (checkBox.isSelected()){
            exp.setDisable(true);
        } else {
            exp.setDisable(false);
        }
    }

    @FXML
    void LettersCheck(MouseEvent event) {
        if (checkBoxLetters.isSelected()){
            Letters = 0;
        } else {
            Letters = 63;
        }
    }

    private static boolean isSimple(long a) { // Проверка простые ли числа
        for (long i = 2; i <= Math.sqrt(a); i++) {
            if (a % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTwoSimple(long a, long b) {  // Являются ли числа взаимно простыми
        if (a == b) {
            return a == 1;
        } else {
            if (a > b) {
                return isTwoSimple(a - b, b);
            } else {
                return isTwoSimple(b - a, a);
            }
        }
    }

    private static long getE(long f) {  // Вычисляем значение открытой экспненты e для генерации
        ArrayList<Long> valArr = new ArrayList<Long>();
        long e = f - 1;
        for (int i = 2; i < f; i++) {
            if (isSimple(e) && isTwoSimple(e, f)) {
                valArr.add(e);
            }
            e--;
        }
        Random random = new Random();
        int index = random.nextInt(valArr.size());
        return valArr.get(index);
    }

    public static int getExtendGcd( int a, int b) {  // Расширенный алгоритм Евклида

        int d0 = a; int d1 = b;
        int x0 = 1; int x1 = 0;
        int y0 = 0; int y1 = 1;
        while(d1 > 1) {
            int q = d0 / d1;
            int d2 = d0 % d1;
            int x2 = x0 - q * x1;
            int y2 = y0 - q * y1;
            d0 = d1; d1 = d2;
            x0 = x1; x1 = x2;
            y0 = y1; y1 = y2;
        }

        return (y1);
    }

    @FXML
    void encodeFunc(MouseEvent event) {
        firstNumber = Long.parseLong(firstNum.getText());
        secondNumber = Long.parseLong(secondNum.getText());

        inputText = text.getText();
        if(!checkBoxLetters.isSelected()){
           inputText = inputText.toUpperCase(Locale.ENGLISH);
           System.out.println(inputText);
        }

        long r = firstNumber * secondNumber;
        long f = (firstNumber-1)*(secondNumber-1);
        long e;
        if (checkBox.isSelected()) {
            e = getE(f);
            exp.setText(String.valueOf(e));
        } else {
            e = Long.parseLong(exp.getText());
        }

        // Расширенный алгоритм Евклида
        long d = Long.valueOf(getExtendGcd((int) f,(int) e ));
        if (d < 0) {
            d += f;
        }

        ArrayList<String> encryptMessage = RsaEncrypt(inputText, d, r);

        result.setText(encryptMessage.toString());

        String decryptMessage = RsaDecrypt(encryptMessage, e, r);

        outputText.setText(decryptMessage.toString());
    }

    private static ArrayList<String> RsaEncrypt(String s, long e, long r) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            long index = ((int) s.charAt(i)) - Letters;
            Long res = Power(index, e, r);
            result.add(res.toString());
        }
        return result;
    }

    private static String RsaDecrypt(List<String> input, long d, long r) {
        String result = "";

        for (String item : input) {
            long b = Long.parseLong(item);
            int index = (int) (Power(b, d, r) );
            result += (char) (index+ Letters);
        }
        return result;
    }

    public static long Power(long x, long y, long N) {  //Алгоритм возведения в степень
        if (y == 0) return 1;
        long z = Power(x, y / 2, N);
        if (y % 2 == 0)
            return (z * z) % N;
        else
            return (x * z * z) % N;
    }
}
