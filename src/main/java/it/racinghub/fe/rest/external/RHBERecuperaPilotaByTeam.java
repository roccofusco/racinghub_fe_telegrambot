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
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.helper.Utils;

public class RHBERecuperaPilotaByTeam {

	public static JsonNode preparaRequest(User user, Session botSession, AbsSender absSender) {
		
		Utils.checkPilotaInSession(user, botSession, absSender);
		JsonNode pilota = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_PILOTA);
		JsonNode teamSess = Utils.leggiJsonNodeObj(pilota, "team");

		return teamSess;
	}
	
	
	public static ResponseEntity<List<ObjectNode>> execute(JsonNode requestApi) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETPILOTA_BYTEAM_URL,
																				HttpMethod.valueOf(RHCostants.GETPILOTA_BYTEAM_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				Utils.leggiJsonNodeVar(requestApi, "idTeam"));

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
}
