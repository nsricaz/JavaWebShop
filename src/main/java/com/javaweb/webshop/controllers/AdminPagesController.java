package com.javaweb.webshop.controllers;

import com.javaweb.webshop.models.PageRepository;
import com.javaweb.webshop.models.data.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


import javax.validation.Valid;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository pageRepo;

    @GetMapping
    public String index(final Model model) {

        final List<Page> pages = pageRepo.findAll();

        model.addAttribute("pages", pages);

        return "admin/pages/index";

    }

    @GetMapping("/add")
    public String add(@ModelAttribute final Page page) {

        // model.addAttribute("page",new Page());

        return "admin/pages/add";

    }

    @PostMapping("/add")
    public String add(@Valid  Page page,  BindingResult bindingResult,
             RedirectAttributes redirectAttributes,  Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/pages/add";

        }

        redirectAttributes.addFlashAttribute("message", "Page added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");

         Page slugExists = pageRepo.findBySlug(slug);

        if (slugExists != null) {

            redirectAttributes.addFlashAttribute("message", "Slug exists");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);

        }

        else {

            page.setSlug(slug);
            page.setSorting(100);
            pageRepo.save(page);
        }

        return "redirect:/admin/pages/add";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final int id, final Model model) {

        final Page page = pageRepo.getOne(id);

        model.addAttribute("page", page);

        return "admin/pages/edit";

    }

    @PostMapping("/reorder")
    public String reorder(@RequestParam("id[]") final int[] id) {

        int count = 1;
        Page page;

        for (int pageid : id) {
    page=pageRepo.getOne(pageid);
    page.setSorting(count);
    // pageRepo.setpagesSortingById(300, pageid);
    pageRepo.save(page);
    count++;
}



return "ok";

}


}