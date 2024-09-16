import java.io.*;
import java.util.Scanner;

public class Atm {
    /*
     * String handler
    * */
    private static double baseParser(String line) {
        return (Double.parseDouble(
                line.split(": ")[1]
        ));
    }

    /*
     * Menu method for working with the user
    * */
    public static void AtmMenu() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        while(true) {
            if(!user.getIsRegistered()) {
                int pin;
                System.out.println("=== User registration ===");
                System.out.print("[?] Enter a name: ");
                String name = scanner.nextLine();
                do {
                    System.out.print("[?] Think of a pin number: ");
                    pin = scanner.nextInt();
                } while ((""+pin).length() != 4);

                user.setName(name);
                user.setPin(pin);
                Atm.writeBaseRegistration(user);
            }

            System.out.print("[?] " + user.getName() + ", Enter the desired operation:\n" +
                    "\t1. Withdrawal from account\n" +
                    "\t2. Account replenishment\n" +
                    "\t3. account balance\n" +
                    "\t0. Exit\n" +
                    "\t> ");
            int numberOfOperation = scanner.nextInt();

            if(numberOfOperation == 0) {
                System.exit(0);
            }

            int enteredPin;
            do {
                System.out.print("[?] Enter the pin code: ");
                enteredPin = scanner.nextInt();
            } while (("" + enteredPin).length() != 4);

            Atm.readBase(user, enteredPin, numberOfOperation);
        }
    }

    /*
     * Methods for working with accounts
     * (change of balance in the base, withdrawal and replenishment of the account)
    * */
    private static void changingBalanceInBase(User user) {
        try {
            FileReader fr = new FileReader("user_base.csv");
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter("user_base.csv");
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(user.userWriteInfo());     // I failed to implement changing a certain line in the file, so I use these crutches

            bw.close();
            br.close();
        } catch(IOException e) {
            System.out.println("[!] Error when writing to database...");
            e.printStackTrace();
        }
    }

    private static void withdrawalOfMoney(User user, double interimBalance) {   // withdrawal
        user.setBalance(user.getBalance() - interimBalance);
        Atm.changingBalanceInBase(user);
        System.out.println("[+] Successful! Account balance: " + user.getBalance());
    }

    private static void replenishmentOfBalance(User user, double interimBalance) {  // replenishment of account
        user.setBalance(user.getBalance() + interimBalance);
        Atm.changingBalanceInBase(user);
        System.out.println("[+] Successful! Current balance: " + user.getBalance());
    }

    /*
     * Basic methods for working with the base
    * */
    public static void readBase(User user, int enteredPin, int numberOperation) {
        try {
            FileReader fr = new FileReader("user_base.csv");
            BufferedReader br = new BufferedReader(fr);

            double interimBalance;  // the variable is needed to perform withdrawal/replenishment operations (“intermediate balance”)
            String line;
            int currentPin = 0;
            Scanner scanner = new Scanner(System.in);

            while ((line = br.readLine()) != null) {
                if(line.contains("Pin")) {
                    currentPin = (int) Atm.baseParser(line);
                }
            }
            
            if(enteredPin != currentPin) {
                System.out.println("[-] Incorrect pin code");
            } else {

                while ((line = br.readLine()) != null) {
                    if (line.contains("Balance")) {
                        user.setBalance(baseParser(line));
                    }
                }

                switch (numberOperation) {
                    case 1 -> {
                        System.out.print("[?] Enter the amount to be withdrawn: ");
                        interimBalance = scanner.nextDouble();

                        if (user.getBalance() < interimBalance) {
                            System.out.println("[-] Error: insufficient funds on account");
                        } else {
                            Atm.withdrawalOfMoney(user, interimBalance);
                        }
                    }
                    case 2 -> {
                        System.out.print("[?] Enter the amount to be replenished: ");
                        interimBalance = scanner.nextDouble();

                        Atm.replenishmentOfBalance(user, interimBalance);
                    }
                    case 3 -> {
                        System.out.println("[i] Account Balance: " + user.getBalance());
                    }
                    default -> {
                        System.out.println("[-] Error: operation not found");
                    }
                }
                br.close();
            }
        } catch(IOException e) {
            System.out.println("[!] Error while reading the base...");
            e.printStackTrace();
        }
    }

    public static void writeBaseRegistration(User user) {
        try {
            FileWriter fw = new FileWriter("user_base.csv");
            BufferedWriter bw = new BufferedWriter(fw);

            User.setIsRegistered(true);

            bw.write(user.userWriteInfo());
            bw.close();

        } catch(IOException e) {
            System.out.println("[!] Error when writing to database...");
            e.printStackTrace();
        }
    }
}
