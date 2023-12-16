package com.example.demo;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException
    {
        System.out.println("Программа запущена\n\n");
        RSA rsa = new RSA();
        rsa.start();
    }
}