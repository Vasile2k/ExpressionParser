package net.vasile2k.expression;

import java.util.*;

/**
 * Created by Vasile2k on 27.02.2019 22:50.
 */
public class Expression {

    private String expression;

    public Expression(String expression){
        this.expression = expression.replace(" ", "");
    }

    public ArrayList<String> tokenize(){
        ArrayList<String> tokens = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(this.expression, "+-*/()", true);
        while (stringTokenizer.hasMoreTokens()){
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens;
    }

    public ArrayList<String> toPostfixedForm(){
        ArrayList<String> infixed = this.tokenize();
        return this.toPostfixedForm(infixed);
    }

    public ArrayList<String> toPostfixedForm(ArrayList<String> infixed){
        ArrayList<String> postfixed = new ArrayList<String>();

        Stack<String> stack = new Stack<String>();

        for(String token : infixed){
            switch (token){
                case "+":
                case "-":
                case "*":
                case "/":
                    while(!stack.empty() && !stack.peek().equals("(") && (getPriorityForOperator(stack.peek()) >= getPriorityForOperator(token))){
                        postfixed.add(stack.pop());
                    }
                    stack.push(token);
                    break;
                case "(":
                    stack.push(token);
                    break;
                case ")":
                    while (!stack.empty() && !stack.peek().equals("(")){
                        postfixed.add(stack.pop());
                    }
                    // Last parenthesis
                    stack.pop();
                    break;
                default:
                    // Should be a number here...
                    postfixed.add(token);
            }
        }

        while (!stack.empty()){
            postfixed.add(stack.pop());
        }

        return postfixed;
    }

    public double parse() throws NumberFormatException{
        return this.parse(this.toPostfixedForm());
    }

    public strictfp double parse(ArrayList<String> postfixedForm) throws NumberFormatException, EmptyStackException{
        Stack<Double> stack = new Stack<Double>();


        for (String token : postfixedForm){
            double a;
            double b;
            switch (token){
                case "+":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b + a);
                    break;
                case "-":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b - a);
                    break;
                case "*":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b * a);
                    break;
                case "/":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b / a);
                    break;
                default:
                    stack.push(Double.parseDouble(token));
            }
        }

        return stack.pop();
    }

    private static int getPriorityForOperator(String operator){
        switch (operator){
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
        }
        return -1;
    }

//    public static void main(String[] args){
//        Expression e = new Expression("20 + 3.5*(11.1-5.5) + .6*(3.1+2-(11-9))");
//        ArrayList<String> tokens = e.tokenize();
//        System.out.println(tokens);
//        tokens = e.toPostfixedForm();
//        System.out.println(tokens);
//        double result = e.parse();
//        System.out.println(result);
//    }

}
