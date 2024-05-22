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
import it.racinghub.fe.rest.external.RHBEEvento;

/**

 */
public class SetEventCommand extends BotCommandExtended {

	public SetEventCommand() {
		super(RHCostants.COMMAND_SETEVENT, "Comando per modificare i dettagli Evento");
	}

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession,
			String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETEVENT);

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

		String idEventoSel = null;
		if (Objects.nonNull(arguments) && arguments.length > 1) {
			idEventoSel = arguments[1];
		}
		
		ObjectNode sessDatiEventTmp = new ObjectMapper().createObjectNode();
		for (String x : RHCostants.ARRAYVAR_EVENT)
			sessDatiEventTmp.set(x, null);

		sessDatiEventTmp.put("idCampionato", idCampionatoSel);
		sessDatiEventTmp.put("idEvento", idEventoSel);
		botSession.setAttribute(RHCostants.KEYSESSION_EVENT_TMP, sessDatiEventTmp);

		ObjectNode sessStatusVarEvent = new ObjectMapper().createObjectNode();
		for (String x : RHCostants.ARRAYVAR_EVENT)
			sessStatusVarEvent.put(x, false);

		sessStatusVarEvent.put("idCampionato", statusVarSel);
		sessStatusVarEvent.put("idEvento", true);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_EVENT, sessStatusVarEvent);

		chiediDati(absSender, user, botSession);

	}

	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode sessStatusVarEvent = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_EVENT);
		for (int i = 0; i < RHCostants.ARRAYVAR_EVENT.length; i++) {

			if (!(sessStatusVarEvent).get(RHCostants.ARRAYVAR_EVENT[i]).asBoolean()) {
				Utils.inviaMessaggio(absSender, user.getId(), RHCostants.ARRAYTEXT_REQ_EVENT[i]);
				return;
			}
		}

		eseguiComando(absSender, user, botSession);
	}

	public static void setDatiInSessione(DefaultAbsSender absSender, Update update, User user, Session botSession) {

		String message_text = null;
		if (update.hasMessage())
			message_text = update.getMessage().getText();
		else if (update.hasCallbackQuery())
			message_text = update.getCallbackQuery().getData();

		ObjectNode sessStatusVarEvent = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_EVENT);
		ObjectNode sessDatiEventTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_EVENT_TMP);

		for (int i = 0; i < RHCostants.ARRAYVAR_EVENT.length; i++) {

			if (!(sessStatusVarEvent).get(RHCostants.ARRAYVAR_EVENT[i]).asBoolean()) {

				sessDatiEventTmp.put(RHCostants.ARRAYVAR_EVENT[i], message_text);
				sessStatusVarEvent.put(RHCostants.ARRAYVAR_EVENT[i], true);

				chiediDati(absSender, user, botSession);

				return;
			}
		}
	}

	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode responseApi = Utils.getResponseApi(
				RHBEEvento.save(RHBEEvento.preparaRequestSave(user, botSession, absSender)), absSender);

		Utils.inviaMessaggio(absSender, user.getId(), RHCostants.TEXT_ESITO_EVENT_OK);
	}

}