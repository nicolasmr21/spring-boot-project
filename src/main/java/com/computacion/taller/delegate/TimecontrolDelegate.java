package com.computacion.taller.delegate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscTimecontrol;

@Component
public class TimecontrolDelegate {

	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${resource.timecontrols}")
	 private String resource;
	 
	 @Value("${resource.timecontrols}/{id}")
	 private String idResource;
	 
	 @Value("${resource.timecontrols}/{id}/game")
	 private String gameResource;
	
	 
	 public List<TsscTimecontrol> findAll() {
		return Arrays.stream(restTemplate.getForObject(resource, TsscTimecontrol[].class)).collect(Collectors.toList());
	 }
	 
	 public TsscTimecontrol findById(long id) {
		return restTemplate.getForObject(idResource, TsscTimecontrol.class, id);
	 }
	 
	 public TsscGame findGame(long id) {
		return restTemplate.getForObject(gameResource, TsscGame.class, id);
	 }
	 
	 public void save(TsscTimecontrol timeControl) {
		restTemplate.postForObject(resource, timeControl, TsscTimecontrol.class);
	 }
	 
	 public void update(long id, TsscTimecontrol timeControl) {
		restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(timeControl), TsscTimecontrol.class, id).getBody();
	 }
	 
	 public void delete(long id) {
		restTemplate.delete(idResource, id);
	 }

	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setIdResource(String idResource) {
		this.idResource = idResource;
	}

	public void setGameResource(String gameResource) {
		this.gameResource = gameResource;
	}
	 
	 
}
