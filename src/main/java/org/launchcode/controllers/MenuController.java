package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Adam on 4/10/2017.
 */
@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "All Menus");
        model.addAttribute("menus", menuDao.findAll());
        return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model){
        model.addAttribute("title", "Add a Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value="add", method= RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors){

        if (errors.hasErrors()){
            model.addAttribute("title", "Add a Menu");
            model.addAttribute("menu", menu);
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value="view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        if (menu == null) {
            return "redirect:";
        }
        else{
            model.addAttribute("title", menu.getName());
            model.addAttribute("menu", menu);
            return "menu/view";
        }
    }

    @RequestMapping(value="add-item/{menuId}", method= RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){
        Menu menu = menuDao.findOne(menuId);
        if (menu != null) {
            AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll());
            model.addAttribute("title", String.format("Add Cheese to Menu: %s", menu.getName()));
            model.addAttribute("form", form);
            return "menu/add-item";
        } else{
            return "redirect:";
        }
    }

    @RequestMapping(value="add-item", method= RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors){
        if (errors.hasErrors()){
            model.addAttribute("form", form);
            model.addAttribute("title", String.format("Add Cheese to Menu: %s", form.getMenu().getName()));
            return "menu/add-item";
        }
        Cheese cheese = cheeseDao.findOne(form.getCheeseId());
        Menu menu = menuDao.findOne(form.getMenuId());
        menu.addItem(cheese);
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();


    }


}
