package it.racinghub.fe.command;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBESalvaTeam;

/**

 */
public class SetTeamCommand extends BotCommandExtended {

    public SetTeamCommand() {
        super(RHCostants.COMMAND_SETTEAM, "Comando per modificare i dettagli Team");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETTEAM);

		ObjectNode sessDatiTeamTmp = new ObjectMapper().createObjectNode();
		try {
			Utils.checkPilotaInSession(user, botSession, absSender);
			JsonNode objPilota = Utils.leggiSessionVar(botSession, RHCostants.KEYSESSION_PILOTA);
			JsonNode objTeam = Utils.leggiJsonNodeObj(objPilota, "team");
			
			String jsonString = new ObjectMapper().writeValueAsString(objTeam);
			sessDatiTeamTmp = new ObjectMapper().readValue(jsonString, ObjectNode.class);
		}
		catch (Exception e) {}
		
		for(String x : RHCostants.ARRAYVAR_TEAM)
			sessDatiTeamTmp.set(x, null);
		botSession.setAttribute(RHCostants.KEYSESSION_TEAM_TMP, sessDatiTeamTmp);
		
		ObjectNode sessStatusVarTeam = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_TEAM)
			sessStatusVarTeam.put(x, false);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_TEAM, sessStatusVarTeam);

		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_REQ_TEAM_DATA);
		
		chiediDati(absSender, message, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, Message message, User user, Session botSession) {
		
		ObjectNode sessStatusVarTeam = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_TEAM);
		for(int i=0; i<RHCostants.ARRAYVAR_TEAM.length; i++) {
			
			if(!(sessStatusVarTeam).get(RHCostants.ARRAYVAR_TEAM[i]).asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_TEAM[i]);
				return;
			}
		}
		
		eseguiComando(absSender, message, user, botSession);
	}
	
	public static void setDatiInSessione(DefaultAbsSender absSender, Message message, User user, Session botSession) {

		ObjectNode sessStatusVarTeam = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_TEAM);
		for(int i=0; i<RHCostants.ARRAYVAR_TEAM.length; i++) {
			
			if(!(sessStatusVarTeam).get(RHCostants.ARRAYVAR_TEAM[i]).asBoolean()){
				String message_text = message.getText();
				ObjectNode sessDatiTeamTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_TEAM_TMP);
				sessDatiTeamTmp.put(RHCostants.ARRAYVAR_TEAM[i], message_text);
				sessStatusVarTeam.put(RHCostants.ARRAYVAR_TEAM[i], true);
				
				chiediDati(absSender, message, user, botSession);
				
				return;
			}
		}
		
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, Message message, User user, Session botSession) {
		ObjectNode responseApi = Utils.getResponseApi(RHBESalvaTeam.execute(RHBESalvaTeam.preparaRequest(user, botSession)),
				absSender);
		//if(Objects.nonNull(responseApi);
		new ViewTeamCommand().execute(absSender, message, user, null, botSession, null);
	}
	
}
