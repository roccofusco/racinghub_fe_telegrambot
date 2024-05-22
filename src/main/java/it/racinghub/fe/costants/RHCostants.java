package it.racinghub.fe.costants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

import it.racinghub.fe.command.InsResultCommand;
import it.racinghub.fe.command.LeaveTeamCommand;
import it.racinghub.fe.command.SearchMyChampCommand;
import it.racinghub.fe.command.SetChampCommand;
import it.racinghub.fe.command.SetCoDriverTeamCommand;
import it.racinghub.fe.command.SetDriverCommand;
import it.racinghub.fe.command.SetEventCommand;
import it.racinghub.fe.command.SetRegisteredCommand;
import it.racinghub.fe.command.SetSessionCommand;
import it.racinghub.fe.command.SetTeamCommand;
import it.racinghub.fe.command.StartCommand;
import it.racinghub.fe.command.ViewChampStandingsCommand;
import it.racinghub.fe.command.ViewDriverCommand;
import it.racinghub.fe.command.ViewRegisteredCommand;
import it.racinghub.fe.command.ViewTeamCommand;

public class RHCostants {

	private static InputStream input = RHCostants.class.getClassLoader().getResourceAsStream("application.properties");
	private static Properties prop = new Properties() {
//															private static final long serialVersionUID = 7863236545667357684L;
		{
			try {
				load(input);
			} catch (IOException e) {
			}
		}
	};

	/** COMMAND BOT */
	public static final IBotCommand[] ARRAYCOMMAND_ACTIVATE = new IBotCommand[] { new StartCommand(),
			new ViewDriverCommand(), new SetDriverCommand(), new ViewTeamCommand(), new SetTeamCommand(),
			new LeaveTeamCommand(), new SetCoDriverTeamCommand(), new SearchMyChampCommand(), new InsResultCommand(),
			new SetChampCommand(), new SetEventCommand(), new SetSessionCommand(), new ViewChampStandingsCommand(),
			new ViewRegisteredCommand(), new SetRegisteredCommand() };

	public static final String COMMAND_START = "/start";
	public static final String COMMAND_VIEWDRIVER = "/viewdriver", LABEL_VIEWDRIVER = "Scheda Pilota";
	public static final String COMMAND_VIEWTEAM = "/viewteam", LABEL_VIEWTEAM = "Scheda Team";
	public static final String COMMAND_SETDRIVER = "/setdriver", LABEL_SETDRIVER = "Modifica Scheda Pilota";
	public static final String COMMAND_SETTEAM = "/setteam", LABEL_SETTEAM = "Modifica Scheda Team";
	public static final String LABEL_NEWTEAM = "Crea il tuo Team";
	public static final String COMMAND_LEAVETEAM = "/leaveteam", LABEL_LEAVETEAM = "Abbandona Team";
	public static final String COMMAND_INSRESULT = "/insresult", LABEL_INSRESULT = "Importa risultati";
	public static final String COMMAND_SETCODRIVERTEAM = "/setcodriverteam",
			LABEL_SETCODRIVERTEAM = "Modifica Co-pilota Team";
	public static final String COMMAND_SEARCHMYCHAMP = "/searchmychamp", LABEL_SEARCHMYCHAMP = "";
	public static final String COMMAND_SETCHAMP = "/setchampionship", LABEL_SETCHAMP = "Modifica info Campionato";
	public static final String COMMAND_SETEVENT = "/setevent", LABEL_SETEVENT = "Modifica info Evento";
	public static final String COMMAND_SETSESSION = "/setsession", LABEL_SETSESSION = "Modifica info Sessione";
	public static final String LABEL_NEWCHAMP = "**CREA NUOVO CAMPIONATO**";
	public static final String LABEL_NEWEVENT = "**CREA NUOVO EVENTO**";
	public static final String LABEL_NEWSESSION = "**CREA NUOVA SESSIONE**";
	public static final String COMMAND_VIEWCHAMPSTANDINGS = "/viewchampstandings", LABEL_VIEWCHAMPSTANDINGS = "Visualizza classifica campionato";
	public static final String COMMAND_VIEWREGISTERED = "/viewregistered", LABEL_VIEWREGISTERED = "Piloti iscritti";
	public static final String COMMAND_SETREGISTERED = "/setregistered", LABEL_SETREGISTERED = "Inserisci/Modifica Iscritto";

	
	
	/** KEY SESSION */
	public static final String KEYSESSION_COMMAND = "command";
	public static final String KEYSESSION_UTENTE = "utente";
	public static final String KEYSESSION_PILOTA = "pilota";

	public static final String KEYSESSION_PILOTA_TMP = "pilotaTemp";
	public static final String KEYSESSION_STATUSVAR_PILOTA = "statusVarPilota";
	public static final String KEYSESSION_TEAM_TMP = "teamTemp";
	public static final String KEYSESSION_COPILOTATEAM_TMP = "copilotateamTemp";
	public static final String KEYSESSION_STATUSVAR_COPILOTATEAM = "statusVarCopilotateam";
	public static final String KEYSESSION_STATUSVAR_TEAM = "statusVarTeam";
	public static final String KEYSESSION_SEARCHMYCHAMP_TMP = "myChampTemp";
	public static final String KEYSESSION_STATUSVAR_SEARCHMYCHAMP = "statusVarMychamp";
	public static final String KEYSESSION_INSRESULT_TMP = "insResultTemp";
	public static final String KEYSESSION_STATUSVAR_INSRESULT = "statusVarInsresult";
	public static final String KEYSESSION_CHAMP_TMP = "championshipTemp";
	public static final String KEYSESSION_STATUSVAR_CHAMP = "statusVarChampionship";
	public static final String KEYSESSION_EVENT_TMP = "eventTemp";
	public static final String KEYSESSION_STATUSVAR_EVENT = "statusVarEvent";
	public static final String KEYSESSION_SESSION_TMP = "sessionTemp";
	public static final String KEYSESSION_STATUSVAR_SESSION = "statusVarSession";
	public static final String KEYSESSION_CHAMPSTANDINGS_TMP = "standingsTemp";
	public static final String KEYSESSION_STATUSVAR_CHAMPSTANDINGS = "statusVarStandings";
	public static final String KEYSESSION_REGISTERED_TMP = "registeredTemp";
	public static final String KEYSESSION_STATUSVAR_REGISTERED = "statusVarRegistered";
	
	/** INFO TOKEN */
	private static final String cxt_bot = "bot.";
	public static final String BOTUSERNAME = prop.getProperty(cxt_bot + "username");
	public static final String BOTTOKEN = prop.getProperty(cxt_bot + "token");

	/** CONNETTORE BE */
	private static final String cxt_BE = "connectors.rest.racinghubbe.";
	public static final String BE_BASEURL = prop.getProperty(cxt_BE + "serviceBaseURL");

	/** API */
	private static final String cxt_SALVAUTENTE = cxt_BE + "saveusers.";
	public static final String SALVAUTENTE_URL = prop.getProperty(cxt_SALVAUTENTE + "url");
	public static final String SALVAUTENTE_CALLMETHOD = prop.getProperty(cxt_SALVAUTENTE + "callmethod");

	private static final String cxt_GETPILOTA = cxt_BE + "getdrivers.";
	public static final String GETPILOTA_URL = prop.getProperty(cxt_GETPILOTA + "url");
	public static final String GETPILOTA_CALLMETHOD = prop.getProperty(cxt_GETPILOTA + "callmethod");

	private static final String cxt_GETPILOTABYUSER = cxt_BE + "getdriversbyuser.";
	public static final String GETPILOTABYUSER_URL = prop.getProperty(cxt_GETPILOTABYUSER + "url");
	public static final String GETPILOTABYUSER_CALLMETHOD = prop.getProperty(cxt_GETPILOTABYUSER + "callmethod");

	private static final String cxt_SALVACOPILOTATEAM = cxt_BE + "savecodriverteam.";
	public static final String SALVACOPILOTATEAM_URL = prop.getProperty(cxt_SALVACOPILOTATEAM + "url");
	public static final String SALVACOPILOTATEAM_CALLMETHOD = prop.getProperty(cxt_SALVACOPILOTATEAM + "callmethod");

	private static final String cxt_SALVAPILOTA = cxt_BE + "savedrivers.";
	public static final String SALVAPILOTA_URL = prop.getProperty(cxt_SALVAPILOTA + "url");
	public static final String SALVAPILOTA_CALLMETHOD = prop.getProperty(cxt_SALVAPILOTA + "callmethod");

	private static final String cxt_SALVATEAM = cxt_BE + "saveteams.";
	public static final String SALVATEAM_URL = prop.getProperty(cxt_SALVATEAM + "url");
	public static final String SALVATEAM_CALLMETHOD = prop.getProperty(cxt_SALVATEAM + "callmethod");

	private static final String cxt_GETPILOTA_BYTEAM = cxt_BE + "getdriversbyteam.";
	public static final String GETPILOTA_BYTEAM_URL = prop.getProperty(cxt_GETPILOTA_BYTEAM + "url");
	public static final String GETPILOTA_BYTEAM_CALLMETHOD = prop.getProperty(cxt_GETPILOTA_BYTEAM + "callmethod");

	private static final String cxt_GETCAMPIONATO_BYUTENTE = cxt_BE + "getchampionshipsbyutente.";
	public static final String GETCAMPIONATO_BYUTENTE_URL = prop.getProperty(cxt_GETCAMPIONATO_BYUTENTE + "url");
	public static final String GETCAMPIONATO_BYUTENTE_CALLMETHOD = prop
			.getProperty(cxt_GETCAMPIONATO_BYUTENTE + "callmethod");

	private static final String cxt_GETCAMPIONATO_BYID = cxt_BE + "getchampionshipsbyid.";
	public static final String GETCAMPIONATO_BYID_URL = prop.getProperty(cxt_GETCAMPIONATO_BYID + "url");
	public static final String GETCAMPIONATO_BYID_CALLMETHOD = prop.getProperty(cxt_GETCAMPIONATO_BYID + "callmethod");
	
	private static final String cxt_GETCAMPIONATO_BYPILOTAISCRITTO = cxt_BE + "getchampionshipsbydriver.";
	public static final String GETCAMPIONATO_BYPILOTAISCRITTO_URL = prop.getProperty(cxt_GETCAMPIONATO_BYPILOTAISCRITTO + "url");
	public static final String GETCAMPIONATO_BYPILOTAISCRITTO_CALLMETHOD = prop.getProperty(cxt_GETCAMPIONATO_BYPILOTAISCRITTO + "callmethod");

	private static final String cxt_GETPILOTAISCRITTO_BYEVENTO = cxt_BE + "getregisteredeventsbyevent.";
	public static final String GETPILOTAISCRITTO_BYEVENTO_URL = prop.getProperty(cxt_GETPILOTAISCRITTO_BYEVENTO + "url");
	public static final String GETPILOTAISCRITTO_BYEVENTO_CALLMETHOD = prop.getProperty(cxt_GETPILOTAISCRITTO_BYEVENTO + "callmethod");

	private static final String cxt_SALVAPILOTAISCRITTO = cxt_BE + "saveregisteredevents.";
	public static final String SALVAPILOTAISCRITTO_URL = prop.getProperty(cxt_SALVAPILOTAISCRITTO + "url");
	public static final String SALVAPILOTAISCRITTO_CALLMETHOD = prop.getProperty(cxt_GETPILOTAISCRITTO_BYEVENTO + "callmethod");
	
	private static final String cxt_SALVACAMPIONATO = cxt_BE + "savechampionships.";
	public static final String SALVACAMPIONATO_URL = prop.getProperty(cxt_SALVACAMPIONATO + "url");
	public static final String SALVACAMPIONATO_CALLMETHOD = prop.getProperty(cxt_SALVACAMPIONATO + "callmethod");

	private static final String cxt_GETEVENTO_BYCHAMP = cxt_BE + "geteventsbychamp.";
	public static final String GETEVENTO_BYCHAMP_URL = prop.getProperty(cxt_GETEVENTO_BYCHAMP + "url");
	public static final String GETEVENTO_BYCHAMP_CALLMETHOD = prop.getProperty(cxt_GETEVENTO_BYCHAMP + "callmethod");

	private static final String cxt_GETEVENTO_BYID = cxt_BE + "geteventsbyid.";
	public static final String GETEVENTO_BYID_URL = prop.getProperty(cxt_GETEVENTO_BYID + "url");
	public static final String GETEVENTO_BYID_CALLMETHOD = prop.getProperty(cxt_GETEVENTO_BYID + "callmethod");

	private static final String cxt_SALVAEVENTO = cxt_BE + "saveevents.";
	public static final String SALVAEVENTO_URL = prop.getProperty(cxt_SALVAEVENTO + "url");
	public static final String SALVAEVENTO_CALLMETHOD = prop.getProperty(cxt_SALVAEVENTO + "callmethod");
	
	private static final String cxt_GETSESSIONE_BYEVENTO = cxt_BE + "getsessionsbyevent.";
	public static final String GETSESSIONE_BYEVENTO_URL = prop.getProperty(cxt_GETSESSIONE_BYEVENTO + "url");
	public static final String GETSESSIONE_BYEVENTO_CALLMETHOD = prop
			.getProperty(cxt_GETSESSIONE_BYEVENTO + "callmethod");

	private static final String cxt_GETSESSIONE_BYID = cxt_BE + "getsessionsbyid.";
	public static final String GETSESSIONE_BYID_URL = prop.getProperty(cxt_GETSESSIONE_BYID + "url");
	public static final String GETSESSIONE_BYID_CALLMETHOD = prop.getProperty(cxt_GETSESSIONE_BYID + "callmethod");

	private static final String cxt_SALVASESSIONE = cxt_BE + "savesessions.";
	public static final String SALVASESSIONE_URL = prop.getProperty(cxt_SALVASESSIONE + "url");
	public static final String SALVASESSIONE_CALLMETHOD = prop.getProperty(cxt_SALVASESSIONE + "callmethod");
	
	private static final String cxt_INSRISULTATO = cxt_BE + "insresults.";
	public static final String INSRISULTATO_URL = prop.getProperty(cxt_INSRISULTATO + "url");
	public static final String INSRISULTATO_CALLMETHOD = prop.getProperty(cxt_INSRISULTATO + "callmethod");

	private static final String cxt_GETCLASSPILOTA_BYCAMPIONATO = cxt_BE + "getstandingsdriverbychamp.";
	public static final String GETCLASSPILOTA_BYCAMPIONATO_URL = prop.getProperty(cxt_GETCLASSPILOTA_BYCAMPIONATO + "url");
	public static final String GETCLASSPILOTA_BYCAMPIONATO_CALLMETHOD = prop.getProperty(cxt_GETCLASSPILOTA_BYCAMPIONATO + "callmethod");
	
	private static final String cxt_GETCLASSTEAM_BYCAMPIONATO = cxt_BE + "getstandingsteambychamp.";
	public static final String GETCLASSTEAM_BYCAMPIONATO_URL = prop.getProperty(cxt_GETCLASSTEAM_BYCAMPIONATO + "url");
	public static final String GETCLASSTEAM_BYCAMPIONATO_CALLMETHOD = prop.getProperty(cxt_GETCLASSTEAM_BYCAMPIONATO + "callmethod");
	
	/** TESTI */
	public static final String A_CAPO = "\n";

	/** START COMMAND */
	public static final String PLACEHOLDER_USERNAME = "\\[USERNAME\\]";
	public static final String TEXT_STARTCOMMAND = "Benvenuto " + "[USERNAME]"
			+ ", apri il menu dei comandi per una nuova azione";

	public static final String PLACEHOLDER_CID_CAMP = "\\[CID_CAMP\\]";
	public static final String PLACEHOLDER_NOME_CAMP = "\\[NOME_CAMP\\]";
	public static final String PLACEHOLDER_DINIZIO_CAMP = "\\[DINIZIO_CAMP\\]";
	public static final String PLACEHOLDER_DFINE_CAMP = "\\[DFINE_CAMP\\]";
	public static final String PLACEHOLDER_PROPR_CAMP = "\\[PROPR_CAMP\\]";
	public static final String TEXT_VIEWCAMP = "CAMPIONATO " + "C.ID: " + "[CID_CAMP]" + A_CAPO + "" + "[NOME_CAMP]"
			+ A_CAPO + "Inizio: " + "[DINIZIO_CAMP]" + A_CAPO + "Fine: " + "[DFINE_CAMP]" + A_CAPO + "By: "
			+ "[PROPR_CAMP]";

	public static final String PLACEHOLDER_EID_EVENTO = "\\[EID_EVENTO\\]";
	public static final String PLACEHOLDER_NOME_EVENTO = "\\[NOME_EVENTO\\]";
	public static final String PLACEHOLDER_DATA_EVENTO = "\\[DATA_EVENTO\\]";
	public static final String PLACEHOLDER_QUOTAISCR_EVENTO = "\\[QUOTAISCR_EVENTO\\]";
	public static final String PLACEHOLDER_DSCANDENZAISCR_EVENTO = "\\[DSCANDENZAISCR_EVENTO\\]";
	public static final String PLACEHOLDER_NOME_PISTA = "\\[NOME_PISTA\\]";
	public static final String PLACEHOLDER_INDIRIZZO_PISTA = "\\[INDIRIZZO_PISTA\\]";
	public static final String PLACEHOLDER_CITTA_PISTA = "\\[CITTA_PISTA\\]";
	public static final String PLACEHOLDER_PROVINCIA_PISTA = "\\[PROVINCIA_PISTA\\]";
	public static final String PLACEHOLDER_CAP_PISTA = "\\[CAP_PISTA\\]";
	public static final String TEXT_VIEWEVENTO = "EVENTO " + "E.ID: " + "[EID_EVENTO]" + A_CAPO + "" + "[NOME_EVENTO]"
			+ A_CAPO + "Data: " + "[DATA_EVENTO]" + A_CAPO + "Quota Iscrizione: " + "[QUOTAISCR_EVENTO]" + "€" + A_CAPO
			+ "Scadenza Iscrizione: " + "[DSCANDENZAISCR_EVENTO]" + A_CAPO + "Pista: " + "[NOME_PISTA]" + A_CAPO
			+ "[INDIRIZZO_PISTA]" + ", " + "[CITTA_PISTA]" + ", " + "[PROVINCIA_PISTA]" + ", " + "[CAP_PISTA]";

	public static final String PLACEHOLDER_SID_SESSIONE = "\\[SID_SESSIONE\\]";
	public static final String PLACEHOLDER_NOME_SESSIONE = "\\[NOME_SESSIONE\\]";
	public static final String PLACEHOLDER_DESC_TIPOSESSIONE = "\\[DESC_TIPOSESSIONE\\]";
	public static final String PLACEHOLDER_VALDURATA_SESSIONE = "\\[VALDURATA_SESSIONE\\]";
	public static final String PLACEHOLDER_UDURATA_SESSIONE = "\\[UDURATA_SESSIONE\\]";
	public static final String PLACEHOLDER_PERCDURATA_SESSIONE = "\\[PERCDURATA_SESSIONE\\]";
	public static final String PLACEHOLDER_NOTE_SESSIONE = "\\[NOTE_SESSIONE\\]";

	public static final String TEXT_VIEWSESSIONE = "SESSIONE " + "S.ID: " + "[SID_SESSIONE]" + A_CAPO
			+ "[NOME_SESSIONE]" + " - " + "[DESC_TIPOSESSIONE]" + A_CAPO + "Durata: " + "[VALDURATA_SESSIONE]" + " "
			+ "[UDURATA_SESSIONE]" + A_CAPO + "Completata: " + "[PERCDURATA_SESSIONE]" + "%" + A_CAPO + "Note:" + A_CAPO
			+ "<< " + "[NOTE_SESSIONE]" + " >>";

	/** VIEWDRIVER COMMAND */
	public static final String PLACEHOLDER_DID_PILOTA = "\\[DID_PILOTA\\]";
	public static final String PLACEHOLDER_NOME_PILOTA = "\\[NOME_PILOTA\\]";
	public static final String PLACEHOLDER_COGNOME_PILOTA = "\\[COGNOME_PILOTA\\]";
	public static final String PLACEHOLDER_DATANASCITA_PILOTA = "\\[DATANASCITA_PILOTA\\]";
	public static final String PLACEHOLDER_PESOKG_PILOTA = "\\[PESOKG_PILOTA\\]";
	public static final String PLACEHOLDER_CELLULARE_PILOTA = "\\[CELLULARE_PILOTA\\]";
	public static final String TEXT_VIEWDRIVERCOMMAND = "SCHEDA PILOTA" + A_CAPO + "D.ID: " + "[DID_PILOTA]" + A_CAPO
			+ "Nome: " + "[NOME_PILOTA]" + A_CAPO + "Cognome: " + "[COGNOME_PILOTA]" + A_CAPO + "Data nascita: "
			+ "[DATANASCITA_PILOTA]" + A_CAPO + "Peso Kg: " + "[PESOKG_PILOTA]" + A_CAPO + "Cellulare: "
			+ "[CELLULARE_PILOTA]";

	/** VIEWTEAM COMMAND */
	public static final String PLACEHOLDER_TID_TEAM = "\\[TID_TEAM\\]";
	public static final String PLACEHOLDER_PROPR_TEAM = "\\[PROP_TEAM\\]";
	public static final String PLACEHOLDER_NOME_TEAM = "\\[NOME_TEAM\\]";
	public static final String PLACEHOLDER_COLORE_TEAM = "\\[COLORE_TEAM\\]";
	public static final String PLACEHOLDER_LISTPILOTI_TEAM = "\\[LISTPILOTI_TEAM\\]";
	public static final String TEXT_VIEWTEAMCOMMAND = "SCHEDA TEAM" + A_CAPO + "T.ID: " + "[TID_TEAM]" + A_CAPO
			+ "Proprietario: " + "[PROP_TEAM]" + A_CAPO + "Nome: " + "[NOME_TEAM]" + A_CAPO + "Colori: "
			+ "[COLORE_TEAM]" + A_CAPO + "Piloti del Team" + A_CAPO + "[LISTPILOTI_TEAM]";

	/** SETDRIVER COMMAND */
	public static final String TEXT_REQ_DRIVER_DATA = "Procedi a inserire i dati Pilota.";
	public static final String TEXT_REQ_DRIVER_NOME = "Nome:";
	public static final String TEXT_REQ_DRIVER_COGNOME = "Cognome:";
	public static final String TEXT_REQ_DRIVER_DATANASCITA = "Data di nascita (dd/MM/yyyy):";
	public static final String TEXT_REQ_DRIVER_PESOKG = "Peso (Kg):";
	public static final String TEXT_REQ_DRIVER_CELLULARE = "Cellulare:";
	public static final String[] ARRAYTEXT_REQ_DRIVER = new String[] { TEXT_REQ_DRIVER_NOME, TEXT_REQ_DRIVER_COGNOME,
			TEXT_REQ_DRIVER_DATANASCITA, TEXT_REQ_DRIVER_PESOKG, TEXT_REQ_DRIVER_CELLULARE };
	public static final String[] ARRAYVAR_PILOTA = new String[] { "nome", "cognome", "dataNascita", "pesoKg",
			"cellulare" };

	/** SETTEAM COMMAND */
	public static final String TEXT_REQ_TEAM_DATA = "Procedi a inserire i dati Team.";
	public static final String TEXT_REQ_TEAM_NOME = "Nome:";
	public static final String TEXT_REQ_TEAM_COGNOME = "Colori:";
	public static final String[] ARRAYTEXT_REQ_TEAM = new String[] { TEXT_REQ_TEAM_NOME, TEXT_REQ_TEAM_COGNOME };
	public static final String[] ARRAYVAR_TEAM = new String[] { "nome", "colore" };

	/** LEAVETEAM COMMAND */
	public static final String TEXT_ESITO_LEAVETEAM_OK = "Team abbandonato con successo!";

	/** SETCODRIVERTEAM COMMAND */
	public static final String TEXT_ESITO_SETCODRIVERTEAM_OK = "Co-pilota assegnato con successo!";
	public static final String TEXT_REQ_CODRIVERTEAM_DATA = "Procedi a inserire il codice D.ID del pilota da aggiungere al Team.";
	public static final String TEXT_REQ_CODRIVERTEAM_DID = "D.ID:";
	public static final String[] ARRAYTEXT_REQ_CODRIVERTEAM = new String[] { TEXT_REQ_CODRIVERTEAM_DID };
	public static final String[] ARRAYVAR_CODRIVERTEAM = new String[] { "didCodriver" };

	/** SEARCHMYCHAMP COMMAND */
	public static final String TEXT_ESITO_SEARCHMYCHAMP_OK = "Risultati caricati con successo!";
	public static final String TEXT_REQ_SEARCHMYCHAMP_DATA = "Seleziona campionato, evento, sessione per poter editare.";
	public static final String TEXT_REQ_SEARCHMYCHAMP_CAMPIONATO = "Campionati:";
	public static final String TEXT_REQ_SEARCHMYCHAMP_EVENTO = "Eventi:";
	public static final String TEXT_REQ_SEARCHMYCHAMP_SESSIONE = "Sessioni:";
//	public static final String TEXT_REQ_SEARCHMYCHAMP_FILE = "Carica il file dei Risultati:";

	public static final String[] ARRAYTEXT_REQ_SEARCHMYCHAMP = new String[] { TEXT_REQ_SEARCHMYCHAMP_CAMPIONATO,
			TEXT_REQ_SEARCHMYCHAMP_EVENTO, TEXT_REQ_SEARCHMYCHAMP_SESSIONE/** , TEXT_REQ_SEARCHMYCHAMP_FILE */
	};
	public static final String[] ARRAYVAR_SEARCHMYCHAMP = new String[] { "idCampionato", "idEvento",
			"idSessione" /** , "idFile" */
	};

	/** INSRESULT COMMAND */
	public static final String TEXT_REQ_INSRESULT_SID = "S.ID della Sessione:";
	public static final String TEXT_REQ_INSRESULT_FILE = "Carica il file dei Risultati:";
	public static final String[] ARRAYTEXT_REQ_INSRESULT = new String[] { TEXT_REQ_INSRESULT_SID,
			TEXT_REQ_INSRESULT_FILE };
	public static final String[] ARRAYVAR_INSRESULT = new String[] { "idSessione", "idFile", "nameFile" };

	/** SETCHAMP COMMAND */
	public static final String TEXT_ESITO_CHAMP_OK = "Campionato salvato con successo!";
	public static final String TEXT_REQ_CHAMP_CID = "C.ID del Campionato:";
	public static final String TEXT_REQ_CHAMP_NOME = "Nome:";
	public static final String TEXT_REQ_CHAMP_DINIZIO = "Data inizio (dd/MM/yyyy):";
	public static final String TEXT_REQ_CHAMP_DFINE = "Data fine (dd/MM/yyyy):";
	public static final String[] ARRAYTEXT_REQ_CHAMP = new String[] { TEXT_REQ_CHAMP_CID, TEXT_REQ_CHAMP_NOME,
			TEXT_REQ_CHAMP_DINIZIO, TEXT_REQ_CHAMP_DFINE };
	public static final String[] ARRAYVAR_CHAMP = new String[] { "idCampionato", "nome", "dataInizio", "dataFine" };

	/** SETEVENT COMMAND */
	public static final String TEXT_ESITO_EVENT_OK = "Evento salvato con successo!";
	public static final String TEXT_REQ_EVENT_CID = "C.ID del Campionato:";
	public static final String TEXT_REQ_EVENT_EID = "E.ID dell'Evento:";
	public static final String TEXT_REQ_EVENT_NOME = "Nome:";
	public static final String TEXT_REQ_EVENT_DINIZIO = "Data e ora inizio (dd/MM/yyyy HH:mm:ss):";
	public static final String TEXT_REQ_EVENT_QUOTAISCR = "Quota iscrizione €:";
	public static final String TEXT_REQ_EVENT_DSCADENZAISCR = "Data e ora scadenza iscrizione (dd/MM/yyyy HH:mm:ss):";
	public static final String TEXT_REQ_EVENT_TID = "T.ID della Pista:";

	public static final String[] ARRAYTEXT_REQ_EVENT = new String[] { TEXT_REQ_EVENT_CID, TEXT_REQ_EVENT_EID, TEXT_REQ_EVENT_NOME,
			TEXT_REQ_EVENT_DINIZIO, TEXT_REQ_EVENT_QUOTAISCR, TEXT_REQ_EVENT_DSCADENZAISCR, TEXT_REQ_EVENT_TID
			 };
	public static final String[] ARRAYVAR_EVENT = new String[] { "idCampionato", "idEvento", "nome", "dataInizio", "quotaIscrizione",
			"scadenzaIscrizione", "idPista" };
	
	/** SETSESSION COMMAND */
	public static final String TEXT_ESITO_SESSION_OK = "Sessione salvata con successo!";
	public static final String TEXT_REQ_SESSION_CID = "C.ID del Campionato:";
	public static final String TEXT_REQ_SESSION_EID = "E.ID dell'Evento:";
	public static final String TEXT_REQ_SESSION_SID = "S.ID della Sessione:";
	public static final String TEXT_REQ_SESSION_NOME = "Nome:";
	public static final String TEXT_REQ_SESSION_TIPOSESS = "Tipo sessione (Q1/GR1):";
	public static final String TEXT_REQ_SESSION_DURATA = "Durata:";
	public static final String TEXT_REQ_SESSION_UNITADURATA = "Unita durata (MIN/ORA/LAP):";
	public static final String TEXT_REQ_SESSION_PERCENTDURATA = "Completamento sessione (%):";
	public static final String TEXT_REQ_SESSION_NOTE = "Note:";
	public static final String[] ARRAYTEXT_REQ_SESSION = new String[] { TEXT_REQ_SESSION_CID, TEXT_REQ_SESSION_EID, TEXT_REQ_SESSION_SID,
			TEXT_REQ_SESSION_NOME, TEXT_REQ_SESSION_TIPOSESS, TEXT_REQ_SESSION_DURATA, TEXT_REQ_SESSION_UNITADURATA, TEXT_REQ_SESSION_PERCENTDURATA,
			TEXT_REQ_SESSION_NOTE };
	public static final String[] ARRAYVAR_SESSION = new String[] { "idCampionato", "idEvento", "idSessione", "nome", "idTipoSessione", "durata",
			"unitaDurata", "percentDurata", "note" };

	/** VIEWCHAMPSTANDINGS COMMAND */
	public static final String TEXT_REQ_CHAMPSTANDINGS_CAMPIONATO = "Campionati a cui partecipi:";
	public static final String TEXT_REQ_CHAMPSTANDINGS_TIPOCLASS = "Quale tipo di Classifica visualizzare?";
	public static final String[] ARRAYTEXT_REQ_CHAMPSTANDINGS = new String[] { TEXT_REQ_CHAMPSTANDINGS_CAMPIONATO,TEXT_REQ_CHAMPSTANDINGS_TIPOCLASS};
	public static final String[] ARRAYVAR_CHAMPSTANDINGS = new String[] { "idCampionato", "tipoClassifica"};
	public static final String TIPOCLASS_PILOTA_VALUE = "D";
	public static final String TIPOCLASS_PILOTA_LABEL = "Classifica PILOTI";
	public static final String TIPOCLASS_TEAM_VALUE = "T";
	public static final String TIPOCLASS_TEAM_LABEL = "Classifica TEAM";
	
	public static final String PLACEHOLDER_POS_STANDINGS = "\\[POS_DS\\]";
	public static final String PLACEHOLDER_PILOTA_STANDINGS = "\\[PILOTA_DS\\]";
	public static final String PLACEHOLDER_TEAM_STANDINGS = "\\[TEAM_DS\\]";
	public static final String PLACEHOLDER_PUNTI_STANDINGS = "\\[PT_DS\\]";
	public static final String TEXT_DRIVERSTANDINGS = "[POS_DS].\t[PILOTA_DS]\t([TEAM_DS])\t[PT_DS]pts" + A_CAPO;
	public static final String TEXT_TEAMSTANDINGS = "[POS_DS].\t[TEAM_DS]\t[PT_DS]pts" + A_CAPO;
	
	/** SETREGISTERED COMMAND */
	public static final String TEXT_ESITO_REGISTERED_OK = "Pilota iscritto correttamente!";
	public static final String TEXT_REQ_REGISTERED_DATA = "Procedi a inserire i dati Team.";
	public static final String TEXT_REQ_REGISTERED_EID = "E.ID dell'Evento:";
	public static final String TEXT_REQ_REGISTERED_DID = "D.ID del Pilota:";
	public static final String TEXT_REQ_REGISTERED_TARGA = "Targa adottata per questo evento:";
	public static final String[] ARRAYTEXT_REQ_REGISTERED = new String[] { TEXT_REQ_REGISTERED_EID,TEXT_REQ_REGISTERED_DID,TEXT_REQ_REGISTERED_TARGA};
	public static final String[] ARRAYVAR_REGISTERED = new String[] { "idEvento", "idPilota", "targaVeicolo"};
	

}
