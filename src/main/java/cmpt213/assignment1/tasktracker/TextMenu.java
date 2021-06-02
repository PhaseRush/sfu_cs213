package cmpt213.assignment1.tasktracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TextMenu {
    private final Scanner sc;
    private final static String menuTitle = "My catgirl cafe";
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final static Predicate<Task> beforeNow = task -> task.getDueDate().before(Calendar.getInstance());

    String[] options = {
            "List all tasks",
            "Add task",
            "Remove Task",
            "Mark task as completed",
            "List overdue incomplete tasks",
            "List upcoming incomplete tasks",
            "Exit"
    };

    public TextMenu() {
        sc = new Scanner(System.in);
    }

    public void readInput(final List<Task> tasks) {
        printOptions();
        int option = Integer.parseInt(sc.nextLine());
        switch (option) {
            case 1 -> listAllTasks(tasks, true);
            case 2 -> addTask(tasks);
            case 3 -> removeTask(tasks);
            case 4 -> markAsCompleted(tasks);
            case 5 -> listOverdueIncomplete(tasks);
            case 6 -> listUpcomingIncomplete(tasks);
            case 7 -> {
                exit(tasks);
                return;
            }
            default -> System.out.println("Invalid option. Enter a number between 1-7 inclusive.");
        }
        readInput(tasks);
    }

    private void printOptions() {
        System.out.println("-".repeat(menuTitle.length() + 4));
        System.out.printf("| %s |\n", menuTitle);
        System.out.println("-".repeat(menuTitle.length() + 4));
        for (int i = 0; i < options.length; i++) {
            System.out.println(i + 1 + ": " + options[i]);
        }
        System.out.print("Choose an option by entering 1-7 inclusive: ");
    }

    private void listAllTasks(final List<Task> tasks, final boolean displayCompletionStatus) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to show.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("Task #" + (i + 1));
                System.out.println("Task: " + tasks.get(i).getName());
                System.out.println("Notes: " + tasks.get(i).getNotes());
                System.out.println("Due date: " + format.format(tasks.get(i).getDueDate().getTime()));
                if (displayCompletionStatus) {
                    System.out.println("Completed? " + tasks.get(i).isCompleted());
                }
                System.out.println();
            }
        }
    }

    private void addTask(final List<Task> tasks) {
        final Task newTask = new Task();
        System.out.println("Enter the name of the new task:");
        newTask.setName(sc.nextLine());
        System.out.println("Enter the notes of the new task:");
        newTask.setNotes(sc.nextLine());
        System.out.println("Enter the year of the due date:");
        final var year = Integer.parseInt(sc.nextLine());
        System.out.println("Enter the month of the due date (1-12)");
        final var month = Integer.parseInt(sc.nextLine());
        System.out.println("Enter the day of the due date (1-28/29/30/31):");
        final var day = Integer.parseInt(sc.nextLine());
        System.out.println("Enter the hour of the due date (0-23):");
        final var hour = Integer.parseInt(sc.nextLine());
        System.out.println("Enter the minute of the due date (0-59):");
        final var minute = Integer.parseInt(sc.nextLine());
        newTask.setDueDate(new GregorianCalendar(year, month - 1, day, hour, minute));
        newTask.setCompleted(false);
        System.out.println(newTask.getName() + " has been added to the list of tasks.");
        tasks.add(newTask);
    }

    private int promptIndex(final List<Task> tasks, final String prompt) {
        while (true) {
            listAllTasks(tasks, true);
            System.out.println(prompt);
            final var index = Integer.parseInt(sc.nextLine());
            if (index > tasks.size() + 1 || index < 0) {
                System.out.println("Invalid number, try again.");
            } else {
                return index;
            }
        }
    }

    private void removeTask(final List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to show.");
        } else {
            tasks.sort(Comparator.comparing(Task::getDueDate));
            final var index = promptIndex(tasks, "Select a task to remove.");
            if (index != 0) {
                Task toRemove = tasks.remove(index - 1);
                System.out.println("Task " + toRemove.getName() + " has been removed from the list of tasks.");
            }
        }
    }

    private List<Task> filterSortTasks(final List<Task> tasks, final Predicate<Task> condition) {
        return tasks.stream()
                .filter(condition)
                .sorted()
                .collect(Collectors.toList());
    }

    private void markAsCompleted(final List<Task> tasks) {
        final var incompleteTasks = filterSortTasks(tasks, task -> !task.isCompleted());
        if (incompleteTasks.isEmpty()) {
            System.out.println("There are no incomplete tasks to show.");
        } else {
            var index = promptIndex(incompleteTasks, "Select a task to mark as complete.");
            if (index != 0) {
                incompleteTasks.get(index - 1).setCompleted(true);
                System.out.println("Task " + incompleteTasks.get(index - 1).getName() + " is now completed.");
            }
        }
    }

    private void listOverdueIncomplete(final List<Task> tasks) {
        final var overdueIncomplete = filterSortTasks(tasks,
                task -> !task.isCompleted() && beforeNow.test(task));
        if (overdueIncomplete.isEmpty()) {
            System.out.println("No overdue incomplete tasks to show.");
        } else {
            listAllTasks(overdueIncomplete, false);
        }
    }

    private void listUpcomingIncomplete(final List<Task> tasks) {
        final var upcomingIncomplete = filterSortTasks(tasks,
                task -> !task.isCompleted() && !beforeNow.test(task));
        if (upcomingIncomplete.isEmpty()) {
            System.out.println("No upcoming incomplete tasks to show.");
        } else {
            listAllTasks(upcomingIncomplete, false);
        }
    }

    private void exit(final List<Task> tasks) {
        try (Writer fileWriter = new FileWriter("./src/main/resources/tasks.json")) {
            gson.toJson(tasks, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Thank you for visiting the Catgirl Cafe");
    }
}
