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

public class RHBEIscrizioneEvento {

	public static JsonNode preparaRequestPilota(User user, Session botSession, AbsSender absSender) {
		
		Utils.checkPilotaInSession(user, botSession, absSender);
		JsonNode pilotaSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_PILOTA);

		return pilotaSess;
	}
	
	
	public static ResponseEntity<List<ObjectNode>> getChampByPilotaIscritto(JsonNode requestApi) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETCAMPIONATO_BYPILOTAISCRITTO_URL,
																				HttpMethod.valueOf(RHCostants.GETCAMPIONATO_BYPILOTAISCRITTO_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				Utils.leggiJsonNodeVar(requestApi, "idPilota"));

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static ResponseEntity<List<ObjectNode>> getPilotaIscrittoByPilotaIscritto(String idEvento) {
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETPILOTAISCRITTO_BYEVENTO_URL,
																				HttpMethod.valueOf(RHCostants.GETPILOTAISCRITTO_BYEVENTO_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				idEvento);
		return responseApi;
	}
	
	public static ObjectNode preparaRequestSave(User user, Session botSession, AbsSender absSender) {

		ObjectNode requestApi = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_REGISTERED_TMP);
		
		ObjectNode objEvento = new ObjectMapper().createObjectNode();
		objEvento.put("idEvento", Utils.leggiJsonNodeVar(requestApi, "idEvento"));
		requestApi.set("evento", objEvento);

		ObjectNode objPilota = new ObjectMapper().createObjectNode();
		objPilota.put("idPilota", Utils.leggiJsonNodeVar(requestApi, "idPilota"));
		requestApi.set("pilota", objPilota);
		
		return requestApi;
	}
	
	public static ResponseEntity<ObjectNode> save(ObjectNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().postForEntity(RHCostants.BE_BASEURL + RHCostants.SALVAPILOTAISCRITTO_URL,
																requestApi,
																ObjectNode.class);
		
		return responseApi;
	}
}
