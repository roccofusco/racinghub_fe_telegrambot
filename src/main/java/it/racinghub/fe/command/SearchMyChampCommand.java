package it.racinghub.fe.command;

import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.http.ResponseEntity;
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
import it.racinghub.fe.rest.external.RHBEEvento;
import it.racinghub.fe.rest.external.RHBESalvaRisultato;
import it.racinghub.fe.rest.external.RHBESessione;

/**

 */
public class SearchMyChampCommand extends BotCommandExtended {

    public SearchMyChampCommand() {
        super(RHCostants.COMMAND_SEARCHMYCHAMP, "Comando per ricercare champ/event/session");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {
		
		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SEARCHMYCHAMP);

		
		ObjectNode sessDatiInsResultTmp = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_SEARCHMYCHAMP)
			sessDatiInsResultTmp.set(x, null);
		botSession.setAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP, sessDatiInsResultTmp);
		
		ObjectNode sessStatusVarMychamp = new ObjectMapper().createObjectNode();
		for(String x : RHCostants.ARRAYVAR_SEARCHMYCHAMP)
			sessStatusVarMychamp.put(x, false);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_SEARCHMYCHAMP, sessStatusVarMychamp);

		
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				RHCostants.TEXT_REQ_SEARCHMYCHAMP_DATA);
		
		chiediDati(absSender, user, botSession);
		
	}
	
	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {
		
		ObjectNode sessStatusVarMychamp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_SEARCHMYCHAMP);
		ObjectNode sessDatiMychampTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP);

		for(int i=0; i<RHCostants.ARRAYVAR_SEARCHMYCHAMP.length; i++) {
			
			if(!(sessStatusVarMychamp).get(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i]).asBoolean()){
				InlineKeyboardMarkup keybInline= null;

				if("idCampionato".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
					List<ObjectNode> responseApi = Utils.getResponseApi(RHBECampionato.getByUtente(RHBECampionato.preparaRequestUtente(user, botSession, absSender)),
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
					
//					String idChampSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idCampionato");
					String idChampSel= "";

					List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
					InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETCHAMP+" "+idChampSel, RHCostants.LABEL_NEWCHAMP, null);
					rowInline.add(buttonInline);
					matrixInline.add(rowInline);
					
					keybInline.setKeyboard(matrixInline);
					
				}
				else if("idEvento".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
					List<ObjectNode> responseApiEvento = Utils.getResponseApi(RHBEEvento.getByCampionato(RHBEEvento.preparaRequestCampionato(user, botSession, absSender)),
																absSender);
					
					keybInline= Utils.creaKeyboardInline();
					List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
					
					for(ObjectNode x : responseApiEvento){
						
						String idEvento= Utils.leggiJsonNodeVar(x, "idEvento");
						String nomeEvento= Utils.leggiJsonNodeVar(x, "nome");
						
						List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
						InlineKeyboardButton buttonInline= Utils.creaButtonInline(idEvento, nomeEvento, null);
						rowInline.add(buttonInline);
						matrixInline.add(rowInline);

					}
					
					String idChampSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idCampionato");
//					String idEventoSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idEvento");
					String idEventoSel= "";

					List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
					InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETEVENT+" "+idChampSel+" "+idEventoSel, RHCostants.LABEL_NEWEVENT, null);
					rowInline.add(buttonInline);
					matrixInline.add(rowInline);
					
					keybInline.setKeyboard(matrixInline);
					
				}
				else if("idSessione".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
					List<ObjectNode> responseApi = Utils.getResponseApi(RHBESessione.getByEvento(RHBESessione.preparaRequestEvento(user, botSession, absSender)),
																absSender);
					
					keybInline= Utils.creaKeyboardInline();
					List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
					
					for(ObjectNode x : responseApi){
						
						String idSessione= Utils.leggiJsonNodeVar(x, "idSessione");
						String nomeSessione= Utils.leggiJsonNodeVar(x, "nome");
						
						List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
						InlineKeyboardButton buttonInline= Utils.creaButtonInline(idSessione, nomeSessione, null);
						rowInline.add(buttonInline);
						matrixInline.add(rowInline);

					}
					
					String idChampSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idCampionato");
					String idEventoSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idEvento");
//					String idSessioneSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idSessione");
					String idSessioneSel= "";

					List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
					InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETSESSION+" "+idChampSel+" "+idEventoSel+" "+idSessioneSel, RHCostants.LABEL_NEWSESSION, null);
					rowInline.add(buttonInline);
					matrixInline.add(rowInline);
					
					keybInline.setKeyboard(matrixInline);
					
				}
				
				Utils.inviaMessaggio(absSender, user.getId(), 
						RHCostants.ARRAYTEXT_REQ_SEARCHMYCHAMP[i],
						keybInline);
				return;
			}
		}
		
	}
	
	public static void setDatiInSessione(DefaultAbsSender absSender, Update update, User user, Session botSession) {

		String message_text = null;
		if(update.hasMessage())
			message_text= update.getMessage().getText();
		else if(update.hasCallbackQuery())
			message_text= update.getCallbackQuery().getData();
		
		ObjectNode sessStatusVarMychamp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_SEARCHMYCHAMP);
		ObjectNode sessDatiMychampTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP);

		for(int i=0; i<RHCostants.ARRAYVAR_SEARCHMYCHAMP.length; i++) {
			
			if(!(sessStatusVarMychamp).get(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i]).asBoolean()){
				
//				if("idFile".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
//					 String fileName = update.getMessage().getDocument().getFileName();
//			        // Controlla se il file è del tipo desiderato (CSV, XLS, XLSX)
//			        if (fileName.endsWith(".csv") || fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
//			        	sessDatiMychampTmp.put(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i], update.getMessage().getDocument().getFileId());
//						sessStatusVarMychamp.put(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i], true);
//						sessDatiMychampTmp.put("nameFile", update.getMessage().getDocument().getFileName());
//			        }
//			        else {
//			        	Utils.inviaMessaggio(absSender, user.getId(), 
//			    				"Tipo file non corretto, riprova.");
//			        }
//				}else
				{
					sessDatiMychampTmp.put(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i], message_text);
					sessStatusVarMychamp.put(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i], true);
				
					InlineKeyboardMarkup keybInline= Utils.creaKeyboardInline();
					List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
					List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
					
					matrixInline.add(rowInline);
					keybInline.setKeyboard(matrixInline);

					if("idCampionato".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
						ObjectNode responseApiCampionato = Utils.getResponseApi(RHBECampionato.getById(RHBECampionato.preparaRequestCampionato(user, botSession, absSender, RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP)),
								absSender);
						
						String idChampSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idCampionato");

						InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETCHAMP+" "+idChampSel, RHCostants.LABEL_SETCHAMP, null);
						rowInline.add(buttonInline);
						Utils.inviaMessaggio(absSender, user.getId(), 
								buildTextMessageCampionato(responseApiCampionato),
								keybInline);
					}
					else if("idEvento".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
						ObjectNode responseApiEvento = Utils.getResponseApi(RHBEEvento.getById(RHBEEvento.preparaRequestEvento(user, botSession, absSender)),
								absSender);
						
						String idChampSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idCampionato");
						String idEventoSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idEvento");

						InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETEVENT+" "+idChampSel+" "+idEventoSel, RHCostants.LABEL_SETEVENT, null);
						rowInline.add(buttonInline);
						
						List<InlineKeyboardButton> rowInline2= Utils.creaRowInline();
						matrixInline.add(rowInline2);
						InlineKeyboardButton buttonInline2= Utils.creaButtonInline(RHCostants.COMMAND_VIEWREGISTERED+" "+idEventoSel, RHCostants.LABEL_VIEWREGISTERED , null);
						rowInline2.add(buttonInline2);
						
						Utils.inviaMessaggio(absSender, user.getId(), 
								buildTextMessageEvento(responseApiEvento),
								keybInline);
						
					}
					else if("idSessione".equals(RHCostants.ARRAYVAR_SEARCHMYCHAMP[i])){
						ObjectNode responseApiSessione = Utils.getResponseApi(RHBESessione.getById(RHBESessione.preparaRequestSessione(user, botSession, absSender)),
								absSender);
						InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETSESSION, RHCostants.LABEL_SETSESSION, null);
						rowInline.add(buttonInline);
						
//						String idChampSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idCampionato");
//						String idEventoSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idEvento");
						String idSessioneSel= Utils.leggiJsonNodeVar(sessDatiMychampTmp, "idSessione");
						List<InlineKeyboardButton> rowInline2= Utils.creaRowInline();
						matrixInline.add(rowInline2);
						InlineKeyboardButton buttonInline2= Utils.creaButtonInline(RHCostants.COMMAND_INSRESULT+" "+idSessioneSel, RHCostants.LABEL_INSRESULT, null);
						rowInline2.add(buttonInline2);
						
						Utils.inviaMessaggio(absSender, user.getId(), 
								buildTextMessageSessione(responseApiSessione),
								keybInline);
					}
				}
				chiediDati(absSender, user, botSession);
				
				return;
			}
		}
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
	
	private static String buildTextMessageEvento(JsonNode objEvento) {
		
		JsonNode objPistaEvento = Utils.leggiJsonNodeObj(objEvento, "pista");
		
		String text = RHCostants.TEXT_VIEWEVENTO;
		text = text.replaceAll(RHCostants.PLACEHOLDER_EID_EVENTO, 			Utils.leggiJsonNodeVar(objEvento, "idEvento"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOME_EVENTO, 			Utils.leggiJsonNodeVar(objEvento, "nome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_DATA_EVENTO, 			Utils.leggiJsonNodeVar(objEvento, "dataInizio"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_QUOTAISCR_EVENTO,		Utils.leggiJsonNodeVar(objEvento, "quotaIscrizione"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_DSCANDENZAISCR_EVENTO,Utils.leggiJsonNodeVar(objEvento, "scadenzaIscrizione"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOME_PISTA, 			Utils.leggiJsonNodeVar(objPistaEvento, "nome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_INDIRIZZO_PISTA, 		Utils.leggiJsonNodeVar(objPistaEvento, "indirizzo"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_CITTA_PISTA, 			Utils.leggiJsonNodeVar(objPistaEvento, "citta"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_PROVINCIA_PISTA, 		Utils.leggiJsonNodeVar(objPistaEvento, "provincia"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_CAP_PISTA, 			Utils.leggiJsonNodeVar(objPistaEvento, "cap"));

		return text;
	}
	
	private static String buildTextMessageSessione(JsonNode objSessione) {
		
		JsonNode objTipoSessione = Utils.leggiJsonNodeObj(objSessione, "tipoSessione");
		
		String text = RHCostants.TEXT_VIEWSESSIONE;
		text = text.replaceAll(RHCostants.PLACEHOLDER_SID_SESSIONE, 			Utils.leggiJsonNodeVar(objSessione, "idSessione"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOME_SESSIONE, 			Utils.leggiJsonNodeVar(objSessione, "nome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_DESC_TIPOSESSIONE, 		Utils.leggiJsonNodeVar(objTipoSessione, "descrizione"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_VALDURATA_SESSIONE,		Utils.leggiJsonNodeVar(objSessione, "durata"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_UDURATA_SESSIONE, 		Utils.leggiJsonNodeVar(objSessione, "unitaDurata"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_PERCDURATA_SESSIONE, 		Utils.leggiJsonNodeVar(objSessione, "percentDurata"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOTE_SESSIONE, 			Utils.leggiJsonNodeVar(objSessione, "note"));

		/**
		 * 	public static final String PLACEHOLDER_SID_SESSIONE = "\\[SID_SESSIONE\\]";
	public static final String PLACEHOLDER_NOME_SESSIONE = "\\[NOME_SESSIONE\\]";
	public static final String PLACEHOLDER_DESC_TIPOSESSIONE = "\\[DESC_TIPOSESSIONE\\]";
	public static final String PLACEHOLDER_DURATA_SESSIONE = "\\[DURATA_SESSIONE\\]";
	public static final String PLACEHOLDER_UDURATA_SESSIONE = "\\[UDURATA_SESSIONE\\]";
	public static final String PLACEHOLDER_PERCDURATA_SESSIONE = "\\[PERCDURATA_SESSIONE\\]";
	public static final String PLACEHOLDER_NOTE_SESSIONE = "\\[NOTE_SESSIONE\\]";
		 */
		
		return text;
	}
}
