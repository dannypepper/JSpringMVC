package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        messages.add(new Message("Aladár", "Mz/x jelkezz, jelkezz", LocalDateTime.now()));
        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now()));
        messages.add(new Message("Blöki", "Vauuu", LocalDateTime.now()));
        messages.add(new Message("Maffia", "miauuu", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Adj pénzt!", LocalDateTime.now()));
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String showMessages(
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
}
