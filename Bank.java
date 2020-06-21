package banking;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class Bank {
    private final String cardNumber;
    private final String pinCode;
    private int balance;
    private final Random random = new Random();

    Bank(MyDatabase myDatabase, String filename) {
        pinCode = String.format("%04d", random.nextInt(10000));
        cardNumber = luhnalg();
        balance = 0;
        MyDatabase.insert(1, cardNumber, pinCode, 0);
        System.out.println("Your card have been created");
        print();
    }

    Bank(String cardNumber, String pinCode, int balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
    }

    public void print() {
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pinCode);
        System.out.println();
    }

    public int getBalance() {
        return balance;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    String luhnalg() {
        String number = String.format("400000%d", BigInteger.probablePrime(32, random));
        number = number.substring(0, 15);
        int[] x = Arrays.stream(number.split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < x.length; i += 2) {
            x[i] *= 2;
            if (x[i] > 9) {
                x[i] -= 9;
            }
        }
        int sum = 0;
        for (int z : x) {
            sum += z;
        }
        sum = sum % 10 == 0 ? 0 : 10 - (sum % 10);
        return number + sum;
    }

    public void deposit(int income) {
        this.balance = balance + income;
        System.out.println("Income was added!\n");
    }

    public boolean checknum(String cardNumber) {
        int[] a = Arrays.stream(cardNumber.split("")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < a.length - 1; i += 2) {
            a[i] *= 2;
        }
        int sum = 0;
        for (int x : a) {
            sum += x;
        }
        if (sum % 2 == 0) {
            Bank bank=MyDatabase.result(cardNumber);
            if (checkBank(bank)) {
                System.out.println("Enter how much money you want to transfer:");
                return true;

            } else {
                System.out.println("Such a card does not exist.\n");
                return false;
            }
        } else {
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
            return false;
        }
    }

    public void transfer(String cardNumber, int amout) {
        balance -= amout;
        Bank rs = MyDatabase.result(cardNumber);
        MyDatabase.update(cardNumber, rs.balance + amout);
        System.out.println("Success!\n");
    }

    static Boolean checkBank(Bank bank){
        return bank != null;

    }
    public void finalize() {
        MyDatabase.update(cardNumber, balance);

    }

}