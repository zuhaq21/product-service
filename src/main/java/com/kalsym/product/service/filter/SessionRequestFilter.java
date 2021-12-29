package com.kalsym.product.service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalsym.product.service.VersionHolder;
import com.kalsym.product.service.model.Auth;
import com.kalsym.product.service.model.HttpReponse;
import com.kalsym.product.service.model.MySQLUserDetails;
import com.kalsym.product.service.service.MySQLUserDetailsService;
import com.kalsym.product.service.utility.Logger;
import com.kalsym.product.service.utility.DateTimeUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Sarosh
 */
@Component
public class SessionRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MySQLUserDetailsService jwtUserDetailsService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${services.user-service.session_details:not-known}")
    String userServiceSessionDetailsUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String logprefix = request.getRequestURI();

        Logger.application.info(Logger.pattern, VersionHolder.VERSION, "------------- " + request.getMethod() + " " + logprefix + "-------------", "", "");

        final String authHeader = request.getHeader("Authorization");
        Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "Authorization: " + authHeader, "");

        String accessToken = null;

        boolean tokenPresent = false;

        // Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (null != authHeader && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.replace("Bearer ", "");
            Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "token: " + accessToken, "");
            Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "token length: " + accessToken.length(), "");
            tokenPresent = true;
        } else {
            Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "token does not begin with Bearer String", "");
        }

        boolean authorized = false;
        if (accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Logger.application.info(Logger.pattern, VersionHolder.VERSION, logprefix, "sessionId: " + sessionId, "");

            try {
                ResponseEntity<HttpReponse> authResponse = restTemplate.postForEntity(userServiceSessionDetailsUrl, accessToken, HttpReponse.class);

                Date expiryTime = null;

                Auth auth = null;
                String username = null;

                if (authResponse.getStatusCode() == HttpStatus.ACCEPTED) {
                    ObjectMapper mapper = new ObjectMapper();
                    //Logger.application.debug(Logger.pattern, VersionHolder.VERSION, logprefix, "data: " + authResponse.getBody().getData(), "");
                    Logger.application.info(Logger.pattern, VersionHolder.VERSION, logprefix, "got session from user-service" , "");

                    auth = mapper.convertValue(authResponse.getBody().getData(), Auth.class);
                    username = auth.getSession().getUsername();
                    expiryTime = auth.getSession().getExpiry();
                    Logger.application.info(Logger.pattern, VersionHolder.VERSION, logprefix, "user role:"+auth.getRole()+" sessionType:"+auth.getSessionType() , "");
                }

                if (null != expiryTime && null != username) {
                    long diff = 0;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date currentTime = DateTimeUtil.currentTimestamp();
                        diff = expiryTime.getTime() - currentTime.getTime();
                    } catch (Exception e) {
                        Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "error calculating time to session expiry", "");
                    }
                    Logger.application.info(Logger.pattern, VersionHolder.VERSION, logprefix, "time to session expiry: " + diff + "ms", "");
                    if (0 < diff) {
                        authorized = true;
                        MySQLUserDetails userDetails = new MySQLUserDetails(auth, auth.getAuthorities(), auth.getSession().getOwnerId(), auth.getSessionType());

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                                                
                    } else {
                        Logger.application.warn(Logger.pattern, VersionHolder.VERSION, logprefix, "session expired", "");
                        //response.setStatus(HttpStatus.UNAUTHORIZED);
                        response.getWriter().append("Session expired");
                    }
                }
            } catch (IOException | IllegalArgumentException | RestClientException e) {
                Logger.application.error(Logger.pattern, VersionHolder.VERSION, logprefix, "Exception processing session ", "", e);

            }

        }

        Logger.cdr.info(request.getRemoteAddr() + "," + request.getMethod() + "," + request.getRequestURI() + "," + tokenPresent + "," + authorized);

        chain.doFilter(request, response);
    }
}
