package com.capitan.encuestas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import com.capitan.encuestas.entities.UserEntity;
import com.capitan.encuestas.models.requests.UserLoginRequestModel;
import com.capitan.encuestas.models.requests.UserRegisterRequestModel;
import com.capitan.encuestas.models.responses.UserRest;
import com.capitan.encuestas.models.responses.ValidationErrors;
import com.capitan.encuestas.repositories.UserRepository;
import com.capitan.encuestas.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTests {
    
    private static final String API_URL = "/users";
    private static final String API_LOGIN_URL = "/users/login";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_sinNingunDato_retornaBadRequest() {
        ResponseEntity<Object> response = register(new UserRegisterRequestModel(), Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_sinElCampoNombre_retornaBadRequest() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        user.setName(null);
        ResponseEntity<Object> response = register(user, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_sinNingunDato_retornaErroresDeValidacion() {
        ResponseEntity<ValidationErrors> response = register(new UserRegisterRequestModel(), ValidationErrors.class);
        Map<String, String> errors = response.getBody().getErrors();

        assertEquals(errors.size(), 3);
    }

    @Test
    public void createUser_sinNombre_retornaMensajeDeErrorDeValidacionParaElNombre() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        user.setName(null);

        ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("name"));
    }

    @Test
    public void createUser_conUsuarioValido_retornaOK() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        ResponseEntity<UserRest> response = register(user, UserRest.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void createUser_conUsuarioValido_retornaUserRest() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        ResponseEntity<UserRest> response = register(user, UserRest.class);

        assertEquals(response.getBody().getName(), user.getEmail());
    }

    @Test
    public void createUser_conUsuarioValido_guardaElUsuarioEnLaBaseDeDatos() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        ResponseEntity<UserRest> response = register(user, UserRest.class);
        UserEntity userDB = userRepository.findById(response.getBody().getId());

        assertNotNull(userDB);
    }

    @Test
    public void createUser_conUsuarioValido_guardaElPasswordConHashEnLaBaseDeDatos() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        ResponseEntity<UserRest> response = register(user, UserRest.class);
        UserEntity userDB = userRepository.findById(response.getBody().getId());

        assertNotEquals(user.getPassword(), userDB.getEncryptedPassword());
    }

    @Test
    public void createUser_conUsuarioValidoConCorreoElectronicoExistente_retornaBadRequest() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        register(user, UserRest.class);

        ResponseEntity<UserRest> response2 = register(user, UserRest.class);
     
        assertEquals(response2.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_conUsuarioValidoConCorreoElectronicoExistente_retornaMensajeDeError() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        register(user, UserRest.class);

        ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
     
        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("email"));
    }

    @Test
    public void getUser_sinTokenDeAutenticacion_retornaForbidden() {
        ResponseEntity<Object> response = getUser(null, new ParameterizedTypeReference<Object>(){}); 
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    } 

    @Test
    public void getUser_conTokenDeAutenticacion_retornaOK() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());
        ResponseEntity<Map<String, String>> responseLogin = login(model, new ParameterizedTypeReference<Map<String, String>>(){});
        Map<String, String> body = responseLogin.getBody();
        String token = body.get("token").replace("Bearer ", "");

        ResponseEntity<UserRest> response = getUser(token, new ParameterizedTypeReference<UserRest>(){}); 
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    } 

    @Test
    public void getUser_conTokenDeAutenticacion_retornaUserRest() {
        UserRegisterRequestModel user = TestUtils.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());
        ResponseEntity<Map<String, String>> responseLogin = login(model, new ParameterizedTypeReference<Map<String, String>>(){});
        Map<String, String> body = responseLogin.getBody();
        String token = body.get("token").replace("Bearer ", "");

        ResponseEntity<UserRest> response = getUser(token, new ParameterizedTypeReference<UserRest>(){}); 
        assertEquals(user.getName(), response.getBody().getName());
    } 

    public <T> ResponseEntity<T> getUser(String token, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<Object>(null, headers);
        return testRestTemplate.exchange(API_URL, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<T> register(UserRegisterRequestModel data, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_URL, data, responseType);
    }

    public <T> ResponseEntity<T> login(UserLoginRequestModel data, ParameterizedTypeReference<T> responseType) {
        HttpEntity<UserLoginRequestModel> entity = new HttpEntity<UserLoginRequestModel>(data, new HttpHeaders());
        return testRestTemplate.exchange(API_LOGIN_URL, HttpMethod.POST, entity, responseType);
    }
}
