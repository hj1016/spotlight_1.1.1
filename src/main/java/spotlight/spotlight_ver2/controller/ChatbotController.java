package spotlight.spotlight_ver2.controller;

import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.service.ChatbotService;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/ask")
    public String askChatbot(@RequestBody String userInput) {
        return chatbotService.getChatGPTResponse(userInput);
    }
}
