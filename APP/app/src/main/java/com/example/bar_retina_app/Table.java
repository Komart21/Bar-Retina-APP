package com.example.bar_retina_app;

public class Table {
    private Command command;
    private int tableId;

    public Table(int tableId) {
        command = null;
        this.tableId = tableId;
    }

    public Command getCommand() {
        return command;
    }

    public int getTableId() {
        return tableId;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
