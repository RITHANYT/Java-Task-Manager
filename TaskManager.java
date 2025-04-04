import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class TaskManager {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        final String FILE_NAME = "tasks.dat";
        ArrayList<Task> tasks = loadTasks(FILE_NAME);


        while(true){
            System.out.println("TASK MANAGER");
            System.out.println("1.Add Task");
            System.out.println("2.Remove Task");
            System.out.println("3.View Task");
            System.out.println("4.Mark Task as Completed");
            System.out.println("5.Exit");
            System.out.println("Enter your choice");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    try {
                        System.out.println("Enter task description: ");
                        String task = sc.nextLine();
                        System.out.println("Set task priority(High / Medium / Low): ");
                        String priority = sc.nextLine().trim();
                        System.out.println("Enter due date (dd-mm-yyyy) or press Enter to skip: ");
                        String dueDateStr = sc.nextLine().trim();
                        Date dueDate = null;
                        if (!dueDateStr.isEmpty()) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            dueDate = sdf.parse(dueDateStr);
                        }
                        tasks.add(new Task(task, priority, dueDate));

                        System.out.println("Task Added Successfully! ✅");
                    } catch (Exception e){
                        System.out.println("Invalid date format! Please enter date as dd-MM-yyyy.");
                    }

                    break;

                case 2:
                    System.out.print("Enter task number to remove: ");
                    int removeIndex = sc.nextInt();
                    if(removeIndex >=0 && removeIndex<tasks.size()){
                        tasks.remove(removeIndex);

                        System.out.println("Task Removed Successfully!");
                    }
                    else{
                       System.out.println("Invalid Task Number");
                    }
                    break;

                case 3:
                    System.out.println("Your Tasks");
                    if(tasks.isEmpty()){
                        System.out.println("No Tasks available!");
                    }
                    else{
                        Collections.sort(tasks);
                        for (int i=0;i<tasks.size();i++){
                            System.out.println((i+1) + ". " + tasks.get(i));
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