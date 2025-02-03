package jpja.webapp.logging;

import java.time.LocalDate;
import java.time.LocalTime;

public class ActivityLog extends Log {
    //source = IP address
    private String source;
    private RequestMethod method;
    private String uri;
    private String query;

    private static final int MESSAGE_VARS = 4;
    
    public ActivityLog(int id, LocalDate date, LocalTime time, Level level, String source, RequestMethod method, String uri,
            String query) {
        super(id, date, time, level);
        this.source = source;
        this.method = method;
        this.uri = uri;
        this.query = query;
    }

    public ActivityLog(int id, LocalDate date, LocalTime time, Level level, String message) {
        super(id, date, time, level);
        setMessage(message);
    }
    
    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getMessage(){
        String message = "IP: "+ this.getSource() +" - Method: "+ method.name() + " - URI: "+uri+" - Query: "+query+"";
        return message;
    }

    public void setMessage(String message){
        if(message == null){
            throw new IllegalArgumentException("No message given");
        }
        String[] args = message.split("-");
        if(args.length > MESSAGE_VARS){
            throw new IllegalArgumentException("improperly formatted message");
        }
        for(String arg : args){
            this.parseArg(arg);
        }
        if(!areParamsNotNull()){
            throw new IllegalArgumentException("improperly formatted message");
        }
    }

    private void parseArg(String arg){
        if(arg == null){  
            throw new IllegalArgumentException("No arg given");
        }
        String[] parsedArg = arg.split("[:\\s]");
        if(parsedArg.length != 2){
            throw new IllegalArgumentException("improperly formatted arg");
        }
        ActivityArg[] args = ActivityArg.values();
        ActivityArg param = null;
        for(ActivityArg x : args){
            if(parsedArg[0].toLowerCase().contains(x.name().toLowerCase())){
                param = x;
            }
        }

        if(param == null){
            throw new IllegalArgumentException("improperly formatted arg");
        }
        switch (param) {
            case ActivityArg.IP:
                this.setSource(parsedArg[1]);
                break;
            case ActivityArg.METHOD:
                this.setMessage(parsedArg[1]);
                break;
            case ActivityArg.URI:
                this.setUri(parsedArg[1]);
                break;
            case ActivityArg.QUERY:
                this.setQuery(parsedArg[1]);
                break;
            default:
                throw new IllegalArgumentException("improperly formatted arg");
        }
    }

    private boolean areParamsNotNull(){
        return this.getSource() != null && this.getMethod() != null && this.getUri() != null && this.getQuery() != null;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isException(){
        return false;
    }
}
