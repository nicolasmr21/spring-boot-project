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
import com.computacion.taller.model.TsscStory;
import com.computacion.taller.model.TsscTopic;

@Component
public class TopicDelegate {

	 @Autowired
	 private RestTemplate restTemplate;
	 
	 @Value("${resource.topics}")
	 private String resource;
	 
	 @Value("${resource.topics}/{id}")
	 private String idResource;
	 
	 @Value("${resource.topics}/{id}/games")
	 private String gamesResource;
	
	 @Value("${resource.topics}/{id}/stories")
	 private String storiesResource;
	 
	 public List<TsscTopic> findAll() {
		return Arrays.stream(restTemplate.getForObject(resource, TsscTopic[].class)).collect(Collectors.toList());
	 }
	 
	 public TsscTopic findById(long id) {
		return restTemplate.getForObject(idResource, TsscTopic.class, id);
	 }
	 
	 public List<TsscGame> findGames(long id) {
		return Arrays.stream(restTemplate.getForObject(gamesResource, TsscGame[].class, id)).collect(Collectors.toList());
	 }
	 
	 public List<TsscStory> findStories(long id) {
		return Arrays.stream(restTemplate.getForObject(storiesResource, TsscStory[].class, id)).collect(Collectors.toList());
	 }
	  
	 public void save(TsscTopic topic) {
		restTemplate.postForObject(resource, topic, TsscTopic.class);
	 }
	 
	 public void update(long id, TsscTopic topic) {
		restTemplate.exchange(idResource, HttpMethod.PUT, new HttpEntity<>(topic), TsscTopic.class, id).getBody();
	 }
	 
	 public void delete(long id) {
		restTemplate.delete(idResource, id);
	 }
}
