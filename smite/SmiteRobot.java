package smite;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import core.ChatCommand;
import core.ChatReader;
import core.GameRobot;

public class SmiteRobot extends GameRobot{
	
	
	
	
	public SmiteRobot(ChatReader reader) throws AWTException{
		super(reader);
	}
	
	@Override
	protected void processCommand(String command){
		if(getBot() != null){
			if(command.length() > 2){
				processCameraCommand(command);
			}
			else{
				super.processCommand(command);
			}
		}
		
	}
	
	
	private void processCameraCommand(String command){

		String numString = command.substring(2);
		int number = Integer.parseInt(numString);
			
		if(command.charAt(0) == 'l'){
			getBot().mouseMove(MouseInfo.getPointerInfo().getLocation().x-number, MouseInfo.getPointerInfo().getLocation().y);
		}
		else if(command.charAt(0) == 'r'){
			getBot().mouseMove(MouseInfo.getPointerInfo().getLocation().x+number, MouseInfo.getPointerInfo().getLocation().y);
		}
		else if(command.charAt(0) == 'u'){
			getBot().mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y-number);
		}
		else if(command.charAt(0) == 'b'){
			getBot().mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y+number);
		}	
	}

	@Override
	protected void setBotCommands() {
		ArrayList<ChatCommand> commands = getBotCommands();
		
		ChatCommand w = new ChatCommand(getBot(), "w", KeyEvent.VK_W, false, true);
		ChatCommand s = new ChatCommand(getBot(), "s", KeyEvent.VK_S, false, true);
		w.setOpposite(s);
		s.setOpposite(w);
		
		ChatCommand a = new ChatCommand(getBot(), "a", KeyEvent.VK_A, false, true);
		ChatCommand d = new ChatCommand(getBot(), "d", KeyEvent.VK_D, false, true);
		d.setOpposite(a);
		a.setOpposite(d);
		
		ChatCommand one = new ChatCommand(getBot(), "1", KeyEvent.VK_1, false, false);
		ChatCommand two = new ChatCommand(getBot(), "2", KeyEvent.VK_2, false, false);
		ChatCommand three = new ChatCommand(getBot(), "3", KeyEvent.VK_3, false, false);
		ChatCommand four = new ChatCommand(getBot(), "4", KeyEvent.VK_4, false, false);
		
		ChatCommand mouseRight = new ChatCommand(getBot(), "ml", InputEvent.BUTTON1_MASK, true, true);
		ChatCommand mouseLeft = new ChatCommand(getBot(), "mr", InputEvent.BUTTON3_MASK, true, false);
		
		commands.add(w);
		commands.add(s);
		commands.add(d);
		commands.add(a);
		commands.add(one);
		commands.add(two);
		commands.add(three);
		commands.add(four);
		commands.add(mouseRight);
		commands.add(mouseLeft);
	}
}
