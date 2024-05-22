package it.racinghub.fe.rest.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;

public class RHBERecuperaUtente {

	public static ObjectNode preparaRequest(User user) {
		
		ObjectNode requestApi = new ObjectMapper().createObjectNode();
		requestApi.put("userTelegram", "@"+user.getUserName());
		requestApi.put("telegramId", ""+user.getId());
		
		return requestApi;
	}
	
	
	public static ResponseEntity<ObjectNode> execute(ObjectNode requestApi) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().postForEntity(RHCostants.BE_BASEURL + RHCostants.SALVAUTENTE_URL,
																requestApi,
																ObjectNode.class);
		
		
		return responseApi;
		
	}
}
