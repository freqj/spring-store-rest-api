package dev.alexa.store.payload;

import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.security.View;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    @JsonView({View.ItemView.MinimalView.class,View.ResponseView.MinimalView.class,  View.MessageView.MinimalView.class})
    private Long id;
    @NotEmpty
    @Size(min = 2, message = "title should be minimum 2 characters long")
    @JsonView({View.ItemView.MinimalView.class,View.ResponseView.MinimalView.class,  View.MessageView.MinimalView.class})
    private String title;
    @NotEmpty
    @JsonView({View.ItemView.FullInfo.class})
    private String description;
    @JsonView({View.ItemView.MinimalView.class,View.ResponseView.MinimalView.class,  View.MessageView.MinimalView.class})
    private float price;
    @JsonView({View.ItemView.FullInfo.class})
    private UserDto owner;

}
