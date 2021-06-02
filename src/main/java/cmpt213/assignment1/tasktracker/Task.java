package cmpt213.assignment1.tasktracker;

import java.util.GregorianCalendar;

public class Task implements Comparable<Task> {
    private String name;
    private String notes;
    private GregorianCalendar dueDate;
    private boolean isCompleted;

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDueDate(GregorianCalendar dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public int compareTo(Task o) {
        return getDueDate().compareTo(o.getDueDate());
    }
}
