package com.nhjclxc.chatgpt4nhjclxc.config;


import com.nhjclxc.chatgpt4nhjclxc.model.model.TbUser;

/**
 * 当前请求上下文对象
 *
 * @author LuoXianchao
 * @since 2023/5/21 9:46
 */
public class ContextHolder {

	// 想存放到ThreadLoacl里面的数据的类型
	private static final ThreadLocal<TbUser> tbUser = new ThreadLocal<>();

	public static void setUser(TbUser user) {
		tbUser.set(user);
	}

	public static TbUser getUser() {
		 return ContextHolder.tbUser.get();
	}

	public static void removeUser(){
		tbUser.remove();
	}

}
