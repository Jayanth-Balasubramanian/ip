package duke.model;

import duke.interfaces.Model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskModel implements Model {
    private static final String taskStorePath = "./src/main/data/tasks.ser";
    private final ArrayList<Task> tasks;
    private final File tasksFile;
    public TaskModel() {
        this.tasksFile = new File(taskStorePath);
        if (tasksFile.exists() && tasksFile.length() > 0) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(tasksFile));
                Object obj = in.readObject();
                this.tasks = (ArrayList<Task>) obj;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                tasksFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.tasks = new ArrayList<>();
        }
    }
    private void writeToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream(this.tasksFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tasks);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Task createTask(String description) {
        Task newTask = new ToDo(description);
        this.tasks.add(newTask);
        writeToFile();
        return newTask;
    }

    public Task createTask(String description, LocalDateTime deadline) {
        Task newTask = new Deadline(description, deadline);
        this.tasks.add(newTask);
        writeToFile();
        return newTask;
    }

    public Task createTask(String description, LocalDateTime startTime, LocalDateTime endTime) {
        Task newTask = new Event(description, startTime, endTime);
        this.tasks.add(newTask);
        writeToFile();
        return newTask;
    }

    @Override
    public List<Task> getTasks() {
        return this.tasks;
    }

    public List<Task> getTasksOn(LocalDateTime time) {
        // only deadlines and events
        List<Task> res = new ArrayList<>();
        for (Task task: this.tasks) {
            if (task.isDueOn(time)) {
                res.add(task);
            }
        }
        return res;
    }
    public Task getTask(int index) {
        return this.tasks.get(index); // out of bounds exception
    }

    public int getNumberOfTasks() {
        return this.tasks.size();
    }

    public void deleteTask(int indexToRemove) {
        this.tasks.remove(indexToRemove); // handle out of bounds error
        writeToFile();
    }

    public void markTaskDone(int taskIndex) {
        tasks.get(taskIndex).markTaskDone(); // handle out of bounds exception
        writeToFile();
    }

    public void markTaskUndone(int taskIndex) {
        tasks.get(taskIndex).markTaskUndone(); // handle out of bounds exception
        writeToFile();
    }

    public List<Task> findTasks(String subStr) {
        List<Task> res = new ArrayList<>();
        for (Task task: tasks) {
            if (task.descriptionContains(subStr)) {
                res.add(task);
            }
        }
        return res;
    }
}
