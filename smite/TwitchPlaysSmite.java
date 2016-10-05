package smite;
import java.awt.AWTException;
import java.io.IOException;
import java.net.UnknownHostException;

import core.ChatConnection;
import core.ChatReader;

public class TwitchPlaysSmite {
	
	public static void main(String... args){
		
		String[] regex;
		regex = new String[14];
		regex[0] = "^ml((?!.+))";
		regex[1] = "^mr((?!.+))";
		regex[2] = "^a((?!.+))";
		regex[3] = "^b [0-9]*((?!.+))";
		regex[4] = "^d((?!.+))";
		regex[5] = "^l [0-9]*((?!.+))";
		regex[6] = "^r [0-9]*((?!.+))";
		regex[7] = "^s((?!.+))";
		regex[8] = "^u [0-9]*((?!.+))";
		regex[9] = "^w((?!.+))";
		regex[10] = "^1((?!.+))";
		regex[11] = "^2((?!.+))";
		regex[12] = "^3((?!.+))";
		regex[13] = "^4((?!.+))";
		
		
		
		try {
			
			ChatConnection.initialize("example-username", "example-password");
			ChatConnection.joinChannel("#examplechannel");
			ChatReader reader = ChatConnection.readChat(regex);
			SmiteRobot smite = new SmiteRobot(reader);
			ChatConnection.scheduleGameRobot(smite);
		} 
		catch (UnknownHostException e) {
			System.err.println("Could not connnect to Twitch");
			ChatConnection.end();
			return;
		} 
		catch (IOException e) {
			System.err.println("Trouble communicating with Twitch");
			ChatConnection.end();
			return;
		} catch (AWTException e) {
			System.err.println("Could not grab control of computer");
			ChatConnection.end();
			return;
		}
	}
}
