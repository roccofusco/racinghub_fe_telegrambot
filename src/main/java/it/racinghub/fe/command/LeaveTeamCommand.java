package it.racinghub.fe.command;

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
public class LeaveTeamCommand extends BotCommandExtended {

    public LeaveTeamCommand() {
        super(RHCostants.COMMAND_LEAVETEAM, "Comando per visualizzare i dettagli Team");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_LEAVETEAM);

		Utils.checkPilotaInSession(user, botSession, absSender);
		try {	
			ObjectNode pilota = Utils.leggiSessionVar(botSession, RHCostants.KEYSESSION_PILOTA);
			String jsonString = new ObjectMapper().writeValueAsString(pilota);
			ObjectNode sessDatiPilotaTmp = new ObjectMapper().readValue(jsonString, ObjectNode.class);
	
			sessDatiPilotaTmp.set("team", null);
	
			botSession.setAttribute(RHCostants.KEYSESSION_PILOTA_TMP, sessDatiPilotaTmp);
		}
		catch (Exception e) {}
		
		ObjectNode responseApi = Utils.getResponseApi(RHBESalvaPilota.execute(RHBESalvaPilota.preparaRequest(user, botSession)),
				absSender);
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_ESITO_LEAVETEAM_OK);
		
		new ViewTeamCommand().execute(absSender, message, user, null, botSession, null);

	}
	
}
