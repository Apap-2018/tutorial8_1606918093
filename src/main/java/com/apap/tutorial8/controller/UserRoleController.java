package com.apap.tutorial8.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.apap.tutorial8.model.FlightModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value="/addUser", method=RequestMethod.POST)
	private ModelAndView addUser(@ModelAttribute UserRoleModel user, RedirectAttributes redir) {
	    String pattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,}";
	    String str = "";
		if(user.getPassword().matches(pattern)){
			userService.addUser(user);
			str = null;
		}
		else {
			str = "WARNING : Password harus mengandung kombinasi huruf dan angka serta memiliki setidaknya 8 karakter";
			
		}
		ModelAndView archive = new ModelAndView("redirect:/");
		redir.addFlashAttribute("msg",str);
		return archive;
	}
	
	@RequestMapping(value = "/updatePassword")
	private String updatePass() {
		return "update-password";
	}
	
	@RequestMapping(value="/submitPassword",method=RequestMethod.POST)
	public ModelAndView updatePasswordSubmit(@ModelAttribute PasswordModel pword, Model model,RedirectAttributes red) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		String str = "";
	    String randomize = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,}";
		if (pword.getConPassword().equals(pword.getNewPassword())) {
			
			if (passwordEncoder.matches(pword.getOldPassword(), user.getPassword())) {
				if(pword.getNewPassword().matches(randomize)) {
					userService.changePassword(user, pword.getNewPassword());
					str = "password berhasil diubah";
				}
				else {
					str = "WARNING : Password harus mengandung kombinasi huruf dan angka serta memiliki setidaknya 8 karakter";
				}
			}
			else {
				str = "password lama anda salah";
			}
			
		}
		else {
			str = "password baru tidak sesuai";
		}
		
		
		ModelAndView archive = new ModelAndView("redirect:/user/updatePassword");
		red.addFlashAttribute("msg",str);
		return archive;
	}
}