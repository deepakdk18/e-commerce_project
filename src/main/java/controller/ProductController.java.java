package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, 
                           CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(Model model,
                             @RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir) {
        
        Page<Product> pageProducts = productService.findPaginated(page, size, sortField, sortDir);
        List<Product> products = pageProducts.getContent();
        
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Products - ECommerce");
        
        return "products/list";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results - ECommerce");
        return "products/search";
    }

    @GetMapping("/category/{id}")
    public String listProductsByCategory(@PathVariable Long id, Model model) {
        List<Product> products = productService.getProductsByCategory(id);
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Category Products - ECommerce");
        return "products/category";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Add Product - ECommerce");
        return "products/add";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                            @RequestParam("imageFile") MultipartFile imageFile) {
        productService.saveProduct(product, imageFile);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Edit Product - ECommerce");
        return "products/edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/detail/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", product.getName() + " - ECommerce");
        return "products/detail";
    }
}