package com.dogfight.dogfight.common.jwt;

import com.dogfight.dogfight.api.service.user.UserDetailServiceImple;
import com.dogfight.dogfight.domain.user.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${spring.jwt.key}")
    private String secretKey;

    @Value("${spring.jwt.live.access-expiration-time}")
    private Long accessExpiration;

    @Value("${spring.jwt.live.refresh-expiration-time}")
    private Long refreshExpiration;
    private static final String BEARER = "Bearer ";

    @Autowired
    private final UserDetailServiceImple userDetailsService;

    /**
     * AccessToken 생성
     * @param account 아이디
     * @return
     */
    public String createAccessToken(String account, Date date) {
        Claims claims = Jwts.claims().setSubject(account);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        redisTemplate.opsForValue().set(
                account,
                accessToken,
                accessExpiration,
                TimeUnit.MILLISECONDS
        );

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256 , secretKey)
                .compact();
    }

    /**
     * RefreshToken 생성
     * @param account
     * @return
     */
    public String createRefreshToken(String account, Date date) {
        Claims claims = Jwts.claims().setSubject(account);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256 , secretKey)
                .compact();

        redisTemplate.opsForValue().set(
                account,
                refreshToken,
                refreshExpiration,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Claims parseToken(String token, Key secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 유효성 검사
     * @param type 토큰 타입
     * @param token 토큰
     * @param response response
     */
    public boolean validateToken(String type, String token, HttpServletResponse response){
//        ObjectMapper mapper = new ObjectMapper();

        try{
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            System.out.println("정상");
            return true;
        } catch (ExpiredJwtException e){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseStatusException tokenExpiredException
                    = new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, type+ "토큰이 만료되었습니다.");

//            mapper.writeValue(response.getWriter(),tokenExpiredException.getReason());

            throw tokenExpiredException;
        } catch (Exception e){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            ResponseStatusException responseStatusException
                    = new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, type+ "토큰을 다시 확인해주세요.");

//            mapper.writeValue(response.getWriter(),responseStatusException.getReason());

            throw responseStatusException;
        }
    }

    public String getAccount(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public void saveAuthentication(String account){

        UserDetails userDetails = userDetailsService.loadUserByUsername(account);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
