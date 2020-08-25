package com.javaweb.webshop.controllers;

import com.javaweb.webshop.models.CategoryRepository;
import com.javaweb.webshop.models.ProductRepository;
import com.javaweb.webshop.models.data.Category;
import com.javaweb.webshop.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;


import javax.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {
    
@Autowired
private ProductRepository productRepository;

@Autowired
private CategoryRepository categoryRepository;

@GetMapping
public String index(Model model,@RequestParam(value="page",required = false) Integer p )
{
int perPage = 1;
int page= (p !=null ) ? p : 0;
double pageCount;

    Pageable pagable = PageRequest.of(page, perPage);

    List<Category> categories =categoryRepository.findAll();

    Page<Product> products =productRepository.findAll(pagable);
    HashMap<Integer,String> cats =new HashMap<>();

for (Category cat : categories) {
    cats.put(cat.getIdcategories(), cat.getName());
}

    model.addAttribute("products", products);
    model.addAttribute("cats", cats);

long count = productRepository.count();
pageCount = Math.ceil((double)count / (double)perPage);
 
model.addAttribute("pageCount", (int)pageCount);
model.addAttribute("perPage", perPage);
model.addAttribute("count", count);
model.addAttribute("page", page);




return "admin/products/index";

}

@GetMapping("/add")
public String add(Product page,Model model) {

    List<Category> categories =categoryRepository.findAll();
 
    model.addAttribute("categories", categories);




    return "admin/products/add";

}

@PostMapping("/add")
public String add(@Valid  Product product,
                    MultipartFile file,
                    BindingResult bindingResult,
                    RedirectAttributes redirectAttributes,
                    Model model) throws IOException {

List<Category> categories =categoryRepository.findAll();

    if (bindingResult.hasErrors()) {
     
     model.addAttribute("categories", categories);
        return "admin/products/add";

    }

boolean fileOk=false;
byte[] bytes=file.getBytes();
String filename=file.getOriginalFilename();
Path path = Paths.get("src/main/resources/static/media/" + filename);

if  (filename.endsWith("jpg") || filename.endsWith("png") )
{

fileOk=true;

}

    redirectAttributes.addFlashAttribute("message", "Page added");
    redirectAttributes.addFlashAttribute("alertClass", "alert-success");

    String slug = product.getName().toLowerCase().replace(" ", "-");

     Product productExists = productRepository.findBySlug(slug);

if  (!fileOk)
{

    redirectAttributes.addFlashAttribute("message", "Image must be png or jpg");
    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
    redirectAttributes.addFlashAttribute("product", product);

}
  else  if (productExists != null) {

        redirectAttributes.addFlashAttribute("message", "Product exists");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        redirectAttributes.addFlashAttribute("product", product);

    }

    else {

        product.setSlug(slug);
        product.setImage(filename);
        
        productRepository.save(product);

        Files.write(path, bytes);

    }

    return "redirect:/admin/products/add";

}

}