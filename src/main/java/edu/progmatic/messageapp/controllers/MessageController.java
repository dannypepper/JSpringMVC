package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.utils.DateConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    private static List<Message> messages = new ArrayList<>();

    static{
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
            Model model){
        Comparator<Message> msgComp = Comparator.comparing((Message::getCreationDate));
        switch (orderBy){
            case "text":
                msgComp = Comparator.comparing((Message::getText));
                break;
            case "id":
                msgComp = Comparator.comparing((Message::getId));
                break;
            case "author":
                msgComp = Comparator.comparing((Message::getAuthor));
                break;
            default:
                break;
        }
        if(order.equals("desc")){
            msgComp = msgComp.reversed();
        }

        List<Message> msgs = messages.stream()
                .filter(m -> id == null ? true : m.getId().equals(id))
                .filter(m -> StringUtils.isEmpty(author) ? true : m.getAuthor().contains(author))
                .filter(m -> StringUtils.isEmpty(text) ? true : m.getText().contains(text))
                .filter(m -> from == null ? true : m.getCreationDate().isAfter(from))
                .filter(m -> to == null ? true : m.getCreationDate().isBefore(to))
                .sorted(msgComp)
                .limit(limit).collect(Collectors.toList());

        model.addAttribute("msgList", msgs);
        return "messageList";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model){
        Optional<Message> message = messages.stream().filter(m -> m.getId().equals(msgId)).findFirst();
        if(message.isPresent()){
            model.addAttribute("message", message.get());
        }
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

        m.setCreationDate(LocalDateTime.now());
        m.setId((long) messages.size());
        messages.add(m);
        //return "home";
        //return "redirect:/messages?orderby=createDate&order=desc";
        //return "redirect:/messages?id=" + m.getId();
        return "redirect:/message/" + m.getId();
    }

}
