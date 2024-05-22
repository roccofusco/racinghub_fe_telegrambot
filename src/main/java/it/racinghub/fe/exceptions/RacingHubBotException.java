package it.racinghub.fe.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.helper.Utils;



public class RacingHubBotException extends RuntimeException {

	private static final long serialVersionUID = 8669777943379510681L;

	private static final Logger log = LoggerFactory.getLogger(RacingHubBotException.class);

    public RacingHubBotException() {
    	super();
    }

    public RacingHubBotException(AbsSender sender, Message message, Throwable cause) {
    	super();
    	cause.printStackTrace();
    	log.error(cause.getMessage());
    	Utils.inviaMessaggio(sender, message.getChatId(), "Ops... c'è stato un problema. Riprova."+RHCostants.A_CAPO+cause.getMessage());

    }
    
    public RacingHubBotException(String message) {
        super(message);
    }


    public RacingHubBotException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
