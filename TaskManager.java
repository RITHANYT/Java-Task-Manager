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
            System.out.println("6.Edit Task");
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
                    System.out.println("View Options");
                    System.out.println("1. All Tasks");
                    System.out.println("2. Pending Tasks");
                    System.out.println("3. Completed Tasks");
                    int viewOption = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Your Tasks");
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