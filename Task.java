import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class Task implements Comparable<Task>,Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private boolean isCompleted;
    private String priority;
    private Date dueDate;
    public boolean isCompleted(){
        return isCompleted;
    }
   public String getDescription(){
       return description;
   }
   public void setDescription(String description){
       this.description = description;
   }
   public String getPriority(){
       return priority;
   }
   public void setPriority(String priority){
       this.priority = priority;
   }
   public Date getDueDate(){
       return dueDate;
   }
   public void setDueDate(Date dueDate){
       this.dueDate = dueDate;
   }
   public String getFormattedDueDate(){
       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       return (dueDate!=null)? sdf.format(dueDate):"No due Date";
   }
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

    @Override
    public int compareTo(Task other){
        return Integer.compare(other.getPriorityValue(),this.getPriorityValue());
    }
    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String status = isCompleted?"Completed":"Pending";
        String prioritystr = "Priority: "+priority;
        String dueDateStr ="";
        String timetag="";

        if(dueDate!=null){
            dueDateStr =" | Due: "+sdf.format(dueDate);
            Date today = new Date();

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(today);
            c2.setTime(dueDate);
            c1.set(Calendar.HOUR_OF_DAY,0);
            c1.set(Calendar.MINUTE,0);
            c1.set(Calendar.SECOND,0);
            c1.set(Calendar.MILLISECOND,0);
            c2.set(Calendar.HOUR_OF_DAY,0);
            c2.set(Calendar.MINUTE,0);
            c2.set(Calendar.SECOND,0);
            c2.set(Calendar.MILLISECOND,0);

            if(c2.before(c1)){
                timetag = "Overdue";
            } else if (c2.equals(c1)) {
                timetag = "Due Date";
            }
            else {
                timetag = "Upcoming";
            }

        }else {
            timetag = "No Due Date";
        }
        return  status+" "+description+" " +prioritystr +" "+dueDateStr+" "+timetag;
        }
}
