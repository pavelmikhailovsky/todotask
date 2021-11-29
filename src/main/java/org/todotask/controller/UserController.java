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
import org.todotask.service.UserServiceImp;
import org.todotask.service.ValuesNotMatchException;

import java.io.IOException;
import java.sql.SQLDataException;
import java.util.List;
import java.util.Map;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserServiceImp userServiceImp) {
        this.userService = userServiceImp;
    }

    @Operation(summary = "Gets all users", tags = "user")
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
                    description = "Founds users by id",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not found",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema())
                            )
                    }
            )
    })
    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("user_id") Long id) {
        return userService.getByIdUser(id);
    }

    @Operation(summary = "Gets all users", tags = "user")
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
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createUser(@RequestBody User user) {
        String token = userService.createUser(user);
        return Map.of("token", token);
    }

    @Operation(summary = "Gets all users", tags = "user")
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
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestParam("file") MultipartFile file,
                            @RequestHeader("Authorization") String authorizationHeader) {
        userService.uploadImage(file, authorizationHeader);
    }

    @Operation(summary = "Gets all users", tags = "user")
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
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> login(@RequestParam("username") String username,
                                     @RequestParam("password") String password) throws ValuesNotMatchException {
        String token = userService.login(username, password);
        return Map.of("token", token);
    }

    @Operation(summary = "Gets all users", tags = "user")
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
    @GetMapping(value = "/get-image", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
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
}
