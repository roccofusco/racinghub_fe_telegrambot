package it.racinghub.fe.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.shiro.session.Session;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.rest.external.RHBERecuperaPilota;
import it.racinghub.fe.rest.external.RHBERecuperaUtente;

public class Utils<T> {
	
	public static String leggiJsonNodeVar(JsonNode json, String field) {
		String res = "";
		if(Objects.nonNull(json) && Objects.nonNull(field) &&
				json.hasNonNull(field))
			res = json.get(field).asText("");
		
		return res;
	}
	
	public static JsonNode leggiJsonNodeObj(JsonNode json, String field) {
		JsonNode res = new ObjectMapper().createObjectNode();
		if(Objects.nonNull(json) && Objects.nonNull(field) &&
				json.hasNonNull(field))
			res = json.get(field);
		
		return res;
	}
	
	public static <T> T leggiSessionVar(Session botSession, String key) {
		T res = null;
		if(Objects.nonNull(botSession))
			res = (T)botSession.getAttribute(key);
		return res;
	}
	
	public static void saveCommandInSession(Session botSession, String command) {
		botSession.setAttribute(RHCostants.KEYSESSION_COMMAND, command);
	}
	
	public static String readCommandInSession(Session botSession) {
		return (String)botSession.getAttribute(RHCostants.KEYSESSION_COMMAND);
	}
	
	public static void checkPilotaInSession(User user, Session botSession, AbsSender absSender) {
		JsonNode pilotaSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_PILOTA);

		
		if(Objects.isNull(pilotaSess)) {
			JsonNode responseApi = Utils.getResponseApi(RHBERecuperaPilota.execute(RHBERecuperaPilota.preparaRequest(user, botSession, absSender)),
														absSender);
			
			JsonNode objTeam = Utils.leggiJsonNodeObj(responseApi, "team");
			botSession.setAttribute(RHCostants.KEYSESSION_PILOTA, responseApi);
		}
	}
	
	public static void checkUtenteInSession(User user, Session botSession, AbsSender absSender) {
		JsonNode utenteSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_UTENTE);

		if(Objects.isNull(utenteSess)) {
			JsonNode responseApi = Utils.getResponseApi(RHBERecuperaUtente.execute(RHBERecuperaUtente.preparaRequest(user)),
														absSender);
			botSession.setAttribute(RHCostants.KEYSESSION_UTENTE, responseApi);
		}
	}
	
	public  static <T> T getResponseApi(ResponseEntity<T> responseApi, AbsSender absSender){
		if(!responseApi.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Errore durante la chiamata rest. Status code: " + responseApi.getStatusCode());
		}
		
		T res = responseApi.getBody();
		if(Objects.isNull(res)){
			if(res instanceof ObjectNode) {
				res = (T) new ObjectMapper().createObjectNode();
			}
			else if(res instanceof List<?>) {
				res = (T) new ArrayList<ObjectNode>();
			}
			
		}
		
		return res;
	}
	
	public static void inviaMessaggio(AbsSender bot, long chat_id, String message_text) {
		inviaMessaggio(bot, chat_id, message_text, null, "Markdown");
	}
	
	public static void inviaMessaggio(AbsSender bot, long chat_id, String message_text, InlineKeyboardMarkup markupInline) {
		inviaMessaggio(bot, chat_id, message_text, markupInline, "Markdown");
	}
	
	public static void inviaMessaggioHtml(AbsSender bot, long chat_id, String message_text) {
		inviaMessaggio(bot, chat_id, message_text, null, "HTML");
	}
	
	public static void inviaMessaggio(AbsSender bot, long chat_id, String message_text, InlineKeyboardMarkup markupInline, String parseMode) {
		SendMessage message = new SendMessage();
		
		message.setChatId(chat_id);
		message.setText(message_text);
	    
        message.setReplyMarkup(markupInline);
        message.setParseMode(parseMode);

		try {
	         bot.execute(message);
	    } catch (TelegramApiException e) {
	         e.printStackTrace();
	    }
	}
	
	public static File scaricaFile(DefaultAbsSender bot, String idFile) {
		
		 GetFile getFile = new GetFile();
		 getFile.setFileId(idFile);
         try {
        	 org.telegram.telegrambots.meta.api.objects.File filebots = bot.execute(getFile);
             String filePath = filebots.getFilePath();
             
             File file = bot.downloadFile(filePath);
             return file;
         } catch (TelegramApiException e) {
             e.printStackTrace();
         }
         
         return null;
	}
	
	
	public static InlineKeyboardMarkup creaKeyboardInline() {
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		return markupInline;
	}
	
	public static List<List<InlineKeyboardButton>> creaMatrixInline() {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        return rowsInline;
	}
	
	public static List<InlineKeyboardButton> creaRowInline() {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        return rowInline;
    }
	
	public static InlineKeyboardButton creaButtonInline(String callBackData, String text, String url){
        InlineKeyboardButton buttonInline = new InlineKeyboardButton();
        buttonInline.setText(text);
        buttonInline.setCallbackData(callBackData);
        buttonInline.setUrl(url);
        
        return buttonInline;
	}
	
	
}
