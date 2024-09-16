public class User {
    private String name;
    private int pin;
    private double balance;
    private static boolean isRegistered;


    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setPin(int pin) {
        this.pin = pin;
    }
    public void setName(String name) {
        this.name = name;
    }
    public static void setIsRegistered(boolean isRegistered) {
        User.isRegistered = isRegistered;
    }


    public String getName() {
        return this.name;
    }
    public double getBalance() {
        return this.balance;
    }
    public boolean getIsRegistered() {
        return User.isRegistered;
    }


    public String userWriteInfo() {
        return "Name: " + this.name + "\nPin: " + this.pin + "\nBalance: " + this.balance;
    }
}
