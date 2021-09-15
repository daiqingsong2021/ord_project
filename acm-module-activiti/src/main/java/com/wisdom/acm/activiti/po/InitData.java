package com.wisdom.acm.activiti.po;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InitData {

	public static List<Company> getCompany() {
		List<Company> companyList = new ArrayList();
		companyList.add(new Company("平台管理人员", "ping", "0"));
		companyList.add(new Company("公司A", "company1", "1"));
		companyList.add(new Company("公司B", "company2", "1"));
		return companyList;
	}

	public static List<Role> getRole() {
		List<Role> roleList = new ArrayList<>();
		roleList.add(new Role("admin1", "ping", "管理员"));
		roleList.add(new Role("admin2", "company1", "管理员"));
		roleList.add(new Role("admin3", "company2", "管理员"));
		roleList.add(new Role("jihua1", "ping", "计划员"));
		roleList.add(new Role("jihua2", "company1", "计划员"));
		roleList.add(new Role("jihua3", "company2", "计划员"));
		roleList.add(new Role("shishi1", "ping", "实施员"));
		roleList.add(new Role("shishi2", "company1", "实施员"));
		roleList.add(new Role("shishi3", "company2", "实施员"));
		return roleList;
	}

	public static List<User> getUser() {
		List<User> userList = new ArrayList<>();
		userList.add(new User("1", "admin", "平台管理员", "Admin123456@", "ping", null, null));
		userList.add(new User("2", "companyAadmin", "公司A管理员", "Admin123456@", "company1", null, null));
		userList.add(new User("3", "companyBadmin", "公司B管理员", "Admin123456@", "company2", null, null));
		userList.add(new User("4", "companyAplan", "公司A计划员", "Admin123456@", "company1", null, null));
		userList.add(new User("5", "companyBplan", "公司B计划员", "Admin123456@", "company2", null, null));
		userList.add(new User("6", "companyAimplement", "公司A实施员", "Admin123456@", "company1", null, null));
		userList.add(new User("7", "companyBimplement", "公司B实施员", "Admin123456@", "company2", null, null));
		return userList;
}

	public static List<UserRole> getUserRole() {
		List<UserRole> userList = new ArrayList<>();
		userList.add(new UserRole("1", "admin1"));
		userList.add(new UserRole("2", "admin2"));
		userList.add(new UserRole("3", "admin3"));
		userList.add(new UserRole("4", "jihua2"));
		userList.add(new UserRole("5", "jihua3"));
		userList.add(new UserRole("6", "shishi2"));
		userList.add(new UserRole("7", "shishi3"));
		return userList;
	}

	public static List<Role> getRole(String userId) {
		List<String> roleIdList = getUserRole().stream().filter(userRole -> userId != null
				&& userId.equals(userRole.getUserId())).map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
		if (roleIdList == null || (roleIdList != null && roleIdList.size() < 1)) {
			return new ArrayList<>();
		}
		List<Role> roleList = getRole().stream().filter(role -> roleIdList.contains(role.getId())).collect(Collectors.toList());
		if (roleList == null || (roleList != null && roleList.size() < 1)) {
			return new ArrayList<>();
		}
		return roleList;
	}

	public static User getLogin(String loginId, String password) {
		Optional<User> userOptional = getUser().stream().filter(user ->
				loginId != null && password != null && loginId.equals(user.getLoginId())
						&& password.equals(user.getPassword())).findFirst();
		if (userOptional.isPresent()) {
			return userOptional.get();
		} else {
			return null;
		}
	}

	public static User getUser(String userId) {
		Optional<User> userOptional = getUser().stream().filter(user -> userId != null
				&& userId.equals(user.getId())).findFirst();
		if (userOptional.isPresent()) {
			return userOptional.get();
		} else {
			return null;
		}
	}

	public static Company getCompany(String companyId) {
		Optional<Company> companyOptional = getCompany().stream().filter(company -> companyId != null
				&& companyId.equals(company.getCode())).findFirst();
		if (companyOptional.isPresent()) {
			return companyOptional.get();
		} else {
			return null;
		}
	}

	public static List<String> getCompanyType() {
		return getCompany().stream().filter(company -> "0".equals(company.getType())).map(company -> company.getCode()).collect(Collectors.toList());
	}

}
