package com.example.backend.controller.BlacklistView;

import com.example.backend.model.Blacklist;
import com.example.backend.services.BlacklistServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/blacklist")
public class BlacklistViewController {
    BlacklistServices blacklistServices;

    public BlacklistViewController(BlacklistServices blacklistServices) {
        this.blacklistServices = blacklistServices;
    }

    @GetMapping("/all")
    public String getBlacklist(Model model) {
        model.addAttribute("allOnBlacklist", blacklistServices.fetchBlacklist());
        return "Blacklist/blacklist.html";
    }

    @RequestMapping("/update/{id}")
    public String updateBlacklist(@PathVariable Long id, Model model) {
        Blacklist b = blacklistServices.findBlacklistObjById(id);
        if (b != null) {
            System.out.println(b);
            model.addAttribute("blacklistObj", b);
            return "Blacklist/updateBlacklist.html";
        } else {
            model.addAttribute("message", "Something went wrong");
            return getBlacklist(model);
        }
    }

    @RequestMapping("/update/final")
    public String updateFinalBlacklist(@RequestParam("name") String name,
                                       @RequestParam("email") String email,
                                       @RequestParam(value = "isOk", defaultValue = "false") boolean isOk,
                                       Model model) {
        System.out.println(name);
        System.out.println(email);
        System.out.println(isOk);

        model.addAttribute("message", blacklistServices.updateBlacklistedPerson(email, name, isOk));
        return getBlacklist(model);
    }

    @PostMapping("/filter")
    public String filterBlacklist(@RequestParam("input") String searchWord, Model model) {
        List<Blacklist> filteredBlacklist = blacklistServices.filterBlacklist(searchWord);
        if (!filteredBlacklist.isEmpty()) {
            model.addAttribute("allOnBlacklist", filteredBlacklist);
            model.addAttribute("header", "Matches Found");
        } else {
            model.addAttribute("header", "No Matches Found");
        }

        return "Blacklist/blacklist.html";
    }
}
