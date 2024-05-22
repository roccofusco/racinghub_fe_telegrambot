package it.racinghub.fe.command;

import java.util.List;
import java.util.Objects;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBECampionato;
import it.racinghub.fe.rest.external.RHBEIscrizioneEvento;
import it.racinghub.fe.rest.external.RHBESalvaTeam;

/**

 */
public class SetRegisteredCommand extends BotCommandExtended {

    public SetRegisteredCommand() {
        super(RHCostants.COMMAND_SETREGISTERED, "Comando per inserire/modificare i dettagli Iscritto");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETREGISTERED);

		String idEventoSel = null;

//		if(Objects.isNull(arguments) || arguments.length<1) {
//			Utils.inviaMessaggio(absSender, user.getId(), 
//					"C.ID Campionato assente! Riprova.");
//			return;
//		}
		if(Objects.nonNull(arguments) && arguments.length>0) {
			idEventoSel = arguments[0];
		}
		
		ObjectNode sessDatiRegisteredTmp = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_REGISTERED)
			sessDatiRegisteredTmp.set(x, null);
		
		sessDatiRegisteredTmp.put("idEvento", idEventoSel);
		botSession.setAttribute(RHCostants.KEYSESSION_REGISTERED_TMP, sessDatiRegisteredTmp);
		
		ObjectNode sessStatusVarRegistered= new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_REGISTERED)
			sessStatusVarRegistered.put(x, false);
		
		sessStatusVarRegistered.put("idEvento", true);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_REGISTERED, sessStatusVarRegistered);
		
		
		chiediDati(absSender, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarRegistered = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_REGISTERED);
		for(int i=0; i<RHCostants.ARRAYVAR_REGISTERED.length; i++) {
			
			if(!(sessStatusVarRegistered).get(RHCostants.ARRAYVAR_REGISTERED[i]).asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_REGISTERED[i]);
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
		
		ObjectNode sessStatusVarRegistered= (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_REGISTERED);
		ObjectNode sessDatiRegisteredTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_REGISTERED_TMP);

		for (int i = 0; i < RHCostants.ARRAYVAR_REGISTERED.length; i++) {

			if (!(sessStatusVarRegistered).get(RHCostants.ARRAYVAR_REGISTERED[i]).asBoolean()) {

					sessDatiRegisteredTmp.put(RHCostants.ARRAYVAR_REGISTERED[i], message_text);
					sessStatusVarRegistered.put(RHCostants.ARRAYVAR_REGISTERED[i], true);
					
					chiediDati(absSender, user, botSession);

					return;
			}
		}
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode responseApi = Utils.getResponseApi(RHBEIscrizioneEvento.save(RHBEIscrizioneEvento.preparaRequestSave(user, botSession, absSender)), absSender) ;
		
		ObjectNode sessDatiRegisteredTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_REGISTERED_TMP);

		InlineKeyboardMarkup keybInline= Utils.creaKeyboardInline();
		List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
		List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
		matrixInline.add(rowInline);
		keybInline.setKeyboard(matrixInline);
		String idEventoSel= Utils.leggiJsonNodeVar(sessDatiRegisteredTmp, "idEvento");
		InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETREGISTERED+" "+idEventoSel, "Continuare?" , null);
		rowInline.add(buttonInline);
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_ESITO_REGISTERED_OK, keybInline);
	}
	
}