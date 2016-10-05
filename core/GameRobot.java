package core;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.TimerTask;

import com.google.common.collect.ImmutableList;



public abstract class GameRobot extends TimerTask{
	
	private Robot bot;

	private ArrayDeque<String> commands;
	private ChatReader chat;
	
	private ArrayList<ChatCommand> botCommands; 
	
	
	
	public GameRobot(ChatReader reader) throws AWTException{
		chat = reader;
		bot = new Robot();
		commands = new ArrayDeque<String>();
		botCommands = new ArrayList<ChatCommand>();
		
		setBotCommands();
		
		readCommands();
	}
	
	//set list of commands + subsequent robot functions
	protected abstract void setBotCommands();
	
	//check and handle conversion from generic to string
	private void readCommands(){
		ImmutableList<Object> list = chat.getCommands();
		if(list == null){
			return;
		}
		
		for(Object element : list){
			if(element instanceof String){
				commands.add((String)element);
			}
		}
	}
	
	public void end(){
		for(ChatCommand command : botCommands){
			command.cancelTimer();
		}
	}

	public void run() {
		if(commands.isEmpty()){
			readCommands();
		}
		else{
			String command = commands.remove();
			processCommand(command);
		}
	}
	
	protected void processCommand(String command){
		ChatCommand temp = new ChatCommand(null, command, 0);
		int indexOf = botCommands.indexOf(temp);
		
		if(indexOf >= 0){
			ChatCommand botCommand = botCommands.get(indexOf);
			botCommand.doCommand();
			
		}
	}
	
	protected ArrayList<ChatCommand> getBotCommands(){
		return botCommands;
	}
	
	protected Robot getBot(){
		return bot;
	}
	
	
}
