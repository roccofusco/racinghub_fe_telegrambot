package it.racinghub.fe.rest.external;

import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.helper.Utils;

public class RHBECampionato {

	public static JsonNode preparaRequestUtente(User user, Session botSession, AbsSender absSender) {
		
		Utils.checkUtenteInSession(user, botSession, absSender);
		JsonNode utenteSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_UTENTE);
		
		return utenteSess;
	}
	
	
	public static ResponseEntity<List<ObjectNode>> getByUtente(JsonNode requestApi) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETCAMPIONATO_BYUTENTE_URL,
																				HttpMethod.valueOf(RHCostants.GETCAMPIONATO_BYUTENTE_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				Utils.leggiJsonNodeVar(requestApi, "idUtente"));

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static String preparaRequestCampionato(User user, Session botSession, AbsSender absSender, String keyObj) {
		
		Utils.checkUtenteInSession(user, botSession, absSender);
		JsonNode myChampSess = (JsonNode) botSession.getAttribute(keyObj);
		
		return Utils.leggiJsonNodeVar(myChampSess, "idCampionato");
	}
	
	public static ResponseEntity<ObjectNode> getById(String idCampionato) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETCAMPIONATO_BYID_URL,
																				HttpMethod.valueOf(RHCostants.GETCAMPIONATO_BYID_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<ObjectNode>() {},
																				idCampionato);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static ObjectNode preparaRequestSave(User user, Session botSession, AbsSender absSender) {
		Utils.checkUtenteInSession(user, botSession, absSender);

		ObjectNode requestApi = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_CHAMP_TMP);
		
		JsonNode utenteSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_UTENTE);
		requestApi.set("utente", utenteSess);

//		ObjectNode rules = new ObjectMapper().createObjectNode();
//		requestApi.put("idRegolamento", "1");
//
//		requestApi.set("regolamento", rules);

		
		return requestApi;
	}
	
	
	public static ResponseEntity<ObjectNode> save(ObjectNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().postForEntity(RHCostants.BE_BASEURL + RHCostants.SALVACAMPIONATO_URL,
																requestApi,
																ObjectNode.class);
		
//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//		}
		
		return responseApi;
		
	}
}
