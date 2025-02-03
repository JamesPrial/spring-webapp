package jpja.webapp.logging;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppLog extends Log{
    private String thread;
    private String logger;
    private String message;
    private StackTrace trace;
    private String notes;
    
    public AppLog(int id, LocalDate date, LocalTime time, Level level, String thread, String logger, String message,
            StackTrace trace, String notes) {
        super(id, date, time, level);
        this.thread = thread;
        this.logger = logger;
        this.message = message;
        this.trace = trace;
        this.notes = notes;
    }

    public AppLog(int id, LocalDate date, LocalTime time, Level level, String source, String message) {
        super(id, date, time, level);
        setSource(source);
        parseAndSetMessage(message);
    }

    public AppLog(int id, LocalDate date, LocalTime time, Level level, String thread, String logger, String message) {
        super(id, date, time, level);
        this.thread = thread;
        this.logger = logger;
        parseAndSetMessage(message);
    }

    public AppLog(int id, LocalDate date, LocalTime time, Level level, String thread, String logger, String message, String note) {
        super(id, date, time, level);
        this.thread = thread;
        this.logger = logger;
        parseAndSetMessage(message);
        parseNote(note);
    }
    
    public String getSource(){
        return "" + this.thread + " " + this.logger;
    }

    

    //source format: "\\[(.*?)\\]\\s+(\\S+)\\s+"
    public void setSource(String source){
        Matcher matcher = Pattern.compile("\\[(.*?)\\]\\s+(\\S+)\\s+").matcher(source);
        if(matcher.find()){
            setThread(matcher.group(1));
            setLogger(matcher.group(2));
        }else{
            throw new IllegalArgumentException(""+source + " does not match regex");
        }
    }

    private void parseAndSetMessage(String message){
        if(message == null || !message.contains("\n")){
            this.message = message;
            return;
        }
        message = message.trim();
        int firstNewLineIdx = message.indexOf("\n");
        if(firstNewLineIdx > 0){
            this.message = message.substring(0, firstNewLineIdx);
            parseNote(message.substring(message.indexOf("\n")));
        }else{
            this.message = message;
        }
    }

    private void parseNote(String note){
        note = note != null ? note.trim() : note;
        //System.out.println("parseNote: " + note);
        if(note == null || !note.contains("\n")){
            this.notes = this.notes == null ? null : "" + this.notes + note;
            this.trace = null;
            return;
        }
        note = note.trim();
        String firstLine = note.substring(0, note.indexOf("\n"));
        //System.out.println("first newline: " + note.indexOf("\n"));
        if(firstLine.contains("Exception")){
            this.trace = new StackTrace(note);
        }else{
            this.notes = this.notes == null ? firstLine : "" + this.notes + firstLine;
            parseNote(note.substring(note.indexOf("\n")));
        }
    }

    public boolean isException(){
        return this.trace != null;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StackTrace getTrace() {
        return trace;
    }

    public void setTrace(StackTrace trace) {
        this.trace = trace;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    
}
