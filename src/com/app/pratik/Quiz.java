package com.app.pratik;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;





public class Quiz {
  public static void main(String args[])throws Exception {
	  List<Question> questions=null;
	  int score=0;
	  Connection connection=null;
	  Statement statement=null;
	  try {
		  
		  String url="jdbc:mysql://localhost:3306/miniproject";
		  String user="patya";
		  String pass="Pratik@123";
		  connection=DriverManager.getConnection(url,user,pass);
		  questions=loadQuestions(connection);
		  Scanner sc=new Scanner(System.in);
		  while(true) {
		  System.out.println("1) Register as New User"+"  2)Login and Start the Quiz");
		  System.out.println("Enter option");
		  int option=sc.nextInt();
		 
		  if(option==1) {
			 
			  System.out.println("Enter Username:");
			  String name=sc.next();
			  System.out.println("Enter Password:");
			  String password=sc.next();
			  System.out.println("Enter Your First Name:");
			  String firstName=sc.next();
			  System.out.println("Enter Your Last Name:");
			  String lastName=sc.next();
			  System.out.println("Enter email:");
			  String email=sc.next();
			  System.out.println("Enter Contact Number:");
			  long contactNum=sc.nextLong();
			  String isAttempted="N";
			 
			  
			  
		 PreparedStatement psmt=connection.prepareStatement("insert into user(username,password,first_name,last_name,email,contact,s_attempted,score) values(?,?,?,?,?,?,?,?)");
			  
			  psmt.setString(1, name);
			  psmt.setString(2,password);               //omkar@123
			  psmt.setString(3,firstName);
			  psmt.setString(4,lastName);
			  psmt.setString(5,email);
			  psmt.setLong(6,contactNum);
			  psmt.setString(7, isAttempted);
			  psmt.setInt(8, score);
			  psmt.executeUpdate();
			  System.out.println("Registered Successfully.....");
			  System.out.println("Kindly Login and Start the Quiz");
		  }
			  else if(option==2) {
			  System.out.println("Enter Your Username");
			  String uName=sc.next();
			  System.out.println("Enter Password");
			  String uPassword=sc.next();
			statement=connection.createStatement();
			
	ResultSet rs=statement.executeQuery("select username,password from user");
	          boolean flag=false;
		while(rs.next()) {
			if(uName.equals(rs.getString("username"))&&uPassword.equals(rs.getString("password"))) {
				flag=true;	
				
				}
			}
			  if(!flag) {
				  System.out.println("Incorrect Username or Password");
			  }
			  else {
				  System.out.println("Logged in Successfully");
				  statement=connection.createStatement();
					ResultSet  rst=statement.executeQuery("select * from user where username='"+uName+"';");
					if(rst.next()) {
				  while(true) {
				  System.out.println("Enter Option");
				  System.out.println("1) Start the Quiz  2)View Score if Already Attempted");
				  int option2=sc.nextInt();
				
				  if(option2==1) {	
					  
                      if(rst.getString("s_attempted").equals("Y")) {
						  System.out.println("You have already attempted the quiz please view the score");
						  continue;
					  }
					 else {
					for(Question question:questions) {
						System.out.println(question.getQuestion());
						System.out.println("Option A:"+question.getOption_A());
						System.out.println("Option B:"+question.getOption_B());
						System.out.println("Option C:"+question.getOption_C());
						System.out.println("Option D:"+question.getOption_D());
						System.out.println("Enter Correct Option:");
						String userAns=sc.next();
						if(userAns.equalsIgnoreCase(question.getCorrectAns())) {
							score++;
						}
						
					}
					System.out.println("Your Score is:"+score);
					updateScore(connection,score,rst.getString("username"));
					}  
				  
					  break;
				  
				  
				  } else if(option2==2) {
					  if(rst.getString("s_attempted").equals("N")) {
						  System.out.println("You have not Attempted the Quiz");
						  continue;
					  }
					  else {
						  System.out.println("Your Score is:"+rst.getInt("score"));
			
					  }
					 
				  }
				  break;
				  
				   
				  
				  }
				  
					 	 
					 
					 }
					    
					 
					  }
			  break;
				  
			  }  
	  
//		  
//		  con.close();
	  } 
		  
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  
	  
	  
  }
  
    private static void updateScore(Connection connection,int score,String username) {
    	PreparedStatement statement=null;
    	try {
    		statement=connection.prepareStatement("update user set score=?,s_attempted=? where username=?;");
    		statement.setInt(1, score);
    		statement.setString(2, "Y");
    		statement.setString(3, username);
    		statement.executeUpdate();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
  
  
  
  
	 private static List<Question> loadQuestions(Connection connection) {
	        List<Question> questions=new ArrayList<>();
	        ResultSet resultSet=null;
	        Statement statement=null;
	        Question question=null;
	        try {
	            statement=connection.createStatement();
	            resultSet=statement.executeQuery("select * from question;");
	            while(resultSet.next()){
	                question=new Question();
	                question.setId(resultSet.getInt(1));
	                question.setQuestion(resultSet.getString(2));
	                question.setOption_A(resultSet.getString(3));
	                question.setOption_B(resultSet.getString(4));
	                question.setOption_C(resultSet.getString(5));
	                question.setOption_D(resultSet.getString(6));
	                question.setCorrectAns(resultSet.getString(7));
	                questions.add(question);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return questions;
	    }
  
  
  
  
  
  
}
