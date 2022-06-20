package hello.productmanagement.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.logging.Handler;
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String requestURI = request.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
        }
        log.info("==========================================================================================");
        log.info("PREHANDLE REQUEST [{}][{}][{}]",uuid, requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception{
        log.info("postHandle [{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                 Object handler, Exception ex) throws Exception{
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("AFTER REQUEST [{}][{}]", logId, requestURI);
        log.info("==========================================================================================");
        log.info("ERROR = [{}]",ex);
        if(ex!=null){
            log.error("afterCompletion error!!",ex);
        }
    }
}
