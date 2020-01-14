package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private List<Message> messages = new ArrayList<>();

    {
        messages.add(new Message("Aladár", "Mz/x jelkezz, jelkezz", LocalDateTime.now().minusDays(10)));
        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now().minusDays(5)));
        messages.add(new Message("Blöki", "Vauuu", LocalDateTime.now()));
        messages.add(new Message("Maffia", "miauuu", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now().plusDays(5)));
        messages.add(new Message("Aladár", "Adj pénzt!", LocalDateTime.now().plusDays(10)));
        messages.add(new Message("Goldenberg", "Kérem az aranyad!", LocalDateTime.now().plusDays(3)));
        messages.add(new Message("Adolf Hitler","Mein Kampf",LocalDateTime.now().plusYears(-75).plusMonths(3).plusDays(6).plusMinutes(264)));
    }

    public List<Message> filterMessage(Long id, String author, String text, LocalDateTime from, LocalDateTime to, Integer limit, String orderBy, String order) {
        Comparator<Message> msgComp = Comparator.comparing((Message::getCreationDate));
        switch (orderBy) {
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
        if (order.equals("desc")) {
            msgComp = msgComp.reversed();
        }
        return messages;
    }

    public Message getMessage(Long msgId) {
        Optional<Message> message = messages.stream().filter(m -> m.getId().equals(msgId)).findFirst();

        return message.get();
    }

    public void createMessage(Message m) {
        m.setCreationDate(LocalDateTime.now());
        m.setId((long) messages.size());
        messages.add(m);
    }
}

