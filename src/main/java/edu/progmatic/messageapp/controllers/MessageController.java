package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    private InMemoryUserDetailsManager userService;
    private MessageService messageService;

    @Autowired
    public MessageController(UserDetailsService userService, MessageService messageService) {
        this.userService = (InMemoryUserDetailsManager) userService;
        this.messageService = messageService;
    }

    private List<Message> messages = new ArrayList<>();

    {
        messages.add(new Message("Aladár", "Mz/x jelkezz, jelkezz", LocalDateTime.now().minusDays(10)));
        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now().minusDays(5)));
        messages.add(new Message("Blöki", "Vauuu", LocalDateTime.now()));
        messages.add(new Message("Maffia", "miauuu", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now().plusDays(5)));
        messages.add(new Message("Aladár", "Adj pénzt!", LocalDateTime.now().plusDays(10)));

    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String showMessages(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to,
            @RequestParam(name = "limit", defaultValue = "100", required = false) Integer limit,
            @RequestParam(name = "orderby", defaultValue = "", required = false) String orderBy,
            @RequestParam(name = "order", defaultValue = "asc", required = false) String order,
            Model model)
    {
        List<Message> messages1 = messageService.filterMessage(id, author, text, from, to, limit, orderBy, order);
        model.addAttribute("msgList", messages1);
        return "messageList";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model){

        Message message = messageService.getMessage(msgId);
        model.addAttribute("message", message);
        /*Optional<Message> message = messages.stream().filter(m -> m.getId().equals(msgId)).findFirst();
        if(message.isPresent()){
            model.addAttribute("message", message.get());
        }*/
        return "oneMessage";
    }

    @GetMapping(path = "/showcreate")
    public String showCreateMessage(Model model) {
        Message m = new Message();
        model.addAttribute("message", m);
        return "createMessage";
    }

    @PostMapping(path = "/createmessage")
    public String createMessage(@Valid @ModelAttribute("message") Message m, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createMessage";
        }
        messageService.createMessage(m);
        return "redirect:/message/" + m.getId();

        //return "home";
        //return "redirect:/messages?orderby=createDate&order=desc";
        //return "redirect:/messages?id=" + m.getId();
    }

}
