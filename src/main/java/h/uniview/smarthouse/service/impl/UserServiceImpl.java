package h.uniview.smarthouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import h.uniview.smarthouse.data.UserCenter;
import h.uniview.smarthouse.entity.UserEntity;
import h.uniview.smarthouse.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserCenter userCenter;
	
	@Override
	public UserEntity findByName(String username) {
		
		if(!userCenter.getLoginMap().containsKey(username)) {
			return null;
		}
		
		userCenter.getLoginMap().forEach((k, v) -> {
			System.out.println("username:" + k + ",pwd:" + v);
		});
		
		return new UserEntity(username, userCenter.getLoginMap().get(username));
	}

}
