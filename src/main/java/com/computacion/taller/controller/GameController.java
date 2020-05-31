package com.computacion.taller.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.computacion.taller.delegate.GameDelegate;
import com.computacion.taller.delegate.TopicDelegate;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscTopic;

@Controller
public class GameController {
	
	@Autowired
	GameDelegate gameDelegate;
	@Autowired
	TopicDelegate topicDelegate;

	@GetMapping("/games/")
	public String index(Model model) {
		model.addAttribute("games", gameDelegate.findAll());
		return "games/index";
	}
	
	@GetMapping("/games/query")
	public String query(Model model) {
		model.addAttribute("games", gameDelegate.findAll());
		TsscGame k = new TsscGame();
		k.setScheduledDate(LocalDate.of(2020, 3, 1));
		model.addAttribute("dummy", k);
		return "games/query";
	}
	
	@PostMapping("/games/query")
	public String query(TsscGame t, BindingResult bd, @RequestParam(value = "action", required = true) String action,  Model model) {
		model.addAttribute("games", gameDelegate.findByDate(t.getScheduledDate()));
		model.addAttribute("dummy", t);
		
		return "games/query";
	}
		
	@GetMapping("/games/add")
	public String add(Model model) {
		model.addAttribute("tsscGame", new TsscGame());
		List<TsscTopic> topics = topicDelegate.findAll();
		TsscTopic x = new TsscTopic();
		x.setName("Ninguno");
		x.setDescription("Vacio");
		topics.add(x);
		model.addAttribute("topics", topics);
		return "games/add";
	}
	
	
	@PostMapping("/games/add")
	public String add(@Validated TsscGame game, BindingResult bd, @RequestParam(value = "action", required = true) String action, Model model) {
			
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscGame", game);
			model.addAttribute("topics", topicDelegate.findAll());
			return "games/add";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			gameDelegate.save(game);
		}
		return "redirect:/games/";
	}
	
	@GetMapping("/games/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {
		model.addAttribute("tsscGame", gameDelegate.findById(id));
		List<TsscTopic> topics = topicDelegate.findAll();
		TsscTopic x = new TsscTopic();
		x.setName("Ninguno");
		x.setDescription("Vacio");
		topics.add(x);
		model.addAttribute("topics", topics);
		return "games/edit";
	}
	
	@PostMapping("/games/edit/{id}")
	public String edit(@Validated TsscGame game, BindingResult bd, @RequestParam(value = "action", required = true) String action, @PathVariable("id") long id,  Model model) {
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscGame", game);
			List<TsscTopic> topics = topicDelegate.findAll();
			TsscTopic x = new TsscTopic();
			x.setName("Ninguno");
			x.setDescription("Vacio");
			topics.add(x);
			model.addAttribute("topics", topics);
			return "games/edit";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			gameDelegate.update(id, game);
		}
		return "redirect:/games/";
	}
		

	@GetMapping("/games/showStories/{id}")
	public String showStories(Model model, @PathVariable("id") long id) {
		model.addAttribute("stories", gameDelegate.findStories(id));
		return "games/stories-game";
	}
	
	@GetMapping("/games/showTopic/{id}")
	public String showTopic(Model model, @PathVariable("id") long id) {
		TsscTopic topic = gameDelegate.findTopic(id);
		List<TsscTopic> t = new ArrayList<>();
		if(topic!=null) {
			t.add(topic);
			model.addAttribute("topics", t);
		}
		return "games/topic-game";
	}
	
	
	@GetMapping("/games/remove/{id}")
	public String remove(@PathVariable("id") long id) {
		gameDelegate.delete(id);
		return "redirect:/games/";
	}
}
