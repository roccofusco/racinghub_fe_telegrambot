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
import it.racinghub.fe.rest.external.RHBEIscrizioneEvento;

/**

 */
public class ViewRegisteredCommand extends BotCommandExtended {

    public ViewRegisteredCommand() {
        super(RHCostants.COMMAND_VIEWREGISTERED, "Comando per visualizzare i Piloti iscritti ad un evento");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_VIEWREGISTERED);

		String idEventoSel = null;
		boolean varstatus =false;

		if(Objects.nonNull(arguments) && arguments.length>0) {
			idEventoSel = arguments[0];
			varstatus = true;
		}
		
		ObjectNode sessDatiIscrittoTmp = new ObjectMapper().createObjectNode();
		sessDatiIscrittoTmp.put("idEvento", idEventoSel);
		botSession.setAttribute(RHCostants.KEYSESSION_CHAMP_TMP, sessDatiIscrittoTmp);
		
		ObjectNode sessStatusVarIscritto= new ObjectMapper().createObjectNode();
		sessStatusVarIscritto.put("idEvento", varstatus);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMP, sessStatusVarIscritto);
		
		
		chiediDati(absSender, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarIscritto= (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMP);
			
			if(!(sessStatusVarIscritto).get("idEvento").asBoolean()){
				Utils.inviaMessaggio(absSender, user.getId(), 
						"E.ID dell'Evento:");
				return;
			}
		
		eseguiComando(absSender, user, botSession);
	}
	
	public static void setDatiInSessione(DefaultAbsSender absSender, Update update, User user, Session botSession) {

		String message_text = null;
		if(update.hasMessage())
			message_text= update.getMessage().getText();
		else if(update.hasCallbackQuery())
			message_text= update.getCallbackQuery().getData();
		
		ObjectNode sessStatusVarIscritto= (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMP);
		ObjectNode sessDatiIscrittoTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMP_TMP);

			if (!(sessStatusVarIscritto).get("idEvento").asBoolean()) {

				sessDatiIscrittoTmp.put("idEvento", message_text);
				sessStatusVarIscritto.put("idEvento", true);
				
				chiediDati(absSender, user, botSession);

				return;
			}
	}
	
	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode sessDatiIscrittoTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMP_TMP);

		
		List<ObjectNode> responseApi = Utils.getResponseApi(RHBEIscrizioneEvento.getPilotaIscrittoByPilotaIscritto(Utils.leggiJsonNodeVar(sessDatiIscrittoTmp, "idEvento")), absSender);
		
		InlineKeyboardMarkup keybInline= Utils.creaKeyboardInline();
		List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
		List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
		matrixInline.add(rowInline);
		keybInline.setKeyboard(matrixInline);
		
		String idEventoSel= Utils.leggiJsonNodeVar(sessDatiIscrittoTmp, "idEvento");
		InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETREGISTERED+" "+idEventoSel, RHCostants.LABEL_SETREGISTERED , null);
		rowInline.add(buttonInline);
		
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				buildTextMessageListIscritti(responseApi), keybInline);
	}
	
	private static String buildTextMessageListIscritti(List<ObjectNode> pilotiIscritti) {
		
		
		String textListPilotiTeam = "";
		for(ObjectNode x : pilotiIscritti) {
			
			JsonNode objTeam = Utils.leggiJsonNodeObj(x, "team");
			JsonNode objPilota = Utils.leggiJsonNodeObj(x, "pilota");

			String namingPilota=Utils.leggiJsonNodeVar(objPilota, "cognome");
			if(!Utils.leggiJsonNodeVar(objPilota, "nome").isEmpty()) {
				namingPilota = namingPilota + " "+Utils.leggiJsonNodeVar(objPilota, "nome").charAt(0)+".";
			}
			
			textListPilotiTeam = textListPilotiTeam +
						"- "+ "D.ID:"+Utils.leggiJsonNodeVar(objPilota, "idPilota") +" "+
						namingPilota + " " +
						"("+Utils.leggiJsonNodeVar(objTeam, "nome")+")"+ " "+
						"Targa "+Utils.leggiJsonNodeVar(x, "targaVeicolo")+"\n";
		}
		
		if(textListPilotiTeam.isEmpty())
			textListPilotiTeam = "Nessun Pilota iscritto!";
		
		return textListPilotiTeam;
	}
	
}
