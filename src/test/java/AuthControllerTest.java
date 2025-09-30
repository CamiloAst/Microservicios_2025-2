import com.example.usermanagement.dto.ForgotPasswordRequest;
import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.events.UserLoginEvent;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.UserEventPublisher;
import com.example.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mockeamos las dependencias del controlador
    @MockBean
    private UserService userService;

    @MockBean
    private UserEventPublisher userEventPublisher;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLogin_ok() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("juan");
        req.setPassword("1234");

        User mockUser = new User();
        mockUser.setUsername("juan");
        mockUser.setEmail("juan@example.com");

        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(mockUser));
        when(userService.login(any(LoginRequest.class))).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));

        verify(userEventPublisher, times(1)).publish(any(UserLoginEvent.class));
    }

    @Test
    void testLogin_userNotFound() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setUsername("inexistente");
        req.setPassword("1234");

        when(userRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError()); // porque lanza UsernameNotFoundException
    }

    @Test
    void testForgotPassword_ok() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("juan@example.com");

        when(userService.forgotPassword(any(ForgotPasswordRequest.class)))
                .thenReturn("reset-token-123");

        mockMvc.perform(post("/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("reset-token-123"));
    }
}