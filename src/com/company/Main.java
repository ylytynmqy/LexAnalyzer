package com.company;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        boolean flag=true;
        while(true){
            if(flag){
                flag=false;
            }
            else{
                System.out.println();
            }
            System.out.println("please Input number:");
            System.out.println("1   Generating Lexer");
            System.out.println("2   Lexical Analyze");
            System.out.println("3   Quit");
            String command=scanner.nextLine();
            if(command.equals("1")){
                //generate Lexer
                Generator generator =new Generator();
            }
            else if(command.equals("2")){
                //Analyze
                    Lexer lexer=new Lexer();
            }
            else if(command.equals("3")){
                break;
            }
        }
    }
}