package it.racinghub.fe.command;

import java.util.ArrayList;
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
import it.racinghub.fe.rest.external.RHBERecuperaPilota;

/**

 */
public class ViewDriverCommand extends BotCommandExtended {

    public ViewDriverCommand() {
        super(RHCostants.COMMAND_VIEWDRIVER, "Comando per visualizzare i dettagli Pilota");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_VIEWDRIVER);

		JsonNode responseApi = Utils.getResponseApi(RHBERecuperaPilota.execute(RHBERecuperaPilota.preparaRequest(user, botSession, absSender)),
													absSender);
		
		botSession.setAttribute(RHCostants.KEYSESSION_PILOTA, responseApi);
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        
        InlineKeyboardButton viewTrackButton = new InlineKeyboardButton();
        viewTrackButton.setText(RHCostants.LABEL_SETDRIVER);
        viewTrackButton.setCallbackData(RHCostants.COMMAND_SETDRIVER);
        rowInline.add(viewTrackButton);
        
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
		
		Utils.inviaMessaggio(absSender, user.getId(), 
				buildTextMessage(responseApi),
				markupInline);

	}
	
	
	private String buildTextMessage(JsonNode responseApi) {
		
		String text = RHCostants.TEXT_VIEWDRIVERCOMMAND;
		text = text.replaceAll(RHCostants.PLACEHOLDER_DID_PILOTA, 			Utils.leggiJsonNodeVar(responseApi, "idPilota"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOME_PILOTA, 			Utils.leggiJsonNodeVar(responseApi, "nome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_COGNOME_PILOTA, 		Utils.leggiJsonNodeVar(responseApi, "cognome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_DATANASCITA_PILOTA,	Utils.leggiJsonNodeVar(responseApi, "dataNascita"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_PESOKG_PILOTA, 		Utils.leggiJsonNodeVar(responseApi, "pesoKg"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_CELLULARE_PILOTA, 	Utils.leggiJsonNodeVar(responseApi, "cellulare"));

		return text;
	}
	
}
