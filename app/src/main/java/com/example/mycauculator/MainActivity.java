package com.example.mycauculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.lang.String;
import java.util.Stack;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputNumber;
    boolean clear_flag;//清空标识
    //容错标志
    boolean flag_0 = false;//限制数字开头的0
    boolean flag_point = false;//限制数字小数点个数1
    boolean flag_operator = false;//限制操作符数量1，连续输入运算符标志
    boolean flag_num = false;//数字输入开始

    Stack stk_point = new Stack();//记录小数点的输入状态，del时不重复输入容错
    Stack stk_Length = new Stack();//记录每一个数的长度(包括小数点)
    int numLength = 0;//数字长度，初始0；
    Stack stk_operator = new Stack();//顺序运算符栈
    Stack stk_num = new Stack();//顺序数字栈
    Stack stk_midop = new Stack();//逆序运算符栈
    Stack stk_midnum = new Stack();//逆序数字栈


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_port);

        Button button1 = (Button) this.findViewById(R.id.button1);
        Button button2 = (Button) this.findViewById(R.id.button2);
        Button button3 = (Button) this.findViewById(R.id.button3);
        Button button4 = (Button) this.findViewById(R.id.button4);
        Button button5 = (Button) this.findViewById(R.id.button5);
        Button button6 = (Button) this.findViewById(R.id.button6);
        Button button7 = (Button) this.findViewById(R.id.button7);
        Button button8 = (Button) this.findViewById(R.id.button8);
        Button button9 = (Button) this.findViewById(R.id.button9);
        Button button0 = (Button) this.findViewById(R.id.button0);
        Button buttonPoint = (Button) this.findViewById(R.id.buttonPoint);
        Button buttonAdd = (Button) this.findViewById(R.id.buttonAdd);
        Button buttonSub = (Button) this.findViewById(R.id.buttonSub);
        Button buttonMul = (Button) this.findViewById(R.id.buttonMul);
        Button buttonDiv = (Button) this.findViewById(R.id.buttonDiv);
        Button buttonEql = (Button) this.findViewById(R.id.buttonEql);
        Button buttonClear = (Button) this.findViewById(R.id.buttonC);
        Button buttonDelete = (Button) this.findViewById(R.id.buttonDel);
        Button buttonBin = (Button) this.findViewById(R.id.buttonChange);
        Button buttonLeft = (Button) this.findViewById(R.id.buttonLeft);
        Button buttonRight = (Button) this.findViewById(R.id.buttonRight);
        inputNumber = (EditText) findViewById(R.id.text);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonSub.setOnClickListener(this);
        buttonMul.setOnClickListener(this);
        buttonDiv.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonEql.setOnClickListener(this);
        buttonBin.setOnClickListener(this);
        //buttonLeft.setOnClickListener(this);
        //buttonRight.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String str = inputNumber.getText().toString();
        switch (view.getId()){
            case R.id.button0:
                if(clear_flag){                                     //输入前清空结果
                    clear_flag = false;
                    str="";
                    inputNumber.setText("");
                }
                if(flag_0 == true){                                 //0输入受限
                    return;
                }
                if(flag_num == false){                              //数字输入未开始
                    flag_0 = true;                                  //限制0的个数1
                    numLength++;
                    inputNumber.setText(str+((Button)view).getText());//若先输入0，整数部分会以一个0开头
                }else{                                              //数字输入开始
                    if(flag_0 == true){flag_0 = false;}             //若有限制，则取消
                }
                if(flag_0 == false){                                //0输入不受限
                    inputNumber.setText(str+((Button)view).getText());
                    numLength++;
                }
                break;

            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
            //case R.id.buttonLeft:
            //case R.id.buttonRight:
                if(clear_flag){
                    clear_flag = false;
                    str="";
                    inputNumber.setText("");
                }
                if(flag_num == false){
                    flag_num = true;                                //数字输入开始
                    flag_0 = false;                                 //不限制0的输入
                    flag_operator = false;                          //置连续输入运算符为false，无运算符输入
                }
                inputNumber.setText(str+((Button)view).getText());
                numLength++;
                break;

            case R.id.buttonPoint:
                if(clear_flag){
                    clear_flag = false;
                    str="";
                    inputNumber.setText("");
                }
                if(flag_point == true){                             //已有小数点，限制输入
                    return;
                }else{                                              //无小数点可以输入
                    // stk_point.push(true);                         //记录小数点输入状态
                    flag_point = true;                              //已有一个小数点
                    if(flag_num == false){                          //数字输入可以开始
                        flag_num = true;
                        flag_0 = false;
                        flag_operator = false;
                    }
                    inputNumber.setText(str + ((Button) view).getText());
                    numLength++;
                }
                break;

            case R.id.buttonAdd:
            case R.id.buttonSub:
            case R.id.buttonMul:
            case R.id.buttonDiv:
                if(clear_flag){
                    clear_flag = false;
                    str="";
                    inputNumber.setText("");
                }
                if(flag_operator == false){                                 //无运算符输入
                    flag_operator = true;                                   //已输入运算符
                    flag_num = false;                                       //数字输入结束
                    if(flag_point == false){                                //数字输入无小数点时也记录状态
                        stk_point.push(false);
                    }else {
                        stk_point.push(true);
                        flag_point = false;
                    }
                    stk_Length.push(numLength);
                    numLength = 0;
                    inputNumber.setText(str+" "+((Button)view).getText()+" ");////
                }else{                                                      //已有运算符输入
                    inputNumber.setText(str.substring(0,str.length()-3)+" "+((Button)view).getText()+" ");//取代上次输入的运算符
                }
                break;

            case R.id.buttonDel:
                if(clear_flag){
                    clear_flag = false;
                    str="";
                    inputNumber.setText("");
                }else if(str!=null&&!str.equals("")){
                    if(flag_num == true) {                                      //数据输入未结束
                        if(numLength>0) {
                            if (str.endsWith(".")) {
                                flag_point = false;
                            }
                            numLength--;
                            inputNumber.setText(str.substring(0, str.length() - 1));
                            if(numLength==0){
                                flag_num = false;
                                flag_0 = false;
                                flag_operator = true;//
                            }
                        }
                    }else{                                                      //已输入运算符
                        inputNumber.setText(str.substring(0, str.length() - 3));
                        flag_num = true;                                        //返回上一个状态，可以输入数据
                        flag_point = (boolean)stk_point.pop();                  //小数点返回上一个记录
                        flag_operator = false;
                        numLength = (int)stk_Length.pop();
                    }
                }
                break;

            case R.id.buttonC:
                flag_num = false;
                //flag_0 = false;
                flag_operator = false;
                flag_point = false;
                while(!stk_Length.isEmpty()){
                    stk_Length.pop();
                }
                numLength = 0;
                while(!stk_point.empty()){                                      //清除小数点所有记录
                    stk_point.pop();
                }
                clear_flag = false;
                str="";
                inputNumber.setText("");
                break;

            case R.id.buttonEql:
                if(flag_num == true) {                                          //若以运算符结尾，不予计算
                    flag_num = false;
                    flag_operator = false;
                    flag_point = false;  //
                    while(!stk_Length.isEmpty()){
                        stk_Length.pop();
                    }
                    numLength = 0;
                    while (!stk_point.empty()) {                                      //清除小数点所有记录
                        stk_point.pop();
                    }
                    getResult();
                }
                break;

            case R.id.buttonChange:

                inputNumber.setText(str + "->"+binary());
                break;
        }
    }
    private void getResult(){
        String exp = inputNumber.getText().toString();
        if(exp==null||exp.equals("")){
            return;
        }
        if(!exp.contains(" ")){
            return;
        }
        if(clear_flag){
            clear_flag = false;
            return;
        }
        clear_flag = true;
        double num1, num2, num3;
        double result = 0;
        while(!exp.isEmpty()) {                                //表达式分类进栈
            double d1;
            if (exp.contains(" ")) {                            //表达式包含操作符
                if(exp.startsWith(" ")){                        //表达式以运算符开头
                    stk_midnum.push(result);                    //result=0;push(0);
                    String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2);//运算符
                    stk_midop.push(op);
                    exp = exp.substring(exp.indexOf(" ") + 3, exp.length());
                }else {
                    String s1 = exp.substring(0, exp.indexOf(" "));//运算符前面的字符串
                    String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2);//运算符
                    String s2 = exp.substring(exp.indexOf(" ")+3);//运算符后面的字符串
                    //if(!s1.equals("")&&!s2.equals("")){
                    d1 = Double.parseDouble(s1);
                    stk_midnum.push(d1);
                    stk_midop.push(op);
                    exp = exp.substring(exp.indexOf(" ") + 3, exp.length());
                }
            } else {                                            //表达式不含运算符
                d1 = Double.parseDouble(exp);
                stk_midnum.push(d1);
                exp = "";
            }
        }
        //逆序，达到“先进先出”的效果
        while(!stk_midnum.isEmpty()) {
            stk_num.push(stk_midnum.pop());
        }
        stk_operator.push(null);//到 底 标志
        while(!stk_midop.isEmpty()) {
            stk_operator.push(stk_midop.pop());
        }
        String op1, op2;
        while(!stk_operator.isEmpty()) {
            if(stk_operator.peek()!=null){                  //至少还有一个运算符
                op1 = (String) stk_operator.pop();
                op2 = (String) stk_operator.pop();          //可能为null
                if(op2!=null){                              //至少2个运算符!op2.equals(null) null与""
                    if(op2.equals("+")||op2.equals("-")){   //第二个运算符为低优先级，先计算第一个运算符
                        num1 = (double) stk_num.pop();
                        num2 = (double) stk_num.pop();
                        result = operator(num1,num2,op1);
                        stk_num.push(result);
                        stk_operator.push(op2);
                    }else if(op1.equals("×")||op1.equals("÷")){//第二个运算符是高优先级，若第一运算符也为高优先级，先计算
                        num1 = (double) stk_num.pop();
                        num2 = (double) stk_num.pop();
                        result = operator(num1,num2,op1);
                        stk_num.push(result);
                        stk_operator.push(op2);
                    }else{                                      //第二高优先级，第一低优先级
                        num1 = (double) stk_num.pop();
                        num2 = (double) stk_num.pop();
                        num3 = (double) stk_num.pop();
                        result = operator(num2,num3,op2);
                        stk_num.push(result);
                        stk_num.push(num1);
                        stk_operator.push(op1);
                    }
                }else{                                                      //只剩一个运算符
                    num1 = (double) stk_num.pop();
                    num2 = (double) stk_num.pop();
                    result = operator(num1,num2,op1);
                    //inputNumber.setText(result+"");
                }
            }else{                                                          //无运算符
                stk_operator.pop();
            }

        }
        inputNumber.setText(result+"");                                        //显示结果
/*     inputNumber.setText("123");
        */

    }
    private double operator(double n1, double n2, String s) {
        if (s.equals("+")){
            return n1+n2;
        }else if(s.equals("-")){
            return n1-n2;
        }else if(s.equals("*")){
            return n1*n2;
        }else{//if(s.equals("÷"))
            if(n2!=0){
                return n1/n2;
            }else {
                Toast toast = Toast.makeText(this,"错误：除数为0",Toast.LENGTH_SHORT);
                toast.show();
                return 0;   //除数为0
            }
        }
    }

    private String binary(){
        String resultString="";
        //位数限制
        String input=inputNumber.getText().toString();
        double   num   =   Double.parseDouble(input);
        int preCount=16;
        int count=0;
        //取整数部分的二进制表示
        int intPart=(int)num;
        //保存副本
        int intPartSaved=intPart;

        String intString="";

        if(intPart==0){
            intString="0";
        }else{
            intString="";
            while(intPart!=0){
                int m=intPart%2;
                if(m==0){
                    intString+="0";
                }
                if(m==1){
                    intString+="1";
                }
                intPart/=2;
            }
            intString=new StringBuilder(intString).reverse().toString();
        }
        resultString+=intString;

        if(input.indexOf(".")!=-1){
            resultString+=".";
            //取小数部分的二进制表示
            int base=1;
            while(count!=0){
                base*=10;
                --count;
            }
            num*=base;

            int decPart=(int)(num-intPart);
            String decString="";

            while(decPart!=0 && preCount!=0){

                decPart*=2;
                //取进位
                int n=decPart/base;
                //取余
                decPart=decPart%base;

                if(n==1){
                    decString+="1";
                }
                if(n==0){
                    decString+="0";
                }
                --preCount;
            }

            resultString+=decString;
        }

        return resultString;
    }

}

