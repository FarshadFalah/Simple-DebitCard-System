package banking;

import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String filename = "bankdb.s3db";
        if (args.length > 0) {
            if (args[0].matches("-fileName")) {
                filename = args[1];
            }
        }
        MyDatabase myDatabase = MyDatabase.getInstance();
        MyDatabase.createTable(filename);

        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            switch (scanner.nextLine()) {
                case "1":
                    System.out.println();
                    new Bank(myDatabase, filename);
                    break;
                case "2":
                    System.out.println();
                    login();
                    break;

                case "0":
                    return;
            }

        }
    }

    static void login() {
        System.out.println("Enter your card number:");
        String cardID = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pinnum = scanner.nextLine().trim();
        System.out.println();
        Bank bank = MyDatabase.result(cardID);
        if (Bank.checkBank(bank)&& bank.getPinCode().equals(pinnum)) {

            System.out.println("You have successfully logged in!\n");
            while (logedIn(bank)) ;
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }


    static boolean logedIn(Bank bank) {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        switch (scanner.nextLine()) {
            case "1":
                System.out.println(bank.getBalance());
                System.out.println();
                break;
            case "2":
                System.out.println("Enter income:");
                bank.deposit(scanner.nextInt());
                scanner.nextLine();
                break;
            case "3":
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                String s = scanner.nextLine();
                if (bank.checknum(s)) {
                    System.out.println("Enter how much money you want to transfer:");
                    int ba = scanner.nextInt();
                    scanner.nextLine();
                    if (bank.getBalance() >= ba) {
                        bank.transfer(s, ba);
                    } else {
                        System.out.println("Not enough money!\n");
                    }
                }
                break;

            case "4":
                MyDatabase.delete(bank.getCardNumber());
                System.out.println("The account has been closed!\n");
                return false;
            case "5":
                System.out.println("You have successfully logged out!\n");
                return false;
            case "0":
                System.out.println();
                System.out.println("Bye!");
                System.exit(0);
        }
        System.out.println();
        return true;
    }
}

