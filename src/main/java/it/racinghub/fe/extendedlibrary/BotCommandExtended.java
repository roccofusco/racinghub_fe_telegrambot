package it.racinghub.fe.extendedlibrary;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class BotCommandExtended extends BotCommand{

	public BotCommandExtended(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}
	
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
    	Session botSession = null; 
    	if(absSender instanceof TelegramLPCommandSessionBot)
    		botSession = ((TelegramLPCommandSessionBot) absSender).getSession(message).get();
    	execute((DefaultAbsSender)absSender, message, message.getFrom(), message.getChat(), botSession, arguments);
	}

	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		
	}
    
	public abstract void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments);

}
