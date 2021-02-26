package golden.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.util.Date;

public class Token {
  public String getToken(boolean isVip, String username, String pwd) {
    String token = null;
    try {
      Date expiresAt = new Date(System.currentTimeMillis() + 3600*1000*168);//���ù���ʱ��Ϊ7��
      token = JWT.create()
        .withIssuer("auth0")
        .withClaim("isVip", Boolean.valueOf(isVip))
        .withClaim("account", username)
        .withClaim("pwd", pwd)
        .withExpiresAt(expiresAt)
        
        .sign(Algorithm.HMAC256("mysecret"));
    } catch (JWTCreationException jWTCreationException) {
    
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } 
    return token;
  }
}
