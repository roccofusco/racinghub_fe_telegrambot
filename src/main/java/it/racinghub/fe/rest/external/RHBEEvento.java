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

public class RHBEEvento {

	public static String preparaRequestCampionato(User user, Session botSession, AbsSender absSender) {
		
		JsonNode myChampSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP);
		
		return Utils.leggiJsonNodeVar(myChampSess, "idCampionato");
	}
	
	
	public static ResponseEntity<List<ObjectNode>> getByCampionato(String idCampionato) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETEVENTO_BYCHAMP_URL,
																				HttpMethod.valueOf(RHCostants.GETEVENTO_BYCHAMP_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				idCampionato);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static String preparaRequestEvento(User user, Session botSession, AbsSender absSender) {
		
		Utils.checkUtenteInSession(user, botSession, absSender);
		JsonNode myChampSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP);
		
		return Utils.leggiJsonNodeVar(myChampSess, "idEvento");
	}
	
	public static ResponseEntity<ObjectNode> getById(String idEvento) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETEVENTO_BYID_URL,
																				HttpMethod.valueOf(RHCostants.GETEVENTO_BYID_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<ObjectNode>() {},
																				idEvento);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static ObjectNode preparaRequestSave(User user, Session botSession, AbsSender absSender) {
		Utils.checkUtenteInSession(user, botSession, absSender);

		ObjectNode requestApi = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_EVENT_TMP);
		
		ObjectNode objChamp = new ObjectMapper().createObjectNode();
		objChamp.put("idCampionato", Utils.leggiJsonNodeVar(requestApi, "idCampionato"));
		requestApi.set("campionato", objChamp);

		ObjectNode objPista = new ObjectMapper().createObjectNode();
		objPista.put("idPista", Utils.leggiJsonNodeVar(requestApi, "idPista"));
		requestApi.set("pista", objPista);
		
		return requestApi;
	}
	
	
	public static ResponseEntity<ObjectNode> save(ObjectNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().postForEntity(RHCostants.BE_BASEURL + RHCostants.SALVAEVENTO_URL,
																requestApi,
																ObjectNode.class);
		
//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//		}
		
		return responseApi;
		
	}
}
