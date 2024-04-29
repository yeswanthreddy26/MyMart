package com.mymart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mymart.model.Faq;
import com.mymart.repository.FaqRepository;

import java.util.Optional;

@Controller
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    private FaqRepository faqRepository;

    @GetMapping
    public String faqPage(Model model) {
        model.addAttribute("faqs", faqRepository.findAll());
        return "faq";
    }

    @GetMapping("/new")
    public String newFaq(Model model) {
        model.addAttribute("faq", new Faq());
        return "new_faq";
    }

    @PostMapping("/save")
    public String saveFaq(@ModelAttribute Faq faq) {
        faqRepository.save(faq);
        return "redirect:/faq";
    }

    @GetMapping("/edit/{id}")
    public String editFaq(@PathVariable Long id, Model model) {
        Optional<Faq> faq = faqRepository.findById(id);
        faq.ifPresent(value -> model.addAttribute("faq", value));
        return "edit_faq";
    }

    @PostMapping("/update/{id}")
    public String updateFaq(@PathVariable Long id, @ModelAttribute Faq updatedFaq) {
        Optional<Faq> optionalFaq = faqRepository.findById(id);
        optionalFaq.ifPresent(faq -> {
            faq.setQuestion(updatedFaq.getQuestion());
            faq.setAnswer(updatedFaq.getAnswer());
            faqRepository.save(faq);
        });
        return "redirect:/faq";
    }

    @PostMapping("/delete/{id}")
    public String deleteFaq(@PathVariable Long id) {
        faqRepository.deleteById(id);
        return "redirect:/faq";
    }
}
