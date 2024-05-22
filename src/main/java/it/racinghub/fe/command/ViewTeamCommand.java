package it.racinghub.fe.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.extendedlibrary.BotCommandExtended;
import it.racinghub.fe.helper.Utils;
import it.racinghub.fe.rest.external.RHBERecuperaPilota;
import it.racinghub.fe.rest.external.RHBERecuperaPilotaByTeam;

/**

 */
public class ViewTeamCommand extends BotCommandExtended {

    public ViewTeamCommand() {
        super(RHCostants.COMMAND_VIEWTEAM, "Comando per visualizzare i dettagli Team");
    }

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession, String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_VIEWTEAM);

		JsonNode pilotaUtente = Utils.getResponseApi(RHBERecuperaPilota.execute(RHBERecuperaPilota.preparaRequest(user, botSession, absSender)),
													absSender);
		
		JsonNode objTeamPilota = Utils.leggiJsonNodeObj(pilotaUtente, "team");
		botSession.setAttribute(RHCostants.KEYSESSION_PILOTA, pilotaUtente);

		if(Objects.isNull(objTeamPilota) || objTeamPilota.isEmpty()) {
			InlineKeyboardMarkup keybInline= Utils.creaKeyboardInline();
			List<List<InlineKeyboardButton>> matrixInline= Utils.creaMatrixInline();
			List<InlineKeyboardButton> rowInline= Utils.creaRowInline();
			matrixInline.add(rowInline);
			keybInline.setKeyboard(matrixInline);
			InlineKeyboardButton buttonInline= Utils.creaButtonInline(RHCostants.COMMAND_SETTEAM, RHCostants.LABEL_NEWTEAM , null);
			rowInline.add(buttonInline);
			
			Utils.inviaMessaggio(absSender, user.getId(), 
					"Il pilota non appartiene a nessun team!", keybInline);
			return;
		}
		
		List<ObjectNode> pilotiTeam = Utils.getResponseApi(RHBERecuperaPilotaByTeam.execute(RHBERecuperaPilotaByTeam.preparaRequest(user, botSession, absSender)),
				absSender);
		
		InlineKeyboardMarkup markupInline = buildinlineKeyboard(objTeamPilota, user);

		
		Utils.inviaMessaggio(absSender, user.getId(), 
				buildTextMessage(objTeamPilota,pilotiTeam),
				markupInline);

	}
	
	private InlineKeyboardMarkup buildinlineKeyboard(JsonNode objTeamPilota, User user) {
		
		InlineKeyboardMarkup markupInline = null;
		JsonNode objUtenteTeam = Utils.leggiJsonNodeObj(objTeamPilota, "utente");

		String userOwnerTeam = Utils.leggiJsonNodeVar(objUtenteTeam, "userTelegram");
		if(Objects.nonNull(userOwnerTeam) && !userOwnerTeam.isEmpty()){
			
			markupInline = new InlineKeyboardMarkup();
	        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
	        List<InlineKeyboardButton> rowInline = new ArrayList<>();
	        InlineKeyboardButton viewTrackButton = new InlineKeyboardButton();
			
	        //utente corrente è propretario del team
			if(userOwnerTeam.contains(user.getUserName())) {
		        viewTrackButton.setText(RHCostants.LABEL_SETTEAM);
		        viewTrackButton.setCallbackData(RHCostants.COMMAND_SETTEAM);
		        rowInline.add(viewTrackButton);
		        InlineKeyboardButton setCoDriverButton = new InlineKeyboardButton();
		        setCoDriverButton.setText(RHCostants.LABEL_SETCODRIVERTEAM);
		        setCoDriverButton.setCallbackData(RHCostants.COMMAND_SETCODRIVERTEAM);
		        rowInline.add(setCoDriverButton);
			}
			else {
				viewTrackButton.setText(RHCostants.LABEL_LEAVETEAM);
			    viewTrackButton.setCallbackData(RHCostants.COMMAND_LEAVETEAM);
		        rowInline.add(viewTrackButton);
			}
	        rowsInline.add(rowInline);
	        markupInline.setKeyboard(rowsInline);
		}
	
		return markupInline;
	}

	
	
	private String buildTextMessage(JsonNode objTeamPilota, List<ObjectNode> pilotiTeam) {
		
		JsonNode objUtenteTeam = Utils.leggiJsonNodeObj(objTeamPilota, "utente");

		
		String text = RHCostants.TEXT_VIEWTEAMCOMMAND;
		text = text.replaceAll(RHCostants.PLACEHOLDER_TID_TEAM, 			Utils.leggiJsonNodeVar(objTeamPilota, "idTeam"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_PROPR_TEAM, 			Utils.leggiJsonNodeVar(objUtenteTeam, "userTelegram"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_NOME_TEAM, 			Utils.leggiJsonNodeVar(objTeamPilota, "nome"));
		text = text.replaceAll(RHCostants.PLACEHOLDER_COLORE_TEAM,	 		Utils.leggiJsonNodeVar(objTeamPilota, "colore"));

		String textListPilotiTeam = "";
		for(ObjectNode x : pilotiTeam) {
			textListPilotiTeam = textListPilotiTeam +
						"- "+ Utils.leggiJsonNodeVar(x, "cognome") +" "+ Utils.leggiJsonNodeVar(x, "nome") +
						" (D.ID: "+ Utils.leggiJsonNodeVar(x, "idPilota")+ ")"+"\n";
		}
		
		text = text.replaceAll(RHCostants.PLACEHOLDER_LISTPILOTI_TEAM,	 		textListPilotiTeam);

		
		return text;
	}
	
}
