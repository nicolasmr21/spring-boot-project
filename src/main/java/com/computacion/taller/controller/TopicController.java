package com.computacion.taller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.computacion.taller.delegate.TopicDelegate;
import com.computacion.taller.model.TsscTopic;
import com.computacion.taller.service.TopicService;

@Controller
public class TopicController {
	
	@Autowired
	TopicDelegate topicDelegate;

	@GetMapping("/topics/")
	public String index(Model model) {
		model.addAttribute("topics", topicDelegate.findAll());
		return "topics/index";
	}
		
	@GetMapping("/topics/add")
	public String add(Model model) {
		model.addAttribute("tsscTopic", new TsscTopic());
		return "topics/add";
	}
	
	@PostMapping("/topics/add")
	public String add(@Validated TsscTopic topic, BindingResult bd, @RequestParam(value = "action", required = true) String action, Model model) {
			
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscTopic", topic);
			return "topics/add";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			topicDelegate.save(topic);
		}
		return "redirect:/topics/";
	}
	
	@GetMapping("/topics/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {
		model.addAttribute("tsscTopic", topicDelegate.findById(id));
		return "topics/edit";
	}
	
	@PostMapping("/topics/edit/{id}")
	public String edit(@Validated TsscTopic topic, BindingResult bd, @RequestParam(value = "action", required = true) String action, @PathVariable("id") long id,  Model model) {
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscTopic", topic);
			return "topics/edit";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			TsscTopic t = topicDelegate.findById(id);
			t.setName(topic.getName());
			t.setDefaultGroups(topic.getDefaultGroups());
			t.setDefaultSprints(topic.getDefaultSprints());
			t.setGroupPrefix(topic.getGroupPrefix());
			topicDelegate.update(id,t);
		}
		return "redirect:/topics/";
	}
		
	@GetMapping("/topics/showStories/{id}")
	public String showStories(Model model, @PathVariable("id") long id) {
		model.addAttribute("stories", topicDelegate.findStories(id));
		return "topics/story-topic";
	}
	
	@GetMapping("/topics/showGames/{id}")
	public String showGames(Model model, @PathVariable("id") long id) {
		model.addAttribute("games", topicDelegate.findGames(id));
		return "topics/game-topic";
	}
	
	@GetMapping("/topics/remove/{id}")
	public String remove(@PathVariable("id") long id) {
		topicDelegate.delete(id);
		return "redirect:/topics/";
	}
}
