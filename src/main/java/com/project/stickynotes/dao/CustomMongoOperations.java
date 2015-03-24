package com.project.stickynotes.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.project.stickynotes.customConnection.CustomMongoConnection;
import com.project.stickynotes.forms.LoginForm;

public class CustomMongoOperations {

	DBCollection coll;
	DBCollection coll2;
	DBCollection coll3;
	DBCollection coll4;

	public CustomMongoOperations() {
		CustomMongoConnection cust1 = new CustomMongoConnection();
		coll = cust1.getMongoUserDetails();
		coll2 = cust1.getMongoCollection2();
		coll3 = cust1.getMongoCollection3();
		coll4 = cust1.getMongoCollection4();
	}

	public int addUser(LoginForm user) {

		int flag = checkUser(user.getAppUsername());

		if (flag == 0) {
			BasicDBObject doc = new BasicDBObject();
			doc.put("appUsername", user.getAppUsername());
			doc.put("appPassword", user.getAppPassword());
			coll.insert(doc);
		}
		return flag;

	}

	public int checkUser(String appUsername) {
		int flag = 0;
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("appUsername", appUsername);
		DBCursor cursor = coll.find(searchQuery);
		//
		String mappUsername = null;
		String mappPassword = null;

		LoginForm mobj = new LoginForm();
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();

			mappUsername = obj.get("appUsername").toString();
			mappPassword = obj.get("appPassword").toString();

			mobj.setAppUsername(mappUsername);
			mobj.setAppPassword(mappPassword);
			flag = 1;
		}
		return flag;
	}

	public LoginForm getUser(String appUsername, String appPassword) {

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("appUsername", appUsername);
		DBCursor cursor = coll.find(searchQuery);
		String mappUsername = null;
		String mappPassword = null;

		LoginForm mobj = new LoginForm();

		while (cursor.hasNext()) {
			System.out.println("inside cursor");

			DBObject obj = cursor.next();

			mappUsername = obj.get("appUsername").toString();
			mappPassword = obj.get("appPassword").toString();

			mobj.setAppUsername(mappUsername);
			mobj.setAppPassword(mappPassword);
		}
		if (appUsername.equals(mappUsername)) {
			if (appPassword.equals(mappPassword)) {

			} else {
				mobj = null;
			}
		} else {
			mobj = null;
		}
		return mobj;
	}
}
