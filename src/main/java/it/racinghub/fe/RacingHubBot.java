package it.racinghub.fe;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;

import it.racinghub.fe.command.InsResultCommand;
import it.racinghub.fe.command.SearchMyChampCommand;
import it.racinghub.fe.command.SetChampCommand;
import it.racinghub.fe.command.SetCoDriverTeamCommand;
import it.racinghub.fe.command.SetDriverCommand;
import it.racinghub.fe.command.SetEventCommand;
import it.racinghub.fe.command.SetRegisteredCommand;
import it.racinghub.fe.command.SetSessionCommand;
import it.racinghub.fe.command.SetTeamCommand;
import it.racinghub.fe.command.ViewChampStandingsCommand;
import it.racinghub.fe.command.ViewRegisteredCommand;
import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.TelegramLPCommandSessionBot;
import it.racinghub.fe.helper.Utils;

public class RacingHubBot extends TelegramLPCommandSessionBot {

//    private static final Logger log = LoggerFactory.getLogger(DefaultBotSession.class);

    /**
     * Constructor.
     */
    public RacingHubBot(String botUsername) {
        super(botUsername);

        registerAll(RHCostants.ARRAYCOMMAND_ACTIVATE);
        
        registerDefaultAction((absSender, message) -> {
        	Utils.inviaMessaggio(this, message.getChatId(), "Il comando '" + message.getText() + "' non è previsto per questo bot.");
        });
    }

	@Override
	public String getBotUsername() {
		return RHCostants.BOTUSERNAME;
	}
	
	@Override
	public String getBotToken() {
		return RHCostants.BOTTOKEN;
	}

	@Override
	public void processNonCommandUpdate(Update update, Session botSession) {

		String currentCommand = Utils.readCommandInSession(botSession);
		
		//Lettura text
		if(update.hasMessage()){
			switch(currentCommand) {
				case RHCostants.COMMAND_SETDRIVER:
					SetDriverCommand.setDatiInSessione(this, update.getMessage(), update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETTEAM:
					SetTeamCommand.setDatiInSessione(this, update.getMessage(), update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETCODRIVERTEAM:
					SetCoDriverTeamCommand.setDatiInSessione(this, update.getMessage(), update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SEARCHMYCHAMP:
					SearchMyChampCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_INSRESULT:
					InsResultCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETCHAMP:
					SetChampCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETEVENT:
					SetEventCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETSESSION:
					SetSessionCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_VIEWCHAMPSTANDINGS:
					ViewChampStandingsCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_VIEWREGISTERED:
					ViewRegisteredCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETREGISTERED:
					SetRegisteredCommand.setDatiInSessione(this, update, update.getMessage().getFrom(), botSession);
					break;
			}
		}
		
		//Lettura data callback
		if(update.hasCallbackQuery()){
			switch(currentCommand) {
				case RHCostants.COMMAND_SEARCHMYCHAMP:
					SearchMyChampCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_INSRESULT:
					InsResultCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETCHAMP:
					SetChampCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETEVENT:
					SetEventCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETSESSION:
					SetSessionCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_VIEWCHAMPSTANDINGS:
					ViewChampStandingsCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_VIEWREGISTERED:
					ViewRegisteredCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
				case RHCostants.COMMAND_SETREGISTERED:
					SetRegisteredCommand.setDatiInSessione(this, update, update.getCallbackQuery().getFrom(), botSession);
					break;
			}
		}
		
	}


}
