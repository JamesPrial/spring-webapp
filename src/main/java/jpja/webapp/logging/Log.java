package jpja.webapp.logging;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Log {
    private LocalDate date;
    private LocalTime time;
    private Level level;
    private String source;

    public Log(LocalDate date, LocalTime time, Level level) {
        this.date = date;
        this.time = time;
        this.level = level;
        this.source = null;
    }

    public Log(LocalDate date, LocalTime time, Level level, String source) {
        this.date = date;
        this.time = time;
        this.level = level;
        this.source = source;
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
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }

    abstract String getMessage();
    abstract void setMessage(String message);
}
