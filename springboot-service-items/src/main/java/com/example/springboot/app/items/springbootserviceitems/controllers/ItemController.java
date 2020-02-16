package com.example.springboot.app.items.springbootserviceitems.controllers;

import com.example.springboot.app.commons.models.entity.Product;
import com.example.springboot.app.items.springbootserviceitems.models.Item;
import com.example.springboot.app.items.springbootserviceitems.services.IItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RefreshScope // permite actualizar los componentes que estamos inyectandole con value configuraciones y enviroment, actualiza, refresca el contexto, vuelve a inyectar y se vuelve a inicializar el componente con los cambios reflejados en tiempo real sin tener que reiniciar la aplicacion, mediante una ruta url (endpoint de spring actuator)
@RestController
@RequestMapping("")
public class ItemController {

    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("itemServiceFeign")
    //@Qualifier("itemServiceRestTemplate")
    private IItemService itemService;

    @Value("${configuration.text}")
    private String text;

    @GetMapping("/list")
    public List<Item> getAll(){

        return itemService.findAll();
    }

    @HystrixCommand(fallbackMethod = "alternativeMethod")
    @GetMapping("/see/{id}/quantity/{quantity}")
    public Item detail(@PathVariable Long id, @PathVariable Integer quantity) {

        return itemService.findById(id, quantity);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {

        Product createdProduct = itemService.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable Long id) {

        Product updatedProduct = itemService.update(product, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Product> deleteById(@PathVariable Long id) {

        Product deletedProduct = itemService.deleteById(id);

        return ResponseEntity.ok(deletedProduct);
    }


    // creamos un metodo alternativo con los mismos parametros en caso de error
    public Item alternativeMethod(Long id, Integer quantity){

        Item item = new Item();
        Product product = new Product();

        product.setId(id);
        product.setName("Sony Camera");
        product.setPrice(500.00);
        item.setProduct(product);
        item.setQuantity(quantity);

        return item; // retornamos un item por defecto en caso de error
    }

    @GetMapping("/get-config")
    public ResponseEntity<?> getConfig(@Value("${server.port}") String port) {

        log.info(text);

        Map<String, String> json = new HashMap<>();
        json.put("text", text);
        json.put("port", port);

        if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {

            json.put("author.name", env.getProperty("configuration.author.name"));
            json.put("author.email", env.getProperty("configuration.author.email"));
        }

        return ResponseEntity.ok(json);
    }
}
