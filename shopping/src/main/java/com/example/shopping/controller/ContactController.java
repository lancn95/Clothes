package com.example.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.entities.Category;
import com.example.shopping.entities.Contact;
import com.example.shopping.form.CategoryForm;
import com.example.shopping.form.ContactForm;
import com.example.shopping.service.ContactService;

@Controller
public class ContactController {

	@Autowired
	private ContactService contactService;

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contactPage(Model model) {
		List<Contact> contacts = contactService.finđAll();

		model.addAttribute("contacts", contacts);
		return "customer/contact";
	}

	@RequestMapping(value = "/admin/contact", method = RequestMethod.GET)
	public String contactList(Model model) {
		List<Contact> contacts = contactService.finđAll();

		model.addAttribute("contacts", contacts);
		return "admin/list_contact";
	}

	@RequestMapping(value = "/admin/contact-save", method = RequestMethod.GET)
	public String getInfoContact(Model model) {
		ContactForm contactForm = new ContactForm();

		model.addAttribute("contactForm", contactForm);
		return "admin/save_contact";
	}

	@RequestMapping(value = "/admin/contact-save", method = RequestMethod.POST)
	public String saveContact(@ModelAttribute ContactForm contactForm) {

		contactService.save(contactForm);

		return "redirect:/admin/contact";
	}

	@RequestMapping(value = "/admin/contact-update", method = RequestMethod.GET)
	public String editContact(Model model, @RequestParam("id") int id) {
		ContactForm contactForm = null;

		if (id > 0) {
			Contact contact = contactService.findById(id);
			if (contact != null) {
				contactForm = new ContactForm(contact);
			}
		}
		model.addAttribute("contactForm", contactForm);
		return "admin/update_contact";
	}

	@RequestMapping(value = "/admin/contact-update", method = RequestMethod.POST)
	public String updateContacty(@ModelAttribute ContactForm contactForm, RedirectAttributes reidrect) {

		contactService.update(contactForm);
		reidrect.addFlashAttribute("successMessage", "Cập nhật thành công");

		return "redirect:/admin/contact";

	}
	
	@RequestMapping(value = "/admin/contact-delete", method = RequestMethod.GET)
	public String deleteContact(@RequestParam("id") int id, RedirectAttributes reidrect) {
		ContactForm contactForm = null;
		if (id > 0) {
			Contact contact = contactService.findById(id);
			if (contact != null) {
				contactForm = new ContactForm(contact);
			}
		}

		contactService.delete(contactForm);
		reidrect.addFlashAttribute("successMessage", "Xóa thành công");
		
		return "redirect:/admin/contact";
	}
}
