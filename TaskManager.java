import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class TaskManager {
    private static final InputValidator inputValidator = new InputValidator();
    static ArrayList<String> pendingTasks = new ArrayList<>();
    static String[] completedTasks = new String[100];
    static int completedCount = 0;

    public static class InputValidator {
        private final Scanner scanner = new Scanner(System.in);
    
        public int getInt(String prompt) {
            while (true) {
                try {
                    System.out.print(prompt);
                    return Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        pendingTasks = FileHandler.loadData("tasks.txt");
        loadCompleted();

        boolean running = true;
        while (running) {
            System.out.println("\n--- Task Manager ---");
            System.out.println("1. View Tasks");
            System.out.println("2. Add Task");
            System.out.println("3. Complete Task");
            System.out.println("4. Exit");
            int choice = inputValidator.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    showTasks();
                    break;
                case 2:
                    System.out.print("Enter new task: ");
                    String task = scanner.nextLine();
                    pendingTasks.add(task);
                    break;
                case 3:
                    completeTask();
                    break;
                case 4:
                    FileHandler.saveData(pendingTasks, "tasks.txt");
                    saveCompleted();
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    static void showTasks() {
        System.out.println("\nPending Tasks:");
        for (int i = 0; i < pendingTasks.size(); i++) {
            System.out.println((i + 1) + ". " + pendingTasks.get(i));
        }

        System.out.println("\nCompleted Tasks:");
        for (int i = 0; i < completedCount; i++) {
            System.out.println("- " + completedTasks[i]);
        }
    }

    static void completeTask() {
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks.");
            return;
        }
        showTasks();
        int index = inputValidator.getInt("Enter task number to complete: ") - 1;
        try {
            String done = pendingTasks.remove(index);
            if (completedCount < completedTasks.length) {
                completedTasks[completedCount++] = done;
                System.out.println("Task completed!");
            } else {
                System.out.println("Completed task list is full.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index. Try again.");
        }
    }

    static void loadCompleted() {
        try (BufferedReader reader = new BufferedReader(new FileReader("completed.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && completedCount < completedTasks.length) {
                completedTasks[completedCount++] = line;
            }
        } catch (IOException e) {
            System.out.println("No previous completed tasks found.");
        }
    }

    static void saveCompleted() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("completed.txt"))) {
            for (int i = 0; i < completedCount; i++) {
                writer.write(completedTasks[i]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving completed tasks: " + e.getMessage());
        }
    }
}
