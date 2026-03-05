package view;

import java.util.Scanner;

public class LoginView {

    private Scanner scanner = new Scanner(System.in);

    public String getUsername() {
        System.out.print("Enter Username: ");
        return scanner.nextLine();
    }

    public String getPassword() {
        System.out.print("Enter Password: ");
        return scanner.nextLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}