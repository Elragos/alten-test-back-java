package fr.alten.test_back.config;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
@PropertySource(
    value = "classpath:initialData.json",
    factory = InitialDataFactory.class
)
@ConfigurationProperties
public class InitialDataProperties {

    private List<Map> users;

    private List<Map> products;
    
    public List<Map> getUsers() {
        return users;
    }

    public void setUsers(List<Map> users) {
        this.users = users;
    }
    
    public List<Map> getProducts() {
        return products;
    }

    public void setProducts(List<Map> products) {
        this.products = products;
    }
    
}
