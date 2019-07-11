package h.uniview.smarthouse.service;

import h.uniview.smarthouse.entity.UserEntity;

public interface UserService {
	
	UserEntity findByName(String username);

}
