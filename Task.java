import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Task implements Comparable<Task>,Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private boolean isCompleted;
    private String priority;
    private Date dueDate;
    public Task(String description, String priority, Date dueDate){
        this.description=description;
        this.isCompleted = false;
        this.priority = priority;
        this.dueDate = dueDate;
    }
    public void markAsCompleted(){
        this.isCompleted=true;
    }
    private int getPriorityValue(){
        return switch (priority.toLowerCase()){
            case "high"->3;
            case "medium"->2;
            case "low"->1;
            default -> 0;
        };
    }
    private String getFormattedDueDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return (dueDate!=null)? sdf.format(dueDate):"No Due Date";
    }
    @Override
    public int compareTo(Task other){
        return Integer.compare(other.getPriorityValue(),this.getPriorityValue());
    }
    @Override
    public String toString(){
        String status = isCompleted ? "[Completed]":"[Pending]";
        return description + " (" + priority + ", Due: " + getFormattedDueDate() + ") " + status;
    }
}
