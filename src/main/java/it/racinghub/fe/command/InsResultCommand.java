package it.racinghub.fe.command;

import java.util.Objects;

import org.apache.shiro.session.Session;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBESalvaRisultato;

/**

 */
public class InsResultCommand extends BotCommandExtended {

    public InsResultCommand() {
        super(RHCostants.COMMAND_INSRESULT, "Comando per modificare i dettagli Pilota");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_INSRESULT);

		String idSessioneSel = null;
		boolean statusvar = false;
//		if(Objects.isNull(arguments) || arguments.length<1) {
//			Utils.inviaMessaggio(absSender, user.getId(), 
//					"C.ID Campionato assente! Riprova.");
//			return;
//		}
		if(Objects.nonNull(arguments) && arguments.length>0) {
			idSessioneSel = arguments[0];
			statusvar = true;
		}
		
		ObjectNode sessDatiInsresultTmp = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_INSRESULT)
			sessDatiInsresultTmp.set(x, null);
		
		sessDatiInsresultTmp.put("idSessione", idSessioneSel);
		botSession.setAttribute(RHCostants.KEYSESSION_INSRESULT_TMP, sessDatiInsresultTmp);
		
		ObjectNode sessStatusVarInsResult = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_INSRESULT)
			sessStatusVarInsResult.put(x, false);
		
		sessStatusVarInsResult.put("idSessione", statusvar);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_INSRESULT, sessStatusVarInsResult);
		
		
		chiediDati(absSender, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarInsresult = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_INSRESULT);
		for(int i=0; i<RHCostants.ARRAYVAR_INSRESULT.length; i++) {
			
			if(!(sessStatusVarInsresult).get(RHCostants.ARRAYVAR_INSRESULT[i]).asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_INSRESULT[i]);
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
		
		ObjectNode sessStatusVarInsresult = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_INSRESULT);
		ObjectNode sessDatiInsresultTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_INSRESULT_TMP);

		for (int i = 0; i < RHCostants.ARRAYVAR_INSRESULT.length; i++) {

			if (!(sessStatusVarInsresult).get(RHCostants.ARRAYVAR_INSRESULT[i]).asBoolean()) {

				if ("idFile".equals(RHCostants.ARRAYVAR_INSRESULT[i])) {
					String fileName = update.getMessage().getDocument().getFileName();
					// Controlla se il file è del tipo desiderato (CSV, XLS, XLSX)
					if (fileName.endsWith(".csv") || fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
						sessDatiInsresultTmp.put(RHCostants.ARRAYVAR_INSRESULT[i],
								update.getMessage().getDocument().getFileId());
						sessStatusVarInsresult.put(RHCostants.ARRAYVAR_INSRESULT[i], true);
						
						sessDatiInsresultTmp.put("nameFile", update.getMessage().getDocument().getFileName());
						sessStatusVarInsresult.put("nameFile", true);

					} else {
						Utils.inviaMessaggio(absSender, user.getId(), "Tipo file non corretto, riprova.");
					}
				} else {
					sessDatiInsresultTmp.put(RHCostants.ARRAYVAR_INSRESULT[i], message_text);
					sessStatusVarInsresult.put(RHCostants.ARRAYVAR_INSRESULT[i], true);

					return;
				}
			}
		}
		chiediDati(absSender, user, botSession);
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {
		JsonNode myChampSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_INSRESULT_TMP);

		ResponseEntity<ObjectNode> responseApi = RHBESalvaRisultato.execute(
											RHBESalvaRisultato.preparaRequest(user, botSession, absSender),
											Utils.leggiJsonNodeVar(myChampSess, "idSessione"));
		
		if(!responseApi.getStatusCode().is2xxSuccessful())
			Utils.inviaMessaggio(absSender, user.getId(), 
				Utils.leggiJsonNodeVar(responseApi.getBody(), "errorDesc"));
		else
			Utils.inviaMessaggio(absSender, user.getId(), 
					RHCostants.TEXT_ESITO_SEARCHMYCHAMP_OK);
	}
}
