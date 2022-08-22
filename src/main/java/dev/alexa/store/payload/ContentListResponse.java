package dev.alexa.store.payload;


import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.security.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentListResponse<T> {
    @JsonView( View.ResponseView.class)
    private List<T> content;
    @JsonView( View.ResponseView.class)
    private int pageNumber;
    @JsonView( View.ResponseView.class)
    private int pageSize;
    @JsonView( View.ResponseView.class)
    private Long totalElements;
    @JsonView( View.ResponseView.class)
    private int totalPages;
    @JsonView( View.ResponseView.class)
    private boolean last;
}
