package core;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;

public class ChatConnection {
	
	public static boolean isStopped = false;
	
	private final static String server = "irc.chat.twitch.tv";
	private final static int port = 6667;
	
	private static String username = "";
	
	private static String channel = null;
	
	private static Socket socket;
	private static BufferedWriter chatWriter;
	private static ChatReader chatReader;
	
	private static Thread chat;
	private static Timer botTimer;
	
	private static GameRobot game;
	
	public static void initialize(String username, String password) throws UnknownHostException, IOException{
		socket = new Socket(server, port);
		chatWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		chatWriter.write("PASS " + password + "\r\n");
		chatWriter.write("NICK " + username + "\r\n");
		chatWriter.flush( );
		ChatConnection.username = username;
	}
	
	public static void joinChannel(String name) throws IOException{
		if(name != null && socket != null){
			if(channel != null){
				leaveChannel();
			}
			
			channel = name;
			chatWriter.write("JOIN " + channel + "\r\n");
	        chatWriter.flush();
		}
	}
	
	public static void leaveChannel() throws IOException{
		if(channel != null && socket != null){
			chatWriter.write("PART " + channel + "\r\n");
	        chatWriter.flush();
	        channel = null;	
		}
	}
	
	public static ChatReader readChat(String[] regex) throws IOException{
		chatReader = new ChatReader(socket.getInputStream(), regex);
	    chat = new Thread(chatReader);
	    chat.start();
	        
	    return chatReader;	
	}
	
	public static void scheduleGameRobot(GameRobot robot){
		if(game == null){
			game = robot;
			botTimer = new Timer();
			botTimer.schedule(game, 10, 10);
		}
		else{
			System.err.println("Already running a game robot, shutting down and replacing!");
			game.end();
			botTimer.cancel();
			botTimer.purge();
			game = null;
			scheduleGameRobot(robot);
		}
	}
	
	public static String getUsername(){
		return username;
	}

	public static void end(){
		try{
			System.out.println("Shutting down.");
			isStopped = true;
			if(game != null){
				game.end();
				botTimer.cancel();	
				botTimer.purge();
			}
			if(socket != null){
				socket.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Error in shutting down.");
		}
		
	}
}
