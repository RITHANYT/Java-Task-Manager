
import java.sql.Connection;
import java.sql.DriverManager;
public class Main {

    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        // Press Shift+F10 or click the green arrow button in the gutter to run the code.
        for (int i = 1; i <= 5; i++) {

            // Press Shift+F9 to start debugging your code. We have set one breakpoint
            // for you, but you can always add more by pressing Ctrl+F8.
            System.out.println("i = " + i);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ JDBC Driver loaded!");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/task_manager_db",
                    "root",
                    "RITHANYAT"
            );
            System.out.println("✅ Connection established!");

            conn.close();
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
}