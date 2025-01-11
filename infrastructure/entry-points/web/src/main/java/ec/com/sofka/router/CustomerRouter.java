package ec.com.sofka.router;

import ec.com.sofka.data.CustomerRequestDTO;
import ec.com.sofka.data.CustomerResponseDTO;
import ec.com.sofka.exceptions.RequestValidator;
import ec.com.sofka.exceptions.model.ErrorDetails;
import ec.com.sofka.handlers.customer.CreateCustomerHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CustomerRouter {
    private final RequestValidator requestValidator;
    private final CreateCustomerHandler createCustomerHandler;

    public CustomerRouter(RequestValidator requestValidator, CreateCustomerHandler createCustomerHandler) {
        this.requestValidator = requestValidator;
        this.createCustomerHandler = createCustomerHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/customers",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = CreateCustomerHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            tags = {"Users"},
                            operationId = "createUser",
                            summary = "Create a new customer",
                            description = "Create a new customer from the request data.",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CustomerRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Record created successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "The entity has a conflict.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal application problems.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            ),
//            @RouterOperation(
//                    path = "/api/customers",
//                    method = RequestMethod.GET,
//                    beanClass = GetAllUsersHandler.class,
//                    beanMethod = "getAllUsers",
//                    operation = @Operation(
//                            tags = {"Users"},
//                            operationId = "getAllUsers",
//                            summary = "Get all customers",
//                            description = "Get all registered customers.",
//                            responses = {
//                                    @ApiResponse(
//                                            responseCode = "200",
//                                            description = "Successfully obtained all registered customers.",
//                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
//                                    ),
//                                    @ApiResponse(
//                                            responseCode = "400",
//                                            description = "Bad request.",
//                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
//                                    ),
//                                    @ApiResponse(
//                                            responseCode = "500",
//                                            description = "Internal application problems.",
//                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
//                                    )
//                            }
//                    )
//            )
    })
    public RouterFunction<ServerResponse> customersRouters() {
        return RouterFunctions
                .route(POST("/api/customers").and(accept(APPLICATION_JSON)), this::saveCustomer);
//                .andRoute(GET("/api/customers"), this::allUsers);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerRequestDTO.class)
                .flatMap(requestValidator::validate)
                .flatMap(createCustomerHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

//    public Mono<ServerResponse> allUsers(ServerRequest request) {
//        return getAllUsersHandler.getAllUsers()
//                .collectList()
//                .flatMap(list -> ServerResponse.ok()
//                        .contentType(APPLICATION_JSON)
//                        .bodyValue(list)
//                );
//    }

}