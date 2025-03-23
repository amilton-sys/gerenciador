package com.sys.gerenciador.controller;

import com.sys.gerenciador.assembler.ShoppingInputDesassembler;
import com.sys.gerenciador.dto.ShoppingInput;
import com.sys.gerenciador.model.ShoppingItem;
import com.sys.gerenciador.repository.IShoppingRepository;
import com.sys.gerenciador.service.IGenericService;
import com.sys.gerenciador.util.AppConstant;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class ShoppingController {

    private final IShoppingRepository iShoppingRepository;
    private final IGenericService<ShoppingItem> shoppingIGenericService;
    private final ShoppingInputDesassembler shoppingInputDesassemble;

    @GetMapping("/loadShoppings")
    public String loadShoppings(Model model,
                                @RequestParam(defaultValue = "0") Integer pageNumber,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                RedirectAttributes redirectAttributes) {

        if (pageNumber < 0)
            pageNumber = 0;
        if (pageSize <= 0 || pageSize > AppConstant.Pagination.MAX_PAGE_SIZE)
            pageSize = AppConstant.Pagination.DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ShoppingItem> page = iShoppingRepository.findAll(pageable);

        if (pageNumber >= page.getTotalPages() && page.getTotalPages() > 0) {
            redirectAttributes.addAttribute("pageNumber", page.getTotalPages() - 1);
            redirectAttributes.addAttribute("pageSize", pageSize);
            return "redirect:/addShoppings";
        }

        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());

        List<ShoppingItem> expenses = page.getContent();

        model.addAttribute("shoppings", expenses);
        model.addAttribute("shoppingSize", expenses.size());

        return "user/add_shopping";
    }

    @PostMapping("/addShopping")
    public ResponseEntity<Map<String, String>> addShopping(@RequestBody @Valid ShoppingInput shoppingInput) {
        Map<String, String> response = new HashMap<>();
        try {
            ShoppingItem shopping = shoppingInputDesassemble.toDomainObject(shoppingInput);
            shoppingIGenericService.save(shopping);

            response.put("success", "true");
            response.put("message", "Shopping adicionada com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("error", "Erro ao adicionar shopping.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
