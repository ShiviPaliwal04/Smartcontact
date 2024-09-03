package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;


import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;




@Controller
@RequestMapping("/user")
public class UserController {
@Autowired
private UserRepository userRepository;
@Autowired
private ContactRepository contactRepository;
@ModelAttribute
public void addCommonData(Model model,Principal principal) {
	String username=principal.getName();
	System.out.println("Username"+username);
	User user=userRepository.getUserByUserName(username);
	System.out.println("User "+user.getName());
	model.addAttribute("user",user);
}
	@RequestMapping("/index")
public String dashboard(Model model,Principal principal) {
		
	return "Users/user_dashboard";
}
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "Users/add_contact_form";
	}
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Principal principal,Pageable pePageable,HttpSession session) {
	try {
		String name=principal.getName();
	User user=this.userRepository.getUserByUserName(name);
	if(file.isEmpty()) {
	System.out.println("This is a empty file.");
	contact.setImage("img2.jpg");
	}else {
		contact.setImage(file.getOriginalFilename());
		File saveFile=new ClassPathResource("/static/img").getFile();
		Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
		}
	
	user.getContacts().add(contact);
	contact.setUser(user);
	this.userRepository.save(user);
		System.out.println("DATA "+contact);
		System.out.println("Added to the data base");
		session.setAttribute("message",new Message("Your contact is added !! Add more..","success"));
       

	}catch(Exception e) {
		System.out.println("ERROR"+e.getMessage());
		e.printStackTrace();
		session.setAttribute("message",new Message("Something went wrong !! Try again..","danger"));
	}
	return "Users/add_contact_form";
	}
	@GetMapping("/show-contact/{page}")
	  public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal) {
		m.addAttribute("title","Show User Contacts");
//		String username=principal.getName();
//		User user=this.userRepository.getUserByUserName(username);
//		List<Contact> contacts=user.getContacts();
		
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		Pageable pageable=PageRequest.of(page,3);
	Page<Contact> contacts=	this.contactRepository.findContactByUser(user.getId(),pageable);
	m.addAttribute("contacts",contacts);
	m.addAttribute("currentPage",page);
	m.addAttribute("totalPages",contacts.getTotalPages());
		 return "Users/show_contacts";
	  }
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal) {
		System.out.println("CID "+cId);
		Optional<Contact> contactOptional=this.contactRepository.findById(cId);
		Contact contact=contactOptional.get();
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		if(user.getId()==contact.getUser().getId())
		{
		model.addAttribute("contact",contact);
		}
		return "Users/contact_detail";
	}
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model,Principal principal,HttpSession session) {
	
		Optional<Contact> contactOptional=this.contactRepository.findById(cid);
	Contact contact=contactOptional.get();
	String userName=principal.getName();
	User user=this.userRepository.getUserByUserName(userName);
	if(user.getId()==contact.getUser().getId()) {
	this.contactRepository.delete(contact);
	}
	session.setAttribute("message",new Message("Contact deleted successfully.....","success"));
		return "redirect:/user/show-contact/0";
	}
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m) {
		m.addAttribute("title","Update Contact");
	Contact contact=	this.contactRepository.findById(cid).get();
	m.addAttribute("contact",contact);
		return "Users/update_form";
	}
@PostMapping("/update_form")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Model m, HttpSession session,Principal principal) {
		try {
			Contact oldcontactDetail=this.contactRepository.findById(contact.getcId()).get();
		if(!file.isEmpty()) {
			File deleteFile=new ClassPathResource("static/img").getFile();
			File file1=new File(deleteFile,oldcontactDetail.getImage());
			file1.delete();
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			contact.setImage(file.getOriginalFilename());
			
		}else
		{
			contact.setImage(oldcontactDetail.getImage());
		}
		User user=this.userRepository.getUserByUserName(principal.getName());
		contact.setUser(user);
		this.contactRepository.save(contact);
		session.setAttribute("message",new Message("Your contact is updated !!","success"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Contact Name"+contact.getName());
		System.out.println("Contact Id"+contact.getcId());
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
@GetMapping("/profile")
public String yourProfile(Model model) {
	model.addAttribute("title","Profile Page");
	return "Users/profile";
}

@PostMapping("/create_order")
@ResponseBody
public String createOrder(@RequestBody Map<String,Object> data) {
	System.out.println(data);
	int amt=Integer.parseInt(data.get("amount").toString());
	return "Done";
}
}