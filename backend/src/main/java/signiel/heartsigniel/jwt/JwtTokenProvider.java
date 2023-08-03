package signiel.heartsigniel.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import signiel.heartsigniel.jpa.JpaUserDetailsService;
import signiel.heartsigniel.model.member.Authority;
import signiel.heartsigniel.token.RefreshTokenRepository;
import signiel.heartsigniel.token.dto.RefreshToken;
import signiel.heartsigniel.token.dto.TokenDto;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${security.jwt.secret}")
    private String salt;

    private Key secretKey;

    // 만료시간 : 1Day
    private static final long ACCESS_TIME =  60 * 1000L;
    private static final long REFRESH_TIME =  2 * 60 * 1000L;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    private final JpaUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;


    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }


    public TokenDto createAllToken(String nickname) {
        return new TokenDto(createToken(nickname, "Access"), createToken(nickname, "Refresh"));
    }

    // 토큰 생성
//    public String createToken(String account, List<Authority> roles) {
    public String createToken(String loginId, String type) {
        Date date = new Date();

        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        return Jwts.builder()
                .setSubject(loginId)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(secretKey)
                .compact();

    }


    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 account 획득
    public String getAccount(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization Header를 통해 인증을 한다.
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("Authorization");
//    }

    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
    }
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean refreshTokenValidation(String token) {

        // 1차 토큰 검증
        if(!validateToken(token)) return false;

        // DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByLoginId(getAccount(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }


}