package hello.productmanagement.web.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 * 기능 : 
 * 1. 세션 생성 
 *  - sessionId 생성 (임의의 추정 불가능한 랜덤 값)
 *  - 세션 저장소에 sessionId와 보관할 값 저장
 *  - sessionID로 응답 쿠키를 생성해서 클라이언트에 전달
 * 2. 세션 조회 
 *  - 클라이언트가 요청한 SessionId 쿠키의 값으로, 세션 저장소에 보관한 값 조회
 * 3. 세션 만료
 *  - 클라이언트가 요청한 sessionId 쿠키의 값으로 세션 저장소에 보관한 session와 값 제거
 */

public class SessionManager {
    public static final String SESSION_COOKIE_NAME = "mySessionId";
    
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>(); // ConcurrentHashMap 동시요청에 안전

    /**
     * 1. 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response){
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);
        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }
    /**
     * 2. 세션 조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request,SESSION_COOKIE_NAME);
        return sessionStore.get(sessionCookie.getValue());
    }
    /**
     * 3. 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request,String cookieName ){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
