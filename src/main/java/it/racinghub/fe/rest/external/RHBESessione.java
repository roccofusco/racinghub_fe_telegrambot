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

public class RHBESessione {

	public static String preparaRequestEvento(User user, Session botSession, AbsSender absSender) {
		
		JsonNode myChampSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP);
		
		return Utils.leggiJsonNodeVar(myChampSess, "idEvento");
	}
	
	
	public static ResponseEntity<List<ObjectNode>> getByEvento(String idEvento) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETSESSIONE_BYEVENTO_URL,
																				HttpMethod.valueOf(RHCostants.GETSESSIONE_BYEVENTO_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				idEvento);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static String preparaRequestSessione(User user, Session botSession, AbsSender absSender) {
		
		Utils.checkUtenteInSession(user, botSession, absSender);
		JsonNode myChampSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_SEARCHMYCHAMP_TMP);
		
		return Utils.leggiJsonNodeVar(myChampSess, "idSessione");
	}
	
	public static ResponseEntity<ObjectNode> getById(String idSessione) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETSESSIONE_BYID_URL,
																				HttpMethod.valueOf(RHCostants.GETSESSIONE_BYID_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<ObjectNode>() {},
																				idSessione);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static ObjectNode preparaRequestSave(User user, Session botSession, AbsSender absSender) {
		Utils.checkUtenteInSession(user, botSession, absSender);

		ObjectNode requestApi = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_SESSION_TMP);
		
		ObjectNode objEvento = new ObjectMapper().createObjectNode();
		objEvento.put("idEvento", Utils.leggiJsonNodeVar(requestApi, "idEvento"));
		requestApi.set("evento", objEvento);

		ObjectNode objTipoSessione = new ObjectMapper().createObjectNode();
		objTipoSessione.put("idTipoSessione", Utils.leggiJsonNodeVar(requestApi, "idTipoSessione"));
		requestApi.set("tipoSessione", objTipoSessione);
		
		return requestApi;
	}
	
	
	public static ResponseEntity<ObjectNode> save(ObjectNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().postForEntity(RHCostants.BE_BASEURL + RHCostants.SALVASESSIONE_URL,
																requestApi,
																ObjectNode.class);
		
//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//		}
		
		return responseApi;
		
	}
}
