package it.racinghub.fe.rest.external;

import org.apache.shiro.session.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.helper.Utils;

public class RHBESalvaTeam {

	public static ObjectNode preparaRequest(User user, Session botSession) {
		
		ObjectNode requestApi = (ObjectNode) botSession.getAttribute(RHCostants.KEYSESSION_TEAM_TMP);
		
		Utils.checkUtenteInSession(user, botSession, null);
		JsonNode utenteSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_UTENTE);
		requestApi.set("utente", utenteSess);
		
		return requestApi;
	}
	
	
	public static ResponseEntity<ObjectNode> execute(ObjectNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().postForEntity(RHCostants.BE_BASEURL + RHCostants.SALVATEAM_URL,
																requestApi,
																ObjectNode.class);
		
//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//		}
		
		return responseApi;
		
	}
}
