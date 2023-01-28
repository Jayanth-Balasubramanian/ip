package command;

import command.exceptions.CommandExecutionError;
import exceptions.CommandException;
import interfaces.Command;
import interfaces.Model;
import interfaces.View;
import model.TaskModel;

import java.util.List;

public class MarkDoneCommand implements Command {
    private final TaskModel taskModel;
    private final View taskView;
    private final int taskIndex;
    private static final String markedDoneMessage = "Nice! I've marked this task as done:\n";
    MarkDoneCommand(View taskView, TaskModel taskModel, int index) {
        this.taskIndex = index;
        this.taskView = taskView;
        this.taskModel = taskModel;
    }

    @Override
    public void execute() throws CommandExecutionError {
        int numTasks = taskModel.getNumberOfTasks();
        if (taskIndex >= numTasks) {
            throw new CommandExecutionError(String.format("You have only %d tasks", numTasks));
        } else if (taskIndex < 0) {
            throw new CommandExecutionError("index cannot be negative");
        }
        this.taskModel.markTaskDone(this.taskIndex);
        taskView.showMessage(markedDoneMessage + taskModel.getTask(taskIndex).toString());
    }
}
