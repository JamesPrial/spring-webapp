package jpja.webapp.logging;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Log {
    private int id;
    private LocalDate date;
    private LocalTime time;
    private Level level;

    public Log(int id, LocalDate date, LocalTime time, Level level) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.level = level;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract String getMessage();
    abstract void setMessage(String message);
    abstract String getSource();
    abstract void setSource(String source);

    abstract boolean isException();
}
