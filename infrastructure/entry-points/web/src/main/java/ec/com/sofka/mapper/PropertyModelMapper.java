package ec.com.sofka.mapper;

import ec.com.sofka.data.PropertyRequestDTO;
import ec.com.sofka.generics.shared.PropertyQuery;
import org.springframework.stereotype.Component;

@Component
public class PropertyModelMapper {
    public static PropertyQuery mapToRequest(PropertyRequestDTO property) {
        if (property == null) {
            return null;
        }

        return new PropertyQuery(
                property.getProperty()
        );
    }
}
