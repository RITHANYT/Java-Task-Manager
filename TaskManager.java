import java.util.*;
public class TaskManager {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ArrayList<String> tasks = new ArrayList<>();
        HashMap<Integer, Boolean> taskCompletion = new HashMap<>();

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
                    System.out.println("Enter task description: ");
                    String task = sc.nextLine();
                    tasks.add(task);
                    taskCompletion.put(tasks.size()-1, false);
                    System.out.println("Task Added Successfully! ✅");
                    break;

                case 2:
                    System.out.print("Enter task number to remove: ");
                    int removeIndex = sc.nextInt();
                    if(removeIndex >=0 && removeIndex<tasks.size()){
                        tasks.remove(removeIndex);
                        taskCompletion.remove(removeIndex);
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
                        for (int i=0;i<tasks.size();i++){
                            String status = taskCompletion.get(i)? "[Completed]": "[Pending]";
                            System.out.println(i + ". " + tasks.get(i) + " " + status);
                        }
                    }
                    break;

                case 4:
                    System.out.println("Enter task number to mark as completed");
                    int completeIndex = sc.nextInt();
                    if(completeIndex>=0 && completeIndex<tasks.size()){
                        taskCompletion.put(completeIndex,true);
                        System.out.println("Task Marked as Completed! ✔");
                    }
                    else{
                        System.out.println("Invalid Task Number!");
                    }
                    break;
                case 5:
                    System.out.println("Exiting task Manager.....");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice try again..");
            }
        }
    }
}