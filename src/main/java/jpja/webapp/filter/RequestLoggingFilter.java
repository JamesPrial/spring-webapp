package jpja.webapp.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpja.webapp.service.LoggingService;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private final LoggingService loggingService;

    public RequestLoggingFilter(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String query = request.getQueryString();
        if(query == null || query.equals("")){
            query = "NA";
        }
        if(requestUri.equals("/") && !query.equals("NA")){
            loggingService.logActivityAsWarn(clientIp, httpMethod, requestUri, query);
        }else{
            loggingService.logActivity(clientIp, httpMethod, requestUri, query);
        }
        filterChain.doFilter(request, response);
    }
}
