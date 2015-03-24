package com.project.stickynotes.customConnection;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class CustomMongoConnection {

	
		public DBCollection getMongoUserDetails(){
			
			String textUri = "mongodb://stickynotesuser:sticky@ds061200.mongolab.com:61200/stickynotesdb";
			MongoClientURI uri = new MongoClientURI(textUri);
			MongoClient mongo=null;
			try {
				mongo = new MongoClient(uri);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		DB db = mongo.getDB("stickynotesdb");
		DBCollection coll = db.getCollection("userDetails");
		
		return coll;
			
		}
		
		public DBCollection getMongoCollection2(){
			
			String textUri = "mongodb://stickynotesuser:sticky@ds049130.mongolab.com:49130/stickynotesdb";
			MongoClientURI uri = new MongoClientURI(textUri);
			MongoClient mongo=null;
			try {
				mongo = new MongoClient(uri);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		DB db = mongo.getDB("stickynotesdb");
		DBCollection coll = db.getCollection("userDetails");
		
		return coll;
			
		}
		
		public DBCollection getMongoCollection3(){
			
			String textUri = "mongodb://stickynotesuser:sticky@ds049130.mongolab.com:49130/stickynotesdb";
			MongoClientURI uri = new MongoClientURI(textUri);
			MongoClient mongo=null;
			try {
				mongo = new MongoClient(uri);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		DB db = mongo.getDB("stickynotesdb");
		DBCollection coll = db.getCollection("collection3");
		
		return coll;
			
		}
		
		public DBCollection getMongoCollection4(){
			
			String textUri = "mongodb://stickynotesuser:sticky@ds049130.mongolab.com:49130/stickynotesdb";
			MongoClientURI uri = new MongoClientURI(textUri);
			MongoClient mongo=null;
			try {
				mongo = new MongoClient(uri);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		DB db = mongo.getDB("stickynotesdb");
		DBCollection coll = db.getCollection("collection4");
		
		return coll;
			
		}
}
