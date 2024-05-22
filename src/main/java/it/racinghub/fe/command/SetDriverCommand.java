package it.racinghub.fe.command;

import java.util.Objects;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBESalvaPilota;

/**

 */
public class SetDriverCommand extends BotCommandExtended {

    public SetDriverCommand() {
        super(RHCostants.COMMAND_SETDRIVER, "Comando per modificare i dettagli Pilota");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETDRIVER);

		ObjectNode sessDatiPilotaTmp = new ObjectMapper().createObjectNode();
		try {
			Utils.checkPilotaInSession(user, botSession, absSender);

			if(!Objects.isNull((ObjectNode)botSession.getAttribute(RHCostants.KEYSESSION_PILOTA))) {
				String jsonString = new ObjectMapper().writeValueAsString((ObjectNode)botSession.getAttribute(RHCostants.KEYSESSION_PILOTA));
				sessDatiPilotaTmp = new ObjectMapper().readValue(jsonString, ObjectNode.class);
			}
			
		}
		catch (Exception e) {}
		
		for(String x : RHCostants.ARRAYVAR_PILOTA)
			sessDatiPilotaTmp.set(x, null);
		botSession.setAttribute(RHCostants.KEYSESSION_PILOTA_TMP, sessDatiPilotaTmp);
		
		ObjectNode sessStatusVarPilota = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_PILOTA)
			sessStatusVarPilota.put(x, false);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_PILOTA, sessStatusVarPilota);

		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_REQ_DRIVER_DATA);
		
		chiediDati(absSender, message, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, Message message, User user, Session botSession) {
		
		ObjectNode sessStatusVarPilota = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_PILOTA);
		for(int i=0; i<RHCostants.ARRAYVAR_PILOTA.length; i++) {
			
			if(!(sessStatusVarPilota).get(RHCostants.ARRAYVAR_PILOTA[i]).asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_DRIVER[i]);
				return;
			}
		}
		
		eseguiComando(absSender, message, user, botSession);
	}
	
	public static void setDatiInSessione(DefaultAbsSender absSender, Message message, User user, Session botSession) {

		ObjectNode sessStatusVarPilota = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_PILOTA);
		for(int i=0; i<RHCostants.ARRAYVAR_PILOTA.length; i++) {
			
			if(!(sessStatusVarPilota).get(RHCostants.ARRAYVAR_PILOTA[i]).asBoolean()){
				String message_text = message.getText();
				ObjectNode sessDatiPilotaTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_PILOTA_TMP);
				sessDatiPilotaTmp.put(RHCostants.ARRAYVAR_PILOTA[i], message_text);
				sessStatusVarPilota.put(RHCostants.ARRAYVAR_PILOTA[i], true);
				
				chiediDati(absSender, message, user, botSession);
				
				return;
			}
		}
		
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, Message message, User user, Session botSession) {
		ObjectNode responseApi = Utils.getResponseApi(RHBESalvaPilota.execute(RHBESalvaPilota.preparaRequest(user, botSession)),
				absSender);
		//if(Objects.nonNull(responseApi);
		new ViewDriverCommand().execute(absSender, message, user, null, botSession, null);
	}
	
}
