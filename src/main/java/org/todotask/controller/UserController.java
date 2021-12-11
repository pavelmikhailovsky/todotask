package org.todotask.controller;

import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.todotask.model.User;
import org.todotask.service.UserService;
import org.todotask.service.ValuesNotMatchException;
import org.todotask.service.auth.UserAuthorization;

import static org.todotask.controller.SchemaDefaultValue.*;

import java.io.IOException;
import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserAuthorization userAuthorization;

    @Autowired
    public UserController(UserService userService, UserAuthorization userAuthorization) {
        this.userService = userService;
        this.userAuthorization = userAuthorization;
    }

    @Operation(summary = "Gets all users", tags = "user", deprecated = true)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Founds all users",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    }
            )
    })
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        return userService.getAllUsers(authorizationHeader);
    }

    @Operation(summary = "Get users by id", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returned instance user by id",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("user_id") Long id) {
        return userService.getByIdUser(id);
    }

    @Operation(summary = "Create user with unique username", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Returned a token for authorization",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_TOKEN)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Token is not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createUser(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {
        String token = userService.createUser(User.getInstance(username, password));
        return Map.of("token", token);
    }

    @Operation(summary = "Upload image for current user", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return status code",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Return a message with an exception",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestParam("file") MultipartFile file,
                            @RequestHeader("Authorization") String authorizationHeader) {
        userService.uploadImage(file, authorizationHeader);
    }

    @Operation(summary = "Login users", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return token on successful login",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_TOKEN)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect login details",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> login(@RequestParam("username") String username,
                                     @RequestParam("password") String password) throws ValuesNotMatchException {
        String token = userAuthorization.login(username, password);
        return Map.of("token", token);
    }

    @Operation(summary = "Gets image for current user", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return the image if it exists",
                    content = {
                            @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "The picture does not exist",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = "{\"message\": \"string\"}")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid authorization header",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(defaultValue = SCHEMA_DEFAULT_VALUE_MESSAGE)
                            )
                    }
            )
    })
    @GetMapping(value = "/get-image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage(@RequestHeader("Authorization") String authorizationHeader) throws IOException {
        return userService.getImage(authorizationHeader);
    }

    @ExceptionHandler({ValuesNotMatchException.class, SQLDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> sqlException(Exception e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler({SignatureException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tokenException(Exception e) {
        return Map.of("message", "token is not valid");
    }

    @ExceptionHandler({IOException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getImageException(Exception e) { }
}
