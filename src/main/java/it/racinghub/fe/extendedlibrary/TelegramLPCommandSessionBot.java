package it.racinghub.fe.extendedlibrary;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.ChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatSessionContext;

import it.racinghub.fe.exceptions.RacingHubBotException;

/**
 * This class adds command and session functionality to the TelegramLongPollingBot
 *
 */
public abstract class TelegramLPCommandSessionBot extends TelegramLongPollingBot implements ICommandRegistry {

    DefaultSessionManager sessionManager;

    ChatIdConverter chatIdConverter;
    
    CommandRegistry commandRegistry;

    
    /**
     * If this is used getBotToken has to be overridden in order to return the bot token!
     * @deprecated Overwriting the getBotToken() method is deprecated. Use the constructor instead
     */
    @Deprecated
    public TelegramLPCommandSessionBot() {
        super();
    }
    
    /**
     * Creates a TelegramLongPollingCommandBot with custom options and allowing commands with
     * usernames
     * Use ICommandRegistry's methods on this bot to register commands
     *
     * @param options     Bot options
     */
    public TelegramLPCommandSessionBot(DefaultBotOptions options, String botToken) {
        this(new DefaultChatIdConverter(), options, true, botToken);
    }


    public TelegramLPCommandSessionBot(String botToken){
        this(new DefaultChatIdConverter(), new DefaultBotOptions(), true, botToken);
    }

    public TelegramLPCommandSessionBot(ChatIdConverter chatIdConverter, String botToken){
        this(chatIdConverter, new DefaultBotOptions(), true, botToken);
    }

    public TelegramLPCommandSessionBot(ChatIdConverter chatIdConverter, DefaultBotOptions defaultBotOptions, boolean allowCommandsWithUsername, String botToken){
        super(defaultBotOptions, botToken);
        this.setSessionManager(new DefaultSessionManager());
        this.setChatIdConverter(chatIdConverter);
        AbstractSessionDAO sessionDAO = (AbstractSessionDAO) sessionManager.getSessionDAO();
        sessionDAO.setSessionIdGenerator(chatIdConverter);
        this.commandRegistry = new CommandRegistry(allowCommandsWithUsername, this::getBotUsername);
    }


    public void setSessionManager(DefaultSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setChatIdConverter(ChatIdConverter chatIdConverter) {
        this.chatIdConverter = chatIdConverter;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Optional<Session> chatSession;
        Message message;
        if (update.hasMessage()) {
            message = update.getMessage();
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage() instanceof Message) {
                message = (Message) update.getCallbackQuery().getMessage();
            } else {
                return;
            }
        } else {
            chatSession = Optional.empty();
            onUpdateReceived(update, chatSession.get());
            return;
        }
        chatIdConverter.setSessionId(message.getChatId());
        chatSession = this.getSession(message);
        onUpdateReceived(update, chatSession.get());
    }

    public Optional<Session> getSession(Message message){
        try {
        	Session s = sessionManager.getSession(chatIdConverter);
        	s.touch();
            return Optional.of(s);
        } catch (UnknownSessionException | ExpiredSessionException e) {
            SessionContext botSession = new DefaultChatSessionContext(message.getChatId(), message.getFrom().getUserName());
            return Optional.of(sessionManager.start(botSession));
        }
    }

    public final void onUpdateReceived(Update update, Session botSession) {
        try {
    	if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand() && !filter(message)) {
                if (!commandRegistry.executeCommand(this, message)) {
                    //we have received a not registered command, handle it as invalid
                    processInvalidCommandUpdate(update, botSession);
                }
                return;
            }
        }
        else if(update.hasCallbackQuery() && 
        		Objects.nonNull(update.getCallbackQuery().getData()) && 
        		update.getCallbackQuery().getData().startsWith("/")){
        	String cbqCommand = update.getCallbackQuery().getData();
        	
        	List<MessageEntity> listME = new ArrayList<MessageEntity>();
        	MessageEntity me = new MessageEntity(EntityType.BOTCOMMAND, 0, cbqCommand.length());
        	listME.add(me);
        	
        	Message message = new Message();
        	message.setText(cbqCommand);
        	message.setEntities(listME);
        	message.setFrom(update.getCallbackQuery().getFrom());
        	message.setDate(update.getCallbackQuery().getMessage().getDate());
        	message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        	
        	GetChat getChat = new GetChat();
        	getChat.setChatId(update.getCallbackQuery().getMessage().getChatId());
    	    try {
    	        Chat c = execute(getChat);
    	        message.setChat(c);
    	    } catch (TelegramApiException e) {}
        	
        	if(message.isCommand()) {
        		update.setMessage(message);
        		onUpdateReceived(update, botSession);
        		return;
        	}
        }
        processNonCommandUpdate(update, botSession);
        }
        catch(Exception e) {
			throw new RacingHubBotException(this, update.getMessage(), e);
        }
    }
    
    /**
     * @return Bot username
     */
    @Override
    public abstract String getBotUsername();
    
    /**
     * Process all updates, that are not commands.
     *
     * @param update the update
     * @warning Commands that have valid syntax but are not registered on this bot,
     * won't be forwarded to this method <b>if a default action is present</b>.
     */
    public abstract void processNonCommandUpdate(Update update, Session botSession);

    /**
     * This method is called when user sends a not registered command. By default it will just call processNonCommandUpdate(),
     * override it in your implementation if you want your bot to do other things, such as sending an error message
     *
     * @param update Received update from Telegram
     */
    private void processInvalidCommandUpdate(Update update, Session botSession) {
        processNonCommandUpdate(update, botSession);
    }

    /**
     * Override this function in your bot implementation to filter messages with commands
     * <p>
     * For example, if you want to prevent commands execution incoming from group chat:
     * #
     * # return !message.getChat().isGroupChat();
     * #
     *
     * @param message Received message
     * @return true if the message must be ignored by the command bot and treated as a non command message,
     * false otherwise
     * @note Default implementation doesn't filter anything
     */
    private boolean filter(Message message){
        return false;
    }

    @Override
    public final boolean register(IBotCommand botCommand) {
        return commandRegistry.register(botCommand);
    }

    @Override
    public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
        return commandRegistry.registerAll(botCommands);
    }

    @Override
    public final boolean deregister(IBotCommand botCommand) {
        return commandRegistry.deregister(botCommand);
    }

    @Override
    public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
        return commandRegistry.deregisterAll(botCommands);
    }

    @Override
    public final Collection<IBotCommand> getRegisteredCommands() {
        return commandRegistry.getRegisteredCommands();
    }

    @Override
    public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
        commandRegistry.registerDefaultAction(defaultConsumer);
    }

    @Override
    public final IBotCommand getRegisteredCommand(String commandIdentifier) {
        return commandRegistry.getRegisteredCommand(commandIdentifier);
    }

}
