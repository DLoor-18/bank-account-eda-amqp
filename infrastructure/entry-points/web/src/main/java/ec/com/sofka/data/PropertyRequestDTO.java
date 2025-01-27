package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Schema(description = "Object representing the data to find record.")
public class PropertyRequestDTO implements Serializable {
    @Schema(description = "property of found record", example = "test")
    @NotNull(message = "property cannot be null")
    @NotBlank(message = "property cannot be blank")
    private String property;

    public PropertyRequestDTO() {
    }

    public PropertyRequestDTO(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
