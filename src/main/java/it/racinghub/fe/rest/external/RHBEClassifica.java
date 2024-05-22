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

public class RHBEClassifica {

	public static ResponseEntity<List<ObjectNode>> getClassificaPilotaByCampionato(String idCampionato) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETCLASSPILOTA_BYCAMPIONATO_URL,
																				HttpMethod.valueOf(RHCostants.GETCLASSPILOTA_BYCAMPIONATO_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				idCampionato);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
	
	
	public static ResponseEntity<List<ObjectNode>> getClassificaTeamByCampionato(String idCampionato) {
		
		ResponseEntity<List<ObjectNode>> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.GETCLASSTEAM_BYCAMPIONATO_URL,
																				HttpMethod.valueOf(RHCostants.GETCLASSTEAM_BYCAMPIONATO_CALLMETHOD),
																				null,
																				new ParameterizedTypeReference<List<ObjectNode>>() {},
																				idCampionato);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
}
