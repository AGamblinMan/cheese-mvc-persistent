package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private MenuDao menuDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese, @RequestParam int categoryId,
                                       Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }
        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(Model model, @RequestParam int[] cheeseIds) {

        for(int cheeseId : cheeseIds){
            Cheese theCheese = cheeseDao.findOne(cheeseId);
            List<Menu> cheeseMenus = menuDao.findByCheeses_Id(cheeseId);
            for (Menu cheeseMenu : cheeseMenus){
                cheeseMenu.removeItem(theCheese);
            }
            cheeseDao.delete(cheeseId);

        }
//        model.addAttribute("cheeses", cheeseDao.findAll());
//        model.addAttribute("error", "true");
//        model.addAttribute("title", "Remove Cheese");

        return "cheese/index";
    }

    @RequestMapping(value = "category/{categoryId}", method = RequestMethod.GET)
    public String category(Model model, @PathVariable int categoryId){
        Category category = categoryDao.findOne(categoryId);

        if (category != null) {
            model.addAttribute("title", String.format("Cheeses in Category: %s", category.getName()));
            model.addAttribute("cheeses", category.getCheeses());
            return "cheese/index";
        }
        else{
            return "redirect:/category";
        }
    }

    @RequestMapping(value="edit/{cheeseId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int cheeseId){

        Cheese editCheese = cheeseDao.findOne(cheeseId);
        if (editCheese == null){
            return "redirect:";
        }
        model.addAttribute("cheese", editCheese);
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/edit";
    }

    @RequestMapping(value="edit", method=RequestMethod.POST)
    public String processEditForm(Model model,
                                  @ModelAttribute @Valid Cheese editCheese,
                                  Errors errors,
                                  @RequestParam int cheeseId){
        if (errors.hasErrors()){
            model.addAttribute("cheese", editCheese);
        }
        Cheese cheese = cheeseDao.findOne(cheeseId);

        cheese.setCategory(editCheese.getCategory());
        cheese.setName(editCheese.getName());
        cheese.setDescription(editCheese.getDescription());


        cheeseDao.save(cheese);
        return "redirect:";
    }

}
