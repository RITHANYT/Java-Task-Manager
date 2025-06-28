import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class TaskManager {
    public static void saveTaskToDB(Task task) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO tasks (description, priority, due_date, is_completed) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, task.getDescription());
            ps.setString(2, task.getPriority());
            if (task.getDueDate() != null) {
                ps.setDate(3, new java.sql.Date(task.getDueDate().getTime()));
            } else {
                ps.setDate(3, null);
            }
            ps.setBoolean(4, task.isCompleted());
            ps.executeUpdate();
            conn.close();
            System.out.println("✅ Task saved to MySQL DB!");
        } catch (Exception e) {
            System.out.println("❌ DB Save Error: " + e.getMessage());
        }
    }

    public static void displayTasksFromDB() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tasks");
            System.out.println("===== Tasks in MySQL =====");
            while (rs.next()) {
                int id = rs.getInt("id");
                String desc = rs.getString("description");
                String priority = rs.getString("priority");
                java.sql.Date due = rs.getDate("due_date");
                boolean completed = rs.getBoolean("is_completed");

                System.out.printf("ID: %d | %s | %s | Due: %s | %s\n",
                        id, desc, priority,
                        due != null ? due.toString() : "No Due Date",
                        completed ? "Completed" : "Pending");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ DB Load Error: " + e.getMessage());
        }
    }
    public static void deleteTaskFromDB(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM tasks WHERE id = ?";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Task deleted from MySQL DB!");
                } else {
                    System.out.println("⚠️ No task found with that ID.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ DB ERROR: " + e.getMessage());
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        final String FILE_NAME = "tasks.dat";
        ArrayList<Task> tasks = loadTasks(FILE_NAME);

        System.out.println("Upcoming tasks: ");
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 3);
        Date threeDaysLater = calendar.getTime();

        boolean found = false;
        for(int i=0;i<tasks.size();i++){
            Task t = tasks.get(i);
            if(!t.isCompleted() && t.getDueDate()!=null) {
                Date due = t.getDueDate();

                if (!due.before(today) && !due.after(threeDaysLater)) {
                    System.out.println((i + 1) + ". " + t);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No upcoming tasks! 🎉");
        }

        while(true){
            System.out.println("TASK MANAGER");
            System.out.println("1.Add Task");
            System.out.println("2.Remove Task");
            System.out.println("3.View Task");
            System.out.println("4.Mark Task as Completed");
            System.out.println("5.Exit");
            System.out.println("6.Edit Task");
            System.out.println("7. Export Tasks to CSV");
            System.out.println("8. Search Tasks");
            System.out.println("Enter your choice");


            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    try {
                        System.out.println("Enter task description: ");
                        String task = sc.nextLine();
                        System.out.println("Set task priority (High / Medium / Low) or press Enter to auto-set: ");
                        String priority = sc.nextLine().trim();
                        if (priority.isEmpty()) {
                            String descLower = task.toLowerCase();
                            if (descLower.contains("urgent") || descLower.contains("important") || descLower.contains("asap")) {
                                priority = "High";
                            } else if (descLower.contains("moderate") || descLower.contains("soon")) {
                                priority = "Medium";
                            } else {
                                priority = "Low";
                            }
                            System.out.println("🔄 Auto-set priority as: " + priority);
                        }
                        System.out.println("Enter due date (dd-mm-yyyy) or press Enter to skip: ");
                        String dueDateStr = sc.nextLine().trim();
                        Date dueDate = null;
                        if (!dueDateStr.isEmpty()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            dueDate = sdf.parse(dueDateStr);
                        }
                        Task newTask = new Task(task, priority, dueDate);
                        tasks.add(newTask);
                        saveTaskToDB(newTask);

                        System.out.println("Task Added Successfully! ✅");
                    } catch (Exception e){
                        System.out.println("Invalid date format! Please enter date as dd-MM-yyyy.");
                    }

                    break;

                case 2:
                    System.out.println("===== Remove Task =====");
                    System.out.println("1. Remove task from local list");
                    System.out.println("2. Remove task from MySQL DB");
                    System.out.print("Choose option: ");
                    int removeOption = sc.nextInt();
                    sc.nextLine();

                    if (removeOption == 1) {
                        // Remove from local ArrayList
                        System.out.print("Enter task number to remove (check index shown when you view tasks): ");
                        int removeIndex = sc.nextInt() - 1; // remember: user sees 1-based, ArrayList is 0-based

                        if (removeIndex >= 0 && removeIndex < tasks.size()) {
                            tasks.remove(removeIndex);
                            System.out.println("✅ Task removed from local list!");
                        } else {
                            System.out.println("⚠️ Invalid task number!");
                        }

                    } else if (removeOption == 2) {
                        // Remove from DB
                        System.out.print("Enter task ID to remove (check ID from MySQL task list): ");
                        int removeId = sc.nextInt();
                        sc.nextLine();

                        deleteTaskFromDB(removeId);
                    } else {
                        System.out.println("⚠️ Invalid option.");
                    }

                    break;


                case 3:
                    System.out.println("View Options");
                    System.out.println("1. All Tasks");
                    System.out.println("2. Pending Tasks");
                    System.out.println("3. Completed Tasks");
                    int viewOption = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Your Tasks");
                    displayTasksFromDB();
                    if(tasks.isEmpty()){
                        System.out.println("No Tasks available!");
                    }
                    else{
                        Collections.sort(tasks);
                        for (int i=0;i<tasks.size();i++){
                            Task t = tasks.get(i);
                            boolean show = switch (viewOption){
                                case 1 -> true;
                                case 2 -> !t.isCompleted();
                                case 3 -> t.isCompleted();
                                default -> false;
                            };
                            if(show){
                                System.out.println((i+1)+". "+t);
                            }
                        }
                    }
                    break;

                case 4:
                    System.out.println("Enter task number to mark as completed");
                    int completeIndex = sc.nextInt();
                    if(completeIndex>=0 && completeIndex<tasks.size()){
                        tasks.get(completeIndex).markAsCompleted();
                        System.out.println("Task Marked as Completed! ✔");
                    }
                    else{
                        System.out.println("Invalid Task Number!");
                    }
                    break;
                case 5:
                    saveTasks(tasks,FILE_NAME);
                    System.out.println("Exiting task Manager.....");
                    sc.close();
                    return;
                case 6:
                    System.out.println("Enter the task number to edit: ");
                    int editIndex = sc.nextInt()-1;
                    sc.nextLine();
                    if(editIndex>=0 && editIndex<tasks.size()){
                        Task taskToEdit = tasks.get(editIndex);

                        System.out.println("What do you want to edit?");
                        System.out.println("1. Description");
                        System.out.println("2. Priority");
                        System.out.println("3. Due Date");
                        System.out.print("Enter your choice: ");
                        int editChoice = sc.nextInt();
                        sc.nextLine();

                        switch (editChoice){
                            case 1:
                                System.out.println("Enter new description: ");
                                String newDesc = sc.nextLine();
                                taskToEdit.setDescription(newDesc);
                                break;
                            case 2:
                                System.out.println("Enter new priority: ");
                                String newPriority = sc.nextLine();
                                taskToEdit.setPriority(newPriority);
                                break;
                            case 3:
                                System.out.println("Enter the due date(dd-MM-yyyy)");
                                String newDue = sc.nextLine().trim();
                                if (!newDue.isEmpty()){
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                        Date newDate = sdf.parse(newDue);
                                        taskToEdit.setDueDate(newDate);
                                    }catch (Exception e){
                                        System.out.println("Invalid date format");
                                    }
                                    }else {
                                    taskToEdit.setDueDate(null);
                                }
                                break;
                            default:
                                System.out.println("Invalid choice!");
                        }
                        System.out.println("Task updated successfully!");
                        }else {
                        System.out.println("Invalid Task number!");
                    }
                    break;
                case 7:
                    try (PrintWriter writer = new PrintWriter(new File("tasks.csv"))) {
                        writer.println("Description,Priority,Due Date,Status");
                        for (Task t : tasks) {
                            String status = t.isCompleted() ? "Completed" : "Pending";
                            String due = t.getDueDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(t.getDueDate()) : "No Due Date";
                            writer.printf("\"%s\",%s,%s,%s\n", t.getDescription(), t.getPriority(), due, status);
                        }
                        System.out.println("✅ Tasks exported to tasks.csv successfully!");
                    } catch (Exception e) {
                        System.out.println("❌ Error exporting tasks: " + e.getMessage());
                    }
                    break;
                case 8:
                    System.out.print("Enter keyword to search in tasks: ");
                    String keyword = sc.nextLine().toLowerCase();
                    boolean matchFound = false;
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        if (t.getDescription().toLowerCase().contains(keyword)) {
                            System.out.println((i + 1) + ". " + t);
                            matchFound = true;
                        }
                    }
                    if (!matchFound) {
                        System.out.println("🔍 No matching tasks found!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice try again..");
            }
        }
    }
    public static void saveTasks(ArrayList<Task> tasks, String fileName){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(tasks);
        }catch (IOException e){
            System.out.println("Error saving tasks: "+e.getMessage());
        }
    }
    public static ArrayList<Task> loadTasks(String fileName){
        try(ObjectInputStream ois = new ObjectInputStream((new FileInputStream(fileName)))) {
            return (ArrayList<Task>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            return new ArrayList<>();
        }
    }
}