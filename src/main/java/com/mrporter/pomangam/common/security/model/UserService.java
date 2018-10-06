package com.mrporter.pomangam.common.security.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.constant.MenuType;
import com.mrporter.pomangam.common.security.model.domain.Role;
import com.mrporter.pomangam.common.security.model.domain.User;
import com.mrporter.pomangam.member.dao.AdminCrudDAO;
import com.mrporter.pomangam.member.dao.AuthorityCrudDAO;
import com.mrporter.pomangam.member.vo.AdminBean;
import com.mrporter.pomangam.member.vo.AuthorityBean;

@Service
public class UserService implements UserDetailsService {

    @Override
    public User loadUserByUsername(final String username) throws UsernameNotFoundException {
    	User user = new User();
    	AdminBean adminBean = new AdminCrudDAO().getMember(username);
    	
    	if (adminBean == null) {
    		throw new UsernameNotFoundException("접속자 정보를 찾을 수 없습니다.");
    	} else {
    		
        	user.setUsername(adminBean.getUsername());
        	user.setPassword(adminBean.getPassword());
        	user.setNickname(adminBean.getNickname());
        	
        	List<Role> roles = new ArrayList<Role>();
        	Map<Integer, String> menumap = MenuType.getIdxMap();
        	AuthorityCrudDAO authorityCrudDAO = new AuthorityCrudDAO();
        	try {
        		Gson gson = new Gson();
        		List<AuthorityBean> authlist = gson.fromJson(authorityCrudDAO.getList(), 
        				new TypeToken<List<AuthorityBean>>() {}.getType());
        		
        		for(AuthorityBean bean : authlist) {
        			if(bean.getAuthname().equals(adminBean.getRole())) {
        				String[] authority = bean.getAuthority().split("");
        				if(isTrue(authority[0])) {
        	        		Role role = new Role();
        	        		role.setName("ROLE_"+menumap.get(bean.getType())+"_VIEW");
        	        		roles.add(role);
        	        	}
        				if(isTrue(authority[1])) {
        	        		Role role = new Role();
        	        		role.setName("ROLE_"+menumap.get(bean.getType())+"_EDIT");
        	        		roles.add(role);
        	        	}
        			}
        		}
        		
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        	// 기본 권한
        	Role role = new Role();
    		role.setName("ROLE_USER");
    		roles.add(role);
        	
        	user.setAuthorities(roles);
        	//System.out.println(user.getAuthorities());
    	}
    	
    	return user;
    }
    
    public static boolean isTrue(String zeroOrOne) {
    	return zeroOrOne.equals("0")?false:true;
    }
}
