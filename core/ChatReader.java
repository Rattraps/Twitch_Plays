package core;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;

public class ChatReader implements Runnable{
	
	private BufferedReader reader;
	
	private ImmutableList<Object> commands;
	
	private String[] regexCommands;
	
	private boolean interrupted;
	
	public ChatReader(InputStream chat, String[] regex){
		reader = new BufferedReader(new InputStreamReader(chat));
		interrupted = false;
		regexCommands = regex;
		
		commands = ImmutableList.of();
	}
	

	public void run() {
		if(regexCommands != null){
			try{
				String message;
		        while (!ChatConnection.isStopped && (message = reader.readLine()) != null) {
		        	while(interrupted);
		        	System.out.println(message);
		        	readLine(message);
		        }
			}
			catch(IOException e){
				System.err.println("Session disconnected");
				return;
			}	
		}
	}
	
	public ImmutableList<Object> getCommands(){
		interrupted = true;
		ImmutableList<Object> list = ImmutableList.copyOf(commands);
		commands = ImmutableList.of();
		interrupted = false;
		return list;
	}
	
	private void readLine(String message){
		String chatPattern = ":.*!([A-Za-z]*)@.*:([ 0-9A-Za-z]*)";
		
	    Pattern pattern = Pattern.compile(chatPattern);
	    Matcher matcher = pattern.matcher(message);
	    
	    if(matcher.find()){
	    	 String chatUser = matcher.group(1);
	         String chatMSG = matcher.group(2);
	         
	         chatMSG = chatMSG.toLowerCase();
	         
	         //TO FIX: replace with esc key?
	         if(chatUser.equals(ChatConnection.getUsername()) && chatMSG.equals("end session")){
	        	 ChatConnection.end();
	        	 return;
	         }
	         
	         for(String command : regexCommands){
	        	 pattern = Pattern.compile(command);
	        	 matcher = pattern.matcher(chatMSG);
	        	 
	        	 if(matcher.find()){
	        		 commands = ImmutableList
	        				 		.builder()
	        				 		.addAll(commands)
	        				 		.add(chatMSG)
	        				 		.build();
	        	 }
	         }
	    }
	    
	}

}
