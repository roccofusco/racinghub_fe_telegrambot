package it.racinghub.fe.rest.external;

import org.apache.shiro.session.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.helper.Utils;

public class RHBERecuperaPilota {

	public static JsonNode preparaRequest(User user, Session botSession, AbsSender absSender) {
		
		Utils.checkUtenteInSession(user, botSession, absSender);
		JsonNode utenteSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_UTENTE);
		
		return utenteSess;
	}
	
	
	public static ResponseEntity<ObjectNode> execute(JsonNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().getForEntity(RHCostants.BE_BASEURL + RHCostants.GETPILOTABYUSER_URL,
																ObjectNode.class, Utils.leggiJsonNodeVar(requestApi, "idUtente"));

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	public static ResponseEntity<ObjectNode> executeByIdPilota(String idPilota) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().getForEntity(RHCostants.BE_BASEURL + RHCostants.GETPILOTA_URL,
																ObjectNode.class, idPilota);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
}
