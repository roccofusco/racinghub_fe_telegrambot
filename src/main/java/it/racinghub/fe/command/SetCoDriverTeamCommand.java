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
import it.racinghub.fe.rest.external.RHBERecuperaPilota;
import it.racinghub.fe.rest.external.RHBESalvaPilota;

/**

 */
public class SetCoDriverTeamCommand extends BotCommandExtended {

    public SetCoDriverTeamCommand() {
        super(RHCostants.COMMAND_SETCODRIVERTEAM, "Comando per modificare i dettagli Pilota");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETCODRIVERTEAM);

		Utils.checkPilotaInSession(user, botSession, absSender);
		
		ObjectNode sessDatiCoPilotaTeamTmp = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_CODRIVERTEAM)
			sessDatiCoPilotaTeamTmp.set(x, null);
		botSession.setAttribute(RHCostants.KEYSESSION_COPILOTATEAM_TMP, sessDatiCoPilotaTeamTmp);
		
		ObjectNode sessStatusVarCoPilotaTeam = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_CODRIVERTEAM)
			sessStatusVarCoPilotaTeam.put(x, false);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_COPILOTATEAM, sessStatusVarCoPilotaTeam);

		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_REQ_CODRIVERTEAM_DATA);
		
		chiediDati(absSender, message, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, Message message, User user, Session botSession) {
		
		ObjectNode sessStatusVarCoPilotaTeam = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_COPILOTATEAM);
		for(int i=0; i<RHCostants.ARRAYVAR_CODRIVERTEAM.length; i++) {
			
			if(!(sessStatusVarCoPilotaTeam).get(RHCostants.ARRAYVAR_CODRIVERTEAM[i]).asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_CODRIVERTEAM[i]);
				return;
			}
		}
		
		eseguiComando(absSender, message, user, botSession);
	}
	
	public static void setDatiInSessione(DefaultAbsSender absSender, Message message, User user, Session botSession) {

		ObjectNode sessStatusVarCoPilotaTeam = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_COPILOTATEAM);
		for(int i=0; i<RHCostants.ARRAYVAR_CODRIVERTEAM.length; i++) {
			
			if(!(sessStatusVarCoPilotaTeam).get(RHCostants.ARRAYVAR_CODRIVERTEAM[i]).asBoolean()){
				String message_text = message.getText();
				ObjectNode sessDatiCoPilotaTeamTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_COPILOTATEAM_TMP);
				sessDatiCoPilotaTeamTmp.put(RHCostants.ARRAYVAR_CODRIVERTEAM[i], message_text);
				sessStatusVarCoPilotaTeam.put(RHCostants.ARRAYVAR_CODRIVERTEAM[i], true);
				
				chiediDati(absSender, message, user, botSession);
				
				return;
			}
		}
		
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, Message message, User user, Session botSession) {
		ObjectNode sessDatiCoPilotaTeamTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_COPILOTATEAM_TMP);
		
		String DidCoDriver = Utils.leggiJsonNodeVar(sessDatiCoPilotaTeamTmp, "didCodriver");
		
		JsonNode objPilota = Utils.leggiSessionVar(botSession, RHCostants.KEYSESSION_PILOTA);
		JsonNode objTeam = Utils.leggiJsonNodeObj(objPilota, "team");
		String TidTeam = Utils.leggiJsonNodeVar(objTeam, "idTeam");
		
		ObjectNode responseApi = Utils.getResponseApi(RHBESalvaPilota.executeCoDriver(
															RHBESalvaPilota.preparaRequestCoDriver(DidCoDriver, TidTeam)),
															absSender);
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_ESITO_SETCODRIVERTEAM_OK);
		
		//if(Objects.nonNull(responseApi);
		new ViewTeamCommand().execute(absSender, message, user, null, botSession, null);
	}
	
}
