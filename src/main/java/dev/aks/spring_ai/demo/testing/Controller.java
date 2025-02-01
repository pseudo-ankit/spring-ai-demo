package dev.aks.spring_ai.demo.testing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class Controller {
    private final ChatClient client;
    private final Resource resource;

    public Controller(
            ChatClient.Builder clientBuilder,
            @Value("classpath:/prompts/system.st") Resource resource
    ) {
        this.client = clientBuilder.build();
        this.resource = resource;
    }

    @GetMapping("/prompt")
    public ResponseEntity<String> prompt(@RequestBody Map<String, String> reqMap) {
        var sysMessage = new SystemPromptTemplate(resource)
                .createMessage(Map.of("actor", reqMap.get("actor")));

        Prompt prompt = new Prompt(List.of(new UserMessage("Tell me a joke"), sysMessage));
        return ResponseEntity
                .ok(client
                        .prompt(prompt)
                        .call()
                        .content()
                );
    }
}
