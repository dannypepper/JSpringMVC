package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/messages")
    public String showMessages(Model model){
        model.addAttribute("msgList", messages);
        return "messageList";
    }
}
