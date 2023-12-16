package com.example.demo;

import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class RSA
{
    Scanner scanner_in = new Scanner(System.in);
    char alphabet[] = { 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ш', 'ч', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я' };
    int number[] = {  1,   2,   3,   4,   5,   6,   7,   8,   9,   10,  11,  12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,  32,  33 };
    int keys[] = new int[3];
    boolean m = true;
    int choice = 0;
    int mod = 0;
    int e = 0; //public key {e,mod}
    int d = 0; //private key {mod,d}
    public void start() throws IOException {
        while (m)
        {
            System.out.print("\nОткрытый ключ: ");
            if (e == 0) System.out.println("не задан");
            else System.out.printf("%d,%d%n", e, mod);
            System.out.print("Закрытый ключ: ");
            if (e == 0) System.out.println("не задан");
            else System.out.printf("%d,%d%n", d, mod);
            System.out.print("\nВведите 1 для задания ключей\n");
            System.out.print("Введите 2 для чтения файла\n");
            System.out.print("Введите 3 для шифровки текста и его вывода\n");
            System.out.print("Введите 4 для вывода зашифрованного текста\n");
            System.out.print("Введите 5 для расшифровки текста и его вывода\n");
            System.out.print("Введите любое другое число для завершения работы\nВведённое число: ");
            choice = scanner_in.nextInt();
            System.out.print("\n\n\n\n\n\n");
            switch (choice)
            {
                case 1:
                    createKeys(keys);
                    mod = keys[0];
                    e = keys[1];
                    d = keys[2];
                    break;
                case 2:
                    BufferedReader file = new BufferedReader(new FileReader("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file1.txt"));
                    readFile(file);
                    file.close();
                    break;
                case 3:
                    file = new BufferedReader(new FileReader("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file1.txt"));
                    cryptFile(file,alphabet,number,mod,e);
                    file.close();
                    BufferedReader new_file = new BufferedReader(new FileReader("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file2.txt"));
                    readFile(new_file);
                    new_file.close();
                    break;
                case 4:
                    file = new BufferedReader(new FileReader("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file2.txt"));
                    readFile(file);
                    file.close();
                    break;
                case 5:
                    file = new BufferedReader(new FileReader("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file2.txt"));
                    decryptFile(file,alphabet,number,mod,d);
                    file.close();
                    new_file = new BufferedReader(new FileReader("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file3.txt"));
                    readFile(new_file);
                    new_file.close();
                    break;
            }
        }
    }
    private void createKeys(int keys[])
    {
        boolean cycle = true;
        int prime_number_1 = 0, prime_number_2 = 0, f, e = 0, d = 1;
        System.out.print("Создаются ключи, следуйте таким правилам:\n" +
                "1. Вводятся 2 простых числа, причём их произведение должно быть больше 33;\n" +
                "2. Вы выбираете открытую экспоненту;\n");
        while (cycle)
        {
            System.out.print("Число 1: ");
            prime_number_1 = scanner_in.nextInt();
            System.out.print("Число 2: ");
            prime_number_2 = scanner_in.nextInt();
            if (!isPrime(prime_number_1) || !isPrime(prime_number_2))
            {
                System.out.print("\nОдно из чисел не простое");
                pause();
            }
            else if (prime_number_1 * prime_number_2 <= 33)
            {
                System.out.print("\nПроизведение меньше или равно 33 (Всего 33 буквы в алфавите)");
                pause();
            }
            else cycle = false;
            System.out.print("\n\n\n\n\n\n");
        }
        cycle = true;
        f = (prime_number_1 - 1) * (prime_number_2 - 1);

        int quantity = 0;
        int[] e_array = new int[3];
        for(int i = 2; i < f; i++)
        {
            if(isPrime(i))
            {
                int n = i;
                int m = f;
                while (n != m)
                {
                    if (n > m) n -= m;
                    else m -= n;
                }
                if (n == 1)
                {
                    e_array[quantity] = i;
                    quantity++;
                }
                if (quantity == 3) break;
            }
        }

        while (cycle)
        {
            System.out.print("\nВыберите одну из 3-х предложенных открытых экспонент: \n");
            System.out.print("1. " + e_array[0]);
            System.out.print("\n2. " + e_array[1]);
            System.out.print("\n3. " + e_array[2]);
            System.out.print("\nВыбор: ");
            int t;
            t = scanner_in.nextInt();
            switch (t)
            {
                case 1: e = e_array[0]; cycle = false; break;
                case 2: e = e_array[1]; cycle = false; break;
                case 3: e = e_array[2]; cycle = false; break;
                default: break;
            }
            System.out.print("\n\n\n\n\n\n");
        }

        while ((d * e) % f != 1) d++;
        keys[0] = prime_number_1 * prime_number_2; //mod
        keys[1] = e; //e
        keys[2] = d; //d
    }
    private boolean isPrime(int number)
    {
        if (number > 1)
        {
            for(int i = 2;i<number;i++)
            {
                if (number % i == 0) return false;
            }
            return true;
        }
        else return false;
    }
    private void pause()
    {
        System.out.println("\nДля продолжения нажмите Enter...");
        Scanner enter = new Scanner(System.in);
        enter.nextLine();
    }
    private void readFile(BufferedReader file) throws IOException
    {
        String line;
        while((line = file.readLine()) != null)
        {
            char str[] = line.toCharArray();
            for (char character : str)
            {
                System.out.print(character);
            }
            System.out.println();
        }
    }
    private void cryptFile(BufferedReader file1, char alphabet[], int number[], int mod, int e) throws IOException
    {
        String line;
        PrintWriter writer = new PrintWriter("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file2.txt");
        boolean isletter = false;
        int number_letter = 0;
        while((line = file1.readLine()) != null) //Читает каждую линию
        {
            char str[] = line.toCharArray(); //Хранит 1 линию
            for (int i = 0; i < str.length; i++) //Посимвольная обработка
            {
                isletter = false;
                for (int j = 0; j < alphabet.length; j++)
                {
                    if (alphabet[j] == str[i])
                    {
                        number_letter = number[j];
                        isletter = true;
                        break;
                    }
                }
                if (isletter)
                {
                    double crypt_number = Math.pow(number_letter,e);
                    crypt_number = crypt_number % mod;
                    writer.printf("%d ", (int) crypt_number);
                }
                else
                {
                    writer.printf("%c ", str[i]);
                }
            }
            writer.printf("%c", '\n');
        }
        writer.close();
    }
    private void decryptFile(BufferedReader file1, char alphabet[], int number[], int mod, int d) throws IOException //Читает 2 линию??
    {
        String line;
        int character;
        int count_digit = 0;
        boolean u = false;
        int iread[] = new int[10];
        char letter;
        PrintWriter writer = new PrintWriter("F:/INTELIJ THREE COURSE/javaProgs/LAB4INFO/demo/src/main/resources/file3.txt");
        while ((line = file1.readLine()) != null)
        {
            char str[] = line.toCharArray();
            for (int jjj = 0; jjj < str.length; jjj++) //Посимвольная обработка
            {
                character = str[jjj];
                char c = (char) character;
                if (c == '\n') continue;
                if (u == true)
                {
                    u = false;
                    continue;
                }
                if (Character.isDigit(c))
                {
                    count_digit++;
                    iread[count_digit - 1] = c - '0';
                }
                if(!Character.isDigit(c) && count_digit > 0)
                {
                    int sum = 0;
                    int mult = 1;
                    for(int j = count_digit - 1; j >= 0; j--)
                    {
                        sum += iread[j] * mult;
                        mult *= 10;
                    }
                    count_digit = 0;
                    BigInteger sum_big = BigInteger.valueOf(sum);
                    BigInteger power = sum_big.pow(d);
                    BigInteger remainder_sum = power.remainder(BigInteger.valueOf(mod));
                    int result = remainder_sum.intValue();
                    for (int i = 0; i < 33; i++)
                    {
                        if (number[i] == result)
                        {
                            letter = alphabet[i];
                            writer.printf("%c",letter);
                            break;
                        }
                    }
                    continue;
                }
                if(!Character.isDigit(c))
                {
                    writer.printf("%c",c);
                    u = true;
                    continue;
                }
            }
            writer.printf("%c", '\n');
        }
        writer.close();
    }
}
