package com.example.demo;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User user) throws Exception {
		// TODO Auto-generated method stub
		return user;
	}

}
