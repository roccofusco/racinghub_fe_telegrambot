package it.racinghub.fe.command;

import java.util.Objects;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBECampionato;

/**

 */
public class SetChampCommand extends BotCommandExtended {

    public SetChampCommand() {
        super(RHCostants.COMMAND_SETCHAMP, "Comando per modificare i dettagli Campionato");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETCHAMP);

		String idCampionatoSel = null;

//		if(Objects.isNull(arguments) || arguments.length<1) {
//			Utils.inviaMessaggio(absSender, user.getId(), 
//					"C.ID Campionato assente! Riprova.");
//			return;
//		}
		if(Objects.nonNull(arguments) && arguments.length>0) {
			idCampionatoSel = arguments[0];
		}
		
		ObjectNode sessDatiChampTmp = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_CHAMP)
			sessDatiChampTmp.set(x, null);
		
		sessDatiChampTmp.put("idCampionato", idCampionatoSel);
		botSession.setAttribute(RHCostants.KEYSESSION_CHAMP_TMP, sessDatiChampTmp);
		
		ObjectNode sessStatusVarChamp= new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_CHAMP)
			sessStatusVarChamp.put(x, false);
		
		sessStatusVarChamp.put("idCampionato", true);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMP, sessStatusVarChamp);
		
		
		chiediDati(absSender, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarChamp= (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMP);
		for(int i=0; i<RHCostants.ARRAYVAR_CHAMP.length; i++) {
			
			if(!(sessStatusVarChamp).get(RHCostants.ARRAYVAR_CHAMP[i]).asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_CHAMP[i]);
				return;
			}
		}
		
		eseguiComando(absSender, user, botSession);
	}
	
	public static void setDatiInSessione(DefaultAbsSender absSender, Update update, User user, Session botSession) {

		String message_text = null;
		if(update.hasMessage())
			message_text= update.getMessage().getText();
		else if(update.hasCallbackQuery())
			message_text= update.getCallbackQuery().getData();
		
		ObjectNode sessStatusVarChamp= (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMP);
		ObjectNode sessDatiChampTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMP_TMP);

		for (int i = 0; i < RHCostants.ARRAYVAR_CHAMP.length; i++) {

			if (!(sessStatusVarChamp).get(RHCostants.ARRAYVAR_CHAMP[i]).asBoolean()) {

					sessDatiChampTmp.put(RHCostants.ARRAYVAR_CHAMP[i], message_text);
					sessStatusVarChamp.put(RHCostants.ARRAYVAR_CHAMP[i], true);
					
					chiediDati(absSender, user, botSession);

					return;
			}
		}
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode responseApi = Utils.getResponseApi(RHBECampionato.save(RHBECampionato.preparaRequestSave(user, botSession, absSender)), absSender) ;
		

		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_ESITO_CHAMP_OK);
	}
	
}