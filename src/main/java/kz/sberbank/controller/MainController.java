package kz.sberbank.controller;


import kz.sberbank.service.KalkanServiceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/Signer/json")



public class MainController {

    @Value("${app.password}")
    private String password;
    @Value("${app.path}")
    private String path;
   private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("Signer");

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sign(@RequestBody String request) {
        logger.info("request: "+request);

        KalkanServiceProvider ksp=new KalkanServiceProvider();
        String response = ksp.initKalkan(request, password, path);

        logger.info("Response: "+response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }







}
