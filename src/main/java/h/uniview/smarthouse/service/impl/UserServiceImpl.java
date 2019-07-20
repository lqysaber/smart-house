package h.uniview.smarthouse.service.impl;

import h.uniview.smarthouse.data.UserXMLCenter;
import h.uniview.smarthouse.entity.UserEntity;
import h.uniview.smarthouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserXMLCenter userXMLCenter;
	
	@Override
	public UserEntity findByName(String username) {
		
		if(!userXMLCenter.getLoginMap().containsKey(username)) {
			return null;
		}

		userXMLCenter.getLoginMap().forEach((k, v) -> {
			System.out.println("username:" + k + ",pwd:" + v);
		});
		
		return new UserEntity(username, userXMLCenter.getLoginMap().get(username));
	}

}
