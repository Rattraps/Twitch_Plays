package core;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Timer;
import java.util.TimerTask;


/*Handles key and mouse clicking commands*/
public class ChatCommand {

	private String command;
	private int key;
	private boolean isMouse;
	private boolean isHeld;
	
	private ChatCommand oppositeCommand;
	
	private boolean keyBool;
	private Timer keyTimer;
	
	private Robot bot;
	
	public ChatCommand(Robot robot, String command, int key, boolean mouse, boolean held){
		this.bot = robot;
		this.command = command;
		this.key = key;
		this.isMouse = mouse;
		this.isHeld = held;
		
		if(held && !mouse){
			keyBool = false;
		}
	}
	
	public ChatCommand(Robot robot, String command, int key){
		this(robot, command, key, false, false);
	}
	
	public void setOpposite(ChatCommand opposite){
		oppositeCommand = opposite;
	}
	
	public String getCommand(){
		return command;
	}
	
	public void doCommand(){
		if(bot == null){
			return; 
		}
		
		if(isMouse){
			doMouseCommand();
		}
		else{
			doKeyCommand();
		}
	}
	
	private void doKeyCommand(){
		if(!isHeld){
			bot.keyPress(key);
			bot.keyRelease(key);
		}
		else{
			if(keyBool){
				cancelTimer();
			}
			else{
				keyTimer = new Timer();
				keyTimer.schedule(new HeldKey(), 10, 10);
				keyBool = true;
				if(oppositeCommand != null){
					oppositeCommand.cancelTimer();
				}
			}
		}
	}
	
	public void cancelTimer(){
		if(isHeld){
			if(keyTimer != null){
				keyTimer.cancel();
				keyTimer.purge();
			}

				
			keyBool = false;
				
			bot.keyRelease(key);
		}
		
	}
	
	private void doMouseCommand(){
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON2_MASK);
		bot.mouseRelease(InputEvent.BUTTON3_MASK);
		bot.mousePress(key);
		if(!isHeld){
			bot.mouseRelease(key);
		}
	}
	
	public boolean equals(Object o){
		return (o instanceof ChatCommand && ((ChatCommand)o).command.equals(command));
	}
	
	private class HeldKey extends TimerTask{

		@Override
		public void run() {
			bot.keyPress(key);	
		}
		
	}
}
