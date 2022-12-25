import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {


        Scanner menuOption = new Scanner(System.in);
        while (true){
            System.out.println("\n-----------------------------------------------------------------------------");
            System.out.println("Homepage");
            System.out.println("Enter 0/1/2/3 to access.");
            System.out.println("0. Quit");
            System.out.println("1. Student Login");
            System.out.println("2. Student Registration");
            System.out.println("3. Faculty Login");
            System.out.println("\nEnter your selected number: ");
            System.out.println("-----------------------------------------------------------------------------");

            int selected = menuOption.nextInt();

            if (selected == 1){
                System.out.println("\nYou are being directed to student login.");
                LoginHandle studentLogin = new LoginHandle();
                studentLogin.login(true, false);

            } else if (selected == 2) {
                System.out.println("\nYou are being directed to student registration.");
                StudentRegistration studentRegistration = new StudentRegistration();
                studentRegistration.registration();

            } else if (selected == 3) {
                System.out.println("\nYou are being directed to faculty login.");
                LoginHandle facultyLogin = new LoginHandle();
                facultyLogin.login(false, true);

            } else if (selected == 0) {
                System.out.println("\nQuiting.");
                break;
            }
            else {
                System.out.println("Wrong option selected.");
            }
        }
    }
}