package cmpt213.assignment1.tasktracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTracker {
    private final static Type taskListType = new TypeToken<ArrayList<Task>>() {
    }.getType();

    public static void main(String[] args) {
        List<Task> tasks = loadTasks(new Gson());
        TextMenu menu = new TextMenu();
        menu.readInput(tasks);
    }

    private static List<Task> loadTasks(final Gson gson) {
        // check if previous json exists
        ClassLoader classLoader = TaskTracker.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("tasks.json");
        if (inputStream == null) { // file doesn't exist
            return new ArrayList<>();
        } else { // file does exist
            return gson.fromJson(readInputStream(inputStream), taskListType);
        }
    }

    private static String readInputStream(final InputStream inputStream) {
        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

}
