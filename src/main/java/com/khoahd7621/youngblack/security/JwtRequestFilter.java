package com.khoahd7621.youngblack.security;

import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    private void setSecurityContext(User user) {
        UserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/api/v1/login")
                || (request.getRequestURI().equals("/api/v1/register"))
                || (request.getRequestURI().startsWith("/api/v1/category"))
                || (request.getRequestURI().startsWith("/api/v1/product"))
                || (request.getRequestURI().startsWith("/api/v1/product-detail"))
                || (request.getRequestURI().startsWith("/swagger-ui"))
                || (request.getRequestURI().startsWith("/v3/api-docs"))
                || ((request.getRequestURI().startsWith("/api/v1/rating")) && HttpMethod.GET.toString().equals(request.getMethod()))) {
            filterChain.doFilter(request, response);
        } else {
            final Optional<String> requestTokenHeaderOpt = getJwtFromRequest(request);
            if (requestTokenHeaderOpt.isPresent()) {
                try {
                    String accessToken = requestTokenHeaderOpt.get();
                    Jws<Claims> listClaims = jwtTokenUtil.getJwsClaims(accessToken);
                    if (!jwtTokenUtil.validateTokenHeader(listClaims.getHeader())) {
                        throw new RuntimeException("Invalid JWT");
                    }
                    Optional<User> userOptional = userRepository.findById(jwtTokenUtil.getUserIdFromClaims(listClaims.getBody()));
                    if (userOptional.isEmpty()) {
                        throw new RuntimeException("Invalid JWT");
                    }
                    setSecurityContext(userOptional.get());
                    filterChain.doFilter(request, response);
                } catch (SignatureException se) {
                    throw new SignatureException("Invalid JWT signature", se);
                } catch (IllegalArgumentException iae) {
                    throw new IllegalArgumentException("Unable to get JWT", iae);
                } catch (ExpiredJwtException eje) {
                    throw new ExpiredJwtException(null, null, "Token has expired", eje);
                } catch (MalformedJwtException mje) {
                    throw new MalformedJwtException("JWT was not correctly constructed", mje);
                }
            } else {
                throw new RuntimeException("JWT Access Token does not start with 'Bearer ");
            }
        }
    }
}
