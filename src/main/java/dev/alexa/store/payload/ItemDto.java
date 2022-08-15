package dev.alexa.store.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ItemDto {

    private Long id;
    @NotEmpty
    @Size(min = 2, message = "title should be minimum 2 characters long")
    private String title;
    @NotEmpty
    private String description;
    private float price;

}
