package it.racinghub.fe.rest.external;

import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.racinghub.fe.costants.RHCostants;
import it.racinghub.fe.helper.Utils;

public class RHBESalvaRisultato {

	public static HttpEntity<MultiValueMap<String, Object>> preparaRequest(User user, Session botSession, DefaultAbsSender absSender) {
		
		JsonNode insResultSess = (JsonNode) botSession.getAttribute(RHCostants.KEYSESSION_INSRESULT_TMP);
		
		
		// Crea il multipart request
	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("file", new FileSystemResource(Utils.scaricaFile(absSender, Utils.leggiJsonNodeVar(insResultSess, "idFile"))));
	    body.add("fileName", Utils.leggiJsonNodeVar(insResultSess, "nameFile"));
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    
	    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
	    
		
		return requestEntity;
	}
	
	
	public static ResponseEntity<ObjectNode> execute(HttpEntity<MultiValueMap<String, Object>> requestEntity, String idSessione) {
		
		ResponseEntity<ObjectNode> responseApi = new RestTemplate().exchange(RHCostants.BE_BASEURL + RHCostants.INSRISULTATO_URL,
																				HttpMethod.valueOf(RHCostants.INSRISULTATO_CALLMETHOD),
																				requestEntity, new ParameterizedTypeReference<ObjectNode>() {},
																				idSessione);

//		if(Objects.isNull(responseApi)){
//			responseApi = new ObjectMapper().createObjectNode();
//			responseApi.set("utente", requestApi);
//		}
		
		return responseApi;
	}
}
