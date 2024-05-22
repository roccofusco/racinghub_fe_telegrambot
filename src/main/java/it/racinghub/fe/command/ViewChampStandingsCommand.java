package it.racinghub.fe.command;

import java.util.ArrayList;
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
import it.racinghub.fe.rest.external.RHBEClassifica;
import it.racinghub.fe.rest.external.RHBEEvento;
import it.racinghub.fe.rest.external.RHBEIscrizioneEvento;
import it.racinghub.fe.rest.external.RHBERecuperaPilota;
import it.racinghub.fe.rest.external.RHBESalvaTeam;
import it.racinghub.fe.rest.external.RHBESessione;

/**

 */
public class ViewChampStandingsCommand extends BotCommandExtended {

    public ViewChampStandingsCommand() {
        super(RHCostants.COMMAND_VIEWCHAMPSTANDINGS, "Comando per visualizzare la classifica Campionato");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_VIEWCHAMPSTANDINGS);

		String idCampionatoSel = null;
		boolean statusVarSel = false;
//		if(Objects.isNull(arguments) || arguments.length<1) {
//			Utils.inviaMessaggio(absSender, user.getId(), 
//					"C.ID Campionato assente! Riprova.");
//			return;
//		}
		if (Objects.nonNull(arguments) && arguments.length > 0) {
			idCampionatoSel = arguments[0];
			statusVarSel = true;
		}
		
		ObjectNode sessDatiChampStandingsTmp = new ObjectMapper().createObjectNode();
		for (String x : RHCostants.ARRAYVAR_CHAMPSTANDINGS)
			sessDatiChampStandingsTmp.set(x, null);

		sessDatiChampStandingsTmp.put("idCampionato", idCampionatoSel);
		botSession.setAttribute(RHCostants.KEYSESSION_CHAMPSTANDINGS_TMP, sessDatiChampStandingsTmp);

		ObjectNode sessStatusVarChampStandings = new ObjectMapper().createObjectNode();
		for (String x : RHCostants.ARRAYVAR_CHAMPSTANDINGS)
			sessStatusVarChampStandings.put(x, false);

		sessStatusVarChampStandings.put("idCampionato", statusVarSel);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMPSTANDINGS, sessStatusVarChampStandings);

		chiediDati(absSender, user, botSession);

	}

	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarChampStandings = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMPSTANDINGS);
		ObjectNode sessDatiChampStandingsTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMPSTANDINGS_TMP);

		for(int i=0; i<RHCostants.ARRAYVAR_CHAMPSTANDINGS.length; i++) {
			
			if(!(sessStatusVarChampStandings).get(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i]).asBoolean()){
				InlineKeyboardMarkup keybInline= null;

				if("idCampionato".equals(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i])){
					List<ObjectNode> responseApi = Utils.getResponseApi(RHBEIscrizioneEvento.getChampByPilotaIscritto(RHBEIscrizioneEvento.preparaRequestPilota(user, botSession, absSender)),
																absSender);
					
					keybInline= Utils.creaKeyboardInline();
					List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
					
					for(ObjectNode x : responseApi){
						
						String idCampionato= Utils.leggiJsonNodeVar(x, "idCampionato");
						String nomeCampionato= Utils.leggiJsonNodeVar(x, "nome");
						
						List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
						InlineKeyboardButton buttonInline= Utils.creaButtonInline(idCampionato, nomeCampionato, null);
						rowInline.add(buttonInline);
						matrixInline.add(rowInline);

					}
					keybInline.setKeyboard(matrixInline);
				}
				else if("tipoClassifica".equals(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i])){
					keybInline= Utils.creaKeyboardInline();
					List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
					List<InlineKeyboardButton> rowInlineP= Utils.creaRowInline();
					InlineKeyboardButton buttonInlineP= Utils.creaButtonInline(RHCostants.TIPOCLASS_PILOTA_VALUE, RHCostants.TIPOCLASS_PILOTA_LABEL, null);
					rowInlineP.add(buttonInlineP);
					matrixInline.add(rowInlineP);
					List<InlineKeyboardButton> rowInlineT= Utils.creaRowInline();
					InlineKeyboardButton buttonInlineT= Utils.creaButtonInline(RHCostants.TIPOCLASS_TEAM_VALUE, RHCostants.TIPOCLASS_TEAM_LABEL, null);
					rowInlineT.add(buttonInlineT);
					matrixInline.add(rowInlineT);
					
					
					keybInline.setKeyboard(matrixInline);
				}
				
				
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_CHAMPSTANDINGS[i],
						keybInline);
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
		
		ObjectNode sessStatusVarChampStandings = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMPSTANDINGS);
		ObjectNode sessDatiChampStandingsTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMPSTANDINGS_TMP);

		for(int i=0; i<RHCostants.ARRAYVAR_CHAMPSTANDINGS.length; i++) {
			
			if(!(sessStatusVarChampStandings).get(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i]).asBoolean()){
				
				{
					sessDatiChampStandingsTmp.put(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i], message_text);
					sessStatusVarChampStandings.put(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i], true);
				
//					InlineKeyboardMarkup keybInline= Utils.creaKeyboardInline();
//					List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
//					List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
					
//					matrixInline.add(rowInline);
//					keybInline.setKeyboard(matrixInline);

					if("idCampionato".equals(RHCostants.ARRAYVAR_CHAMPSTANDINGS[i])){
						ObjectNode responseApiCampionato = Utils.getResponseApi(RHBECampionato.getById(RHBECampionato.preparaRequestCampionato(user, botSession, absSender, RHCostants.KEYSESSION_CHAMPSTANDINGS_TMP)),
								absSender);
						
						String idChampSel= Utils.leggiJsonNodeVar(sessDatiChampStandingsTmp, "idCampionato");

//						InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETCHAMP+" "+idChampSel, RHCostants.LABEL_SETCHAMP, null);
//						rowInline.add(buttonInline);
						Utils.inviaMessaggio(absSender, user.getId(), 
								buildTextMessageCampionato(responseApiCampionato));
//								keybInline);
					}
				}
				chiediDati(absSender, user, botSession);
				
				return;
			}
		}
		chiediDati(absSender, user, botSession);

	}
	
	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarChampStandings = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_CHAMPSTANDINGS);
		ObjectNode sessDatiChampStandingsTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMPSTANDINGS_TMP);

		String typeStandigs = Utils.leggiJsonNodeVar(sessDatiChampStandingsTmp, "tipoClassifica");
		
		if(RHCostants.TIPOCLASS_PILOTA_VALUE.equals(typeStandigs)){
			List<ObjectNode> responseApi = Utils.getResponseApi(RHBEClassifica.getClassificaPilotaByCampionato(Utils.leggiJsonNodeVar(sessDatiChampStandingsTmp, "idCampionato")), absSender);

			Utils.inviaMessaggio(absSender, user.getId(), 
					buildTextMessageClassificaPilota(responseApi));
		}
		else if(RHCostants.TIPOCLASS_TEAM_VALUE.equals(typeStandigs)){
			List<ObjectNode> responseApi = Utils.getResponseApi(RHBEClassifica.getClassificaTeamByCampionato(Utils.leggiJsonNodeVar(sessDatiChampStandingsTmp, "idCampionato")), absSender);

			Utils.inviaMessaggio(absSender, user.getId(), 
					buildTextMessageClassificaTeam(responseApi));
		}
		sessStatusVarChampStandings.put("tipoClassifica", false);
		
	}
	
	private static String buildTextMessageClassificaPilota(List<ObjectNode> objlistClassifica) {
		
		String text = "";
		for(ObjectNode obj : objlistClassifica) {
			
			JsonNode objPilota = Utils.leggiJsonNodeObj(obj, "pilota");
			JsonNode objTeam = Utils.leggiJsonNodeObj(obj, "team");
			
			String textRow = RHCostants.TEXT_DRIVERSTANDINGS;
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_POS_STANDINGS, 		Utils.leggiJsonNodeVar(obj, "posizione"));
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_PILOTA_STANDINGS, 	Utils.leggiJsonNodeVar(objPilota, "cognome"));
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_TEAM_STANDINGS, 	Utils.leggiJsonNodeVar(objTeam, "nome"));
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_PUNTI_STANDINGS,	Utils.leggiJsonNodeVar(obj, "punti"));
			
			text = text + textRow;
		}
		
		return text;
	}
	
	private static String buildTextMessageClassificaTeam(List<ObjectNode> objlistClassifica) {
		
		String text = "";
		for(ObjectNode obj : objlistClassifica) {
			
			JsonNode objTeam = Utils.leggiJsonNodeObj(obj, "team");
			
			String textRow = RHCostants.TEXT_TEAMSTANDINGS;
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_POS_STANDINGS, 		Utils.leggiJsonNodeVar(obj, "posizione"));
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_TEAM_STANDINGS, 	Utils.leggiJsonNodeVar(objTeam, "nome"));
			textRow = textRow.replaceAll(RHCostants.PLACEHOLDER_PUNTI_STANDINGS,	Utils.leggiJsonNodeVar(obj, "punti"));
			
			text = text + textRow;
		}
		
		return text;
	}
	
	private static String buildTextMessageCampionato(JsonNode objCampionato) {
		
		JsonNode objUtenteCampionato = Utils.leggiJsonNodeObj(objCampionato, "utente");
		
		String text = RHCostants.TEXT_VIEWCAMP;
		text = text.replaceAll(RHCostants.PLACEHOLDER_CID_CAMP, 			Utils.leggiJsonNodeVar(objCampionato, "idCampionato"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOME_CAMP, 			Utils.leggiJsonNodeVar(objCampionato, "nome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_DINIZIO_CAMP, 		Utils.leggiJsonNodeVar(objCampionato, "dataInizio"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_DFINE_CAMP,			Utils.leggiJsonNodeVar(objCampionato, "dataFine"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_PROPR_CAMP, 			Utils.leggiJsonNodeVar(objUtenteCampionato, "userTelegram"));

		return text;
	}

}
