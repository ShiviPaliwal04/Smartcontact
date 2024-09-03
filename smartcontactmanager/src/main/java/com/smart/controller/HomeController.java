package com.smart.controller;


import jakarta.validation.Valid;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.Dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
@Controller
public class HomeController {
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","Register - Smart Contact Manager");
	return "normal/home";
}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","Register - Smart Contact Manager");
		return "normal/about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "normal/signup";
	}
	@Autowired
	public HomeController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
	    this.passwordEncoder = passwordEncoder;
	    this.userRepository = userRepository;
	}
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Validated @ModelAttribute("user") User user,BindingResult result1, @RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model,HttpSession session) {
		

		try {
		if(!agreement) {
			System.out.println("You have not agreed the terms and conditions.");
		throw new Exception("You have not agreed the terms and conditions.");
		}
		if(result1.hasErrors()){
			System.out.println("ERROR"+result1.toString());
			model.addAttribute("user",user);
			return "normal/signup";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImgUrl("default.png");
		System.out.println("Password"+user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		System.out.println("Agreement : "+agreement);
		System.out.println("User : "+user);
		User result = this.userRepository.save(user);
		model.addAttribute("user",new User());
		session.setAttribute("message",new Message("Successfully Registered	 !!","alert-success"));
	return "normal/signup";
	}catch(Exception e) {
	e.printStackTrace();
	model.addAttribute("user",user);
	session.setAttribute("message",new Message("Something went wrong !!"+e.getMessage(),"alert-danger"));
	return "normal/signup";
	}
		
	}
	@GetMapping("/deletes/{id}")
	public String deleteContacts(@PathVariable("id") Integer id, Model model,Principal principal,HttpSession session) {
		System.out.println("Usersssssss");
		Optional<User> contactOptional=this.userRepository.findById(id);
	User contact=contactOptional.get();
	String userName=principal.getName();
	User user=this.userRepository.getUserByUserName(userName);
	System.out.println("Usersssssss "+user);
	this.userRepository.delete(contact);
	
	session.setAttribute("message",new Message("Contact deleted successfully.....","success"));
		return "normal/signup";
	}
	@GetMapping("/signin")
	public String customLogin(Model model) {
	model.addAttribute("title","Login Page");
	return "normal/login";
	}
	
}