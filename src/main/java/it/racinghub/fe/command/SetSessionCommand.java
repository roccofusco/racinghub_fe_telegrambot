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
import it.racinghub.fe.rest.external.RHBESessione;

/**

 */
public class SetSessionCommand extends BotCommandExtended {

	public SetSessionCommand() {
		super(RHCostants.COMMAND_SETSESSION, "Comando per modificare i dettagli Sessione");
	}

	@Override
	public void execute(DefaultAbsSender absSender, Message message, User user, Chat chat, Session botSession,
			String[] arguments) {

		Utils.saveCommandInSession(botSession, RHCostants.COMMAND_SETSESSION);

		String idCampionatoSel = null;
		String idEventoSel = null;
		boolean statusVarSel = false;
//		if(Objects.isNull(arguments) || arguments.length<1) {
//			Utils.inviaMessaggio(absSender, user.getId(), 
//					"C.ID Campionato assente! Riprova.");
//			return;
//		}
		if (Objects.nonNull(arguments) && arguments.length > 1) {
			idCampionatoSel = arguments[0];
			idEventoSel = arguments[1];
			statusVarSel = true;
		}

		String idSessioneSel = null;
		if (Objects.nonNull(arguments) && arguments.length > 2) {
			idSessioneSel = arguments[2];
		}
		
		ObjectNode sessDatiSessionTmp = new ObjectMapper().createObjectNode();
		for (String x : RHCostants.ARRAYVAR_SESSION)
			sessDatiSessionTmp.set(x, null);

		sessDatiSessionTmp.put("idCampionato", idCampionatoSel);
		sessDatiSessionTmp.put("idEvento", idEventoSel);
		sessDatiSessionTmp.put("idSessione", idSessioneSel);
		botSession.setAttribute(RHCostants.KEYSESSION_SESSION_TMP, sessDatiSessionTmp);

		ObjectNode sessStatusVarSession = new ObjectMapper().createObjectNode();
		for (String x : RHCostants.ARRAYVAR_SESSION)
			sessStatusVarSession.put(x, false);

		sessStatusVarSession.put("idCampionato", statusVarSel);
		sessStatusVarSession.put("idEvento", statusVarSel);
		sessStatusVarSession.put("idSessione", true);
		botSession.setAttribute(RHCostants.KEYSESSION_STATUSVAR_SESSION, sessStatusVarSession);

		chiediDati(absSender, user, botSession);

	}

	public static void chiediDati(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode sessStatusVarSession = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_SESSION);
		for (int i = 0; i < RHCostants.ARRAYVAR_SESSION.length; i++) {

			if (!(sessStatusVarSession).get(RHCostants.ARRAYVAR_SESSION[i]).asBoolean()) {
				Utils.inviaMessaggio(absSender, user.getId(), RHCostants.ARRAYTEXT_REQ_SESSION[i]);
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

		ObjectNode sessStatusVarSession = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_STATUSVAR_SESSION);
		ObjectNode sessDatiSessionTmp = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_SESSION_TMP);

		for (int i = 0; i < RHCostants.ARRAYVAR_SESSION.length; i++) {

			if (!(sessStatusVarSession).get(RHCostants.ARRAYVAR_SESSION[i]).asBoolean()) {

				sessDatiSessionTmp.put(RHCostants.ARRAYVAR_SESSION[i], message_text);
				sessStatusVarSession.put(RHCostants.ARRAYVAR_SESSION[i], true);

				chiediDati(absSender, user, botSession);

				return;
			}
		}
	}

	public static void eseguiComando(DefaultAbsSender absSender, User user, Session botSession) {

		ObjectNode responseApi = Utils.getResponseApi(
				RHBESessione.save(RHBESessione.preparaRequestSave(user, botSession, absSender)), absSender);

		Utils.inviaMessaggio(absSender, user.getId(), RHCostants.TEXT_ESITO_SESSION_OK);
	}

}