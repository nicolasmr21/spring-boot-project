package com.computacion.taller.controller;

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
import com.computacion.taller.delegate.TimecontrolDelegate;
import com.computacion.taller.model.TsscGame;
import com.computacion.taller.model.TsscTimecontrol;

@Controller
public class TimecontrolController {
	
	@Autowired
	TimecontrolDelegate timecontrolDelegate;
	@Autowired
	GameDelegate gameService;

	@GetMapping("/timecontrols/")
	public String index(Model model) {
		model.addAttribute("timecontrols", timecontrolDelegate.findAll());
		return "timecontrols/index";
	}
		
	@GetMapping("/timecontrols/add")
	public String add(Model model) {
		model.addAttribute("tsscTimecontrol", new TsscTimecontrol());
		model.addAttribute("games", gameService.findAll());
		return "timecontrols/add";
	}
	
	@PostMapping("/timecontrols/add")
	public String add(@Validated TsscTimecontrol timecontrol, BindingResult bd, @RequestParam(value = "action", required = true) String action, Model model) {
			
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscTimecontrol", timecontrol);
			model.addAttribute("games", gameService.findAll());
			return "timecontrols/add";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			timecontrolDelegate.save(timecontrol);
		}
		return "redirect:/timecontrols/";
	}
	
	@GetMapping("/timecontrols/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {
		model.addAttribute("tsscTimecontrol", timecontrolDelegate.findById(id));
		model.addAttribute("games", gameService.findAll());
		return "timecontrols/edit";
	}
	
	@PostMapping("/timecontrols/edit/{id}")
	public String edit(@Validated TsscTimecontrol timecontrol, BindingResult bd, @RequestParam(value = "action", required = true) String action, @PathVariable("id") long id,  Model model) {
		if(bd.hasErrors()&&!action.equalsIgnoreCase("Cancel")) {
			model.addAttribute("tsscTimecontrol", timecontrol);
			model.addAttribute("games", gameService.findAll());
			return "topics/edit";
		}
		if(!action.equalsIgnoreCase("Cancel")) {
			TsscTimecontrol t = timecontrolDelegate.findById(id);
			t.setType(timecontrol.getType());
			t.setState(timecontrol.getState());
			t.setOrder(timecontrol.getOrder());
			t.setTsscGame(timecontrol.getTsscGame());
			timecontrolDelegate.update(id, t);
		}
		return "redirect:/timecontrols/";
	}
	
	@GetMapping("/timecontrols/showGame/{id}")
	public String showGame(Model model, @PathVariable("id") long id) {
		List<TsscGame> g = new ArrayList<>();
		g.add(timecontrolDelegate.findGame(id));
		model.addAttribute("games", g);
		return "timecontrols/game-timecontrol";
	}
	
	@GetMapping("/timecontrols/remove/{id}")
	public String remove(@PathVariable("id") long id) {
		timecontrolDelegate.delete(id);
		return "redirect:/timecontrols/";
	}
		
}
