package com.mrporter.pomangam.common.security;

import java.security.PrivateKey;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.mrporter.pomangam.common.security.crypt.RSA;
import com.mrporter.pomangam.common.security.model.UserService;
import com.mrporter.pomangam.common.security.model.domain.User;
import com.mrporter.pomangam.common.security.password.PasswordEncoding;
import com.mrporter.pomangam.common.security.password.SHAPasswordEncoder;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    @Autowired
    UserService userService;
   
    @Override
    public Authentication authenticate(Authentication authentication) 
    		throws AuthenticationException {
    	HttpServletRequest request = 
    			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    	HttpSession session = request.getSession();
    	try {
    		SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
    		shaPasswordEncoder.setEncodeHashAsBase64(true);
    		PasswordEncoding pwEncoder = new PasswordEncoding(shaPasswordEncoder);
    		PasswordEncoding bCryptEncoder = new PasswordEncoding(new BCryptPasswordEncoder());
    		
    		PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
    		String username = decryptRSA(privateKey, authentication.getName());
            String password = decryptRSA(privateKey, (String) authentication.getCredentials());
            User user;
            Collection<? extends GrantedAuthority> authorities;
            try {
                user = userService.loadUserByUsername(username);
        		
                boolean isSuccess = bCryptEncoder.matches(pwEncoder.encode(password), user.getPassword());
                if (!isSuccess) {
                	throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
                }
                authorities = user.getAuthorities();
            } catch (UsernameNotFoundException e) {
                logger.info(e.toString());
                throw new UsernameNotFoundException(e.getMessage());
            } catch (BadCredentialsException e) {
                logger.info(e.toString());
                throw new BadCredentialsException(e.getMessage());
            }
            user.setPassword("");
            session.setAttribute("user", new Gson().toJson(user));
            
            return new UsernamePasswordAuthenticationToken(user, password, authorities);
    	} finally {
    		session.removeAttribute("__rsaPrivateKey__");
		}
    }

	@Override
	public boolean supports(Class<?> authentication) {
		  return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	private String decryptRSA(PrivateKey privateKey, String cipher) throws RuntimeException {
		String plainText = null;
		if (privateKey == null) {
			throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
		} else {
			RSA rsa = new RSA(); 
			plainText = rsa.decrypt(privateKey, cipher);
		}
		return plainText;
	}
}
