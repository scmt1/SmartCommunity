package me.zhengjie.modules.security.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.config.SecurityProperties;
import me.zhengjie.system.service.MenuService;
import me.zhengjie.system.service.RoleService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.MenuDto;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author /
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

   private final SecurityProperties properties;
   private static final String AUTHORITIES_KEY = "auth";
   private Key key;

   private final MenuService menuService;

   private final UserService userService;

   private final RoleService roleService;

   public TokenProvider(SecurityProperties properties, MenuService menuService, UserService userService, RoleService roleService) {
      this.properties = properties;
      this.menuService = menuService;
      this.userService = userService;
      this.roleService = roleService;
   }


   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   public String createToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date validity = new Date(now + properties.getTokenValidityInSeconds());

      return Jwts.builder()
              .setSubject(authentication.getName())
//         .claim(AUTHORITIES_KEY, authorities)
              .signWith(key, SignatureAlgorithm.HS512)
              .setExpiration(validity)
              .compact();
   }

   Authentication getAuthentication(String token) {
      Claims claims = Jwts.parser()
              .setSigningKey(key)
              .parseClaimsJws(token)
              .getBody();
      List<Object> menunameBackList = menuService.findMenuPermissionByuserName(claims.getSubject());
      List<String> menunameList = (List<String>)(List)menunameBackList;
      menunameList = menunameList.stream().distinct()
              .filter(s -> StringUtils.isNotBlank(s))
              .collect(Collectors.toList());
      String[] menunameArray =  menunameList.toArray(new String[menunameList.size()]);

      Collection<? extends GrantedAuthority> authorities =
              Arrays.stream( menunameArray)
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   boolean validateToken(String authToken) {
      try {
         Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         log.info("Invalid JWT signature.");
         e.printStackTrace();
      } catch (ExpiredJwtException e) {
         log.info("Expired JWT token.");
         e.printStackTrace();
      } catch (UnsupportedJwtException e) {
         log.info("Unsupported JWT token.");
         e.printStackTrace();
      } catch (IllegalArgumentException e) {
         log.info("JWT token compact of handler are invalid.");
         e.printStackTrace();
      }
      return false;
   }

   public String getToken(HttpServletRequest request){
      final String requestHeader = request.getHeader(properties.getHeader());
      if (requestHeader != null && requestHeader.startsWith(properties.getTokenStartWith())) {
         return requestHeader.substring(7);
      }
      return null;
   }
}
