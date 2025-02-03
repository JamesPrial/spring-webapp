package jpja.webapp.service;

import org.springframework.stereotype.Service;

import jpja.webapp.logging.ActivityLog;
import jpja.webapp.logging.AppLog;
import jpja.webapp.logging.Level;
import jpja.webapp.logging.Log;
import jpja.webapp.logging.RequestMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LogParserService {

    private static final String LOG_DIR_PATH = "logs/";
    private static final String APP_LOG_PREFIX = "app.";
    private static final String ACTIVITY_LOG_PREFIX = "activity.";
    private static final String DEFAULT_LOG_NAME = "app";
    private static final String DEFAULT_ACTIVITY_NAME = "activity";
    private static final String LOG_FILE_SUFFIX = ".log";

    private static final String test = "app.2025-01-01";

    // Define the log pattern based on your logback-spring.xml
    //"(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(\\w+)\\s+(\\d+)\\s+---\\s+\\[(.*?)\\]\\s+(\\S+)\\s+:\\s+(.*)" 
    private static final Pattern LOG_PATTERN = Pattern.compile(
    "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+" + // 1. Timestamp
    "(\\w+)\\s+" +                                       // 2. Log Level
    "\\[(.*?)\\]\\s+" +                                  // 3. Thread Name
    "(\\S+)\\s+-\\s+" +                                  // 4. Logger Name
    "(.*)$"                                              // 5. Log Message
    );

    private static final Pattern ACTIVITY_PATTERN = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) " + 
        "\\[(\\w+)\\] IP: ([\\d\\.]+) - "+
        "Method: (\\w+) - "+
        "URI: (\\S+) - "+
        "Query: (.*)$"
    );

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Lists all available log files sorted by date descending.
     */
    public String getDefault(){
        return "" + DEFAULT_LOG_NAME + LOG_FILE_SUFFIX;
    }

    public List<String> listAvailableLogFiles() {
        File logDir = new File(LOG_DIR_PATH);
        if (!logDir.exists() || !logDir.isDirectory()) {
            return new ArrayList<>();
        }

        File[] logFiles = logDir.listFiles((dir, name) ->
            (name.startsWith(APP_LOG_PREFIX) || name.startsWith(ACTIVITY_LOG_PREFIX) || name.startsWith(DEFAULT_LOG_NAME) || name.startsWith(DEFAULT_ACTIVITY_NAME)) &&
            name.endsWith(LOG_FILE_SUFFIX)
        );

        if (logFiles == null) {
            return new ArrayList<>();
        }

        // Sort files by date descending
        return List.of(logFiles).stream()
                .map(File::getName)
                .sorted((a, b) -> b.compareTo(a)) // Descending order
                .collect(Collectors.toList());
    }

    public List<Log> parseLogFile(String logFileName, boolean isActivity, String levelFilter) throws IOException{
        List<Log> logs = new ArrayList<Log>();
        BufferedReader logFile = getLogFile(logFileName);
        String line;
        String nextLine = logFile.readLine();
        int id = 0;
        do {
            //System.out.println("id: " + id);
            line = nextLine;
            nextLine = logFile.readLine();
            Matcher matcher = isActivity ? ACTIVITY_PATTERN.matcher(line) : LOG_PATTERN.matcher(line);
            while(!isActivity && !doesLineMatch(nextLine, isActivity)){
                line = "" + line + "\n" + nextLine;
                nextLine = logFile.readLine();
            }
            if(matcher.find()){
                Log parsed = parseEntry(line, matcher, isActivity, id);
                //System.out.println("levelFilter: " + levelFilter + "parsedLevel: " + parsed.getLevel().toString());
                //System.out.println("!isActivity = " + !isActivity + ". !\"ALL\".equalsIgnoreCase(levelFilter): " + !"ALL".equalsIgnoreCase(levelFilter) + 
                //                    ".\n parsed.getLevel().toString().equalsIgnoreCase(levelFilter): " + parsed.getLevel().toString().equalsIgnoreCase(levelFilter));
                if(isActivity || "ALL".equalsIgnoreCase(levelFilter) || parsed.getLevel().toString().equalsIgnoreCase(levelFilter)){
                    logs.add(parsed);
                }
                id++;
            }
        } while (nextLine != null);
        return logs;
    }

    private boolean doesLineMatch(String line, boolean isActivity){
        if(line == null){
            return true;
        }
        Matcher matcher = isActivity ? ACTIVITY_PATTERN.matcher(line) : LOG_PATTERN.matcher(line);
        return matcher.find();
    }

    private Log parseEntry(String entry, Matcher matcher, boolean isActivity, int id){
        //System.out.println("parseEntry: " + entry + "\nnewLineIdx: " + entry.indexOf("\n"));
        LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        Level level = levelHelper(matcher.group(2));
        if(isActivity){
            String source = matcher.group(3);
            RequestMethod method = requestHelper(matcher.group(4));
            String uri = matcher.group(5);
            String query = matcher.group(6);
            return new ActivityLog(id, date, time, level, source, method, uri, query);
        }else{
            String thread = matcher.group(3);
            String logger = matcher.group(4);
            String message = matcher.group(5);
            int newLineIdx = entry.indexOf("\n");
            String note = newLineIdx > 0 ? entry.substring(newLineIdx) : null;

            //System.out.println("newLineIdx: " + newLineIdx + ". parseEntry note: " + note);
            return new AppLog(id, date, time, level, logger, thread, message, note);
        }
    }



    private RequestMethod requestHelper(String requestAsString) {
        if (requestAsString == null) {
            return RequestMethod.DEFAULT;
        }
        switch (requestAsString) {
            case "GET":
                return RequestMethod.GET;
            case "HEAD":
                return RequestMethod.HEAD;
            case "POST":
                return RequestMethod.POST;
            case "PUT":
                return RequestMethod.PUT;
            case "DELETE":
                return RequestMethod.DELETE;
            case "CONNECT":
                return RequestMethod.CONNECT;
            case "OPTIONS":
                return RequestMethod.OPTIONS;
            case "TRACE":
                return RequestMethod.TRACE;
            case "PATCH":
                return RequestMethod.PATCH;
            default:
                return RequestMethod.DEFAULT;
        }
    }


    private Level levelHelper(String levelAsString){
        Level level = Level.DEFAULT;
        if(levelAsString != null){
            if(levelAsString.equals("INFO")){
                level = Level.INFO;
            }else if(levelAsString.equals("WARN")){
                level = Level.WARN;
            }else if(levelAsString.equals("ERROR")){
                level = Level.ERROR;
            }else if(levelAsString.equals("DEBUG")){
                level = Level.DEBUG;
            }
        }
        return level;
    }
    /**
     * The function `getLogFile` is a private method in Java that is intended to return a File object
     * based on the provided log file name.
     * 
     * @param logFileName The `logFileName` parameter is a string that represents the name of the log
     * file that you want to retrieve. If null, returns the default/active log.
     * @return new File containing the log file
     */
    private BufferedReader getLogFile(String logFileName) throws FileNotFoundException{
        logFileName = (logFileName == null || logFileName.isEmpty()) ? DEFAULT_LOG_NAME : logFileName;
        return new BufferedReader(new FileReader("" + LOG_DIR_PATH +logFileName));
    }

    public Pattern getLogPattern(){
        return LOG_PATTERN;
    }

    public Pattern getActivityPattern(){
        return ACTIVITY_PATTERN;
    }
}
 