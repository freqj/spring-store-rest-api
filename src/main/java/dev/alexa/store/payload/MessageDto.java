package dev.alexa.store.payload;


import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.security.View;
import lombok.Data;

@Data
public class MessageDto {

    @JsonView({View.MessageView.MinimalView.class, View.ResponseView.MinimalView.class})
    private Long id;
    @JsonView({View.MessageView.FullInfo.class})
    private ItemDto item;
    @JsonView({View.MessageView.MinimalView.class,  View.ResponseView.MinimalView.class})
    private UserDto sender;
    @JsonView({View.MessageView.MinimalView.class,  View.ResponseView.MinimalView.class})
    private String message;

    private UserDto destination;
}
