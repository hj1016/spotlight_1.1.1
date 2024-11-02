package spotlight.spotlight_ver2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import spotlight.spotlight_ver2.service.ChatbotService;

@RestController
@RequestMapping("/api/chat")
@Tag(name="챗봇 API", description = "챗봇 서비스를 제공하는 API 입니다.")
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
