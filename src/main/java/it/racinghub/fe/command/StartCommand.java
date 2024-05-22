package it.racinghub.fe.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.fasterxml.jackson.databind.JsonNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBERecuperaUtente;

/**

 */
public class StartCommand extends BotCommandExtended {

    public static final String LOGTAG = "STARTCOMMAND";

    public StartCommand() {
        super(RHCostants.COMMAND_START, "Comando di inizializzazione Bot");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_START);

		JsonNode responseApi = Utils.getResponseApi(RHBERecuperaUtente.execute(RHBERecuperaUtente.preparaRequest(user)),
													absSender);
		
		botSession.setAttribute(RHCostants.KEYSESSION_UTENTE, responseApi);
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        
        InlineKeyboardButton viewTrackButton = new InlineKeyboardButton();
        viewTrackButton.setText(RHCostants.LABEL_VIEWDRIVER);
        viewTrackButton.setCallbackData(RHCostants.COMMAND_VIEWDRIVER);
        rowInline.add(viewTrackButton);
        
        InlineKeyboardButton newTrackButton = new InlineKeyboardButton();
        newTrackButton.setText(RHCostants.LABEL_VIEWTEAM);
        newTrackButton.setCallbackData(RHCostants.COMMAND_VIEWTEAM);
        rowInline.add(newTrackButton);
        
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				buildTextMessage(responseApi),
				markupInline);


			System.out.println(
					"[" + new Date(message.getDate() * 1000L) + "] --> " + "#Id(" + user.getId() + ")" + "#Username("
							+ user.getUserName() + ")" + "#chat_id(" + user.getId() + ")" + " ha scritto: " + message.getText());
	}
	
	private String buildTextMessage(JsonNode responseApi) {
		
		String text = RHCostants.TEXT_STARTCOMMAND;
		text = text.replaceAll(RHCostants.PLACEHOLDER_USERNAME, ""+responseApi.get("userTelegram").asText());
		
		return text;
	}
}
