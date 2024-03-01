
    import java.util.Scanner;

    public class Authenticator {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter username:");
            String username = scanner.nextLine();

            System.out.println("Enter password:");
            String password = scanner.nextLine();


            if (authenticate(username, password)) {
                System.out.println("Authentication successful!");
                Main.main(new String[0]);
            } else {
                System.out.println("Authentication failed. Exiting...");
            }

            scanner.close();
        }

        private static boolean authenticate(String username, String password) {
            return username.equals("admin") && password.equals("password");
        }
    }

