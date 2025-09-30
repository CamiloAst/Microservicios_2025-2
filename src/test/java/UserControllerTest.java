import com.example.usermanagement.dto.RegisterRequest;
import com.example.usermanagement.dto.ResetPasswordRequest;
import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.events.UserCreatedEvent;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.UserEventPublisher;
import com.example.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;
    @MockBean
    private UserEventPublisher userEventPublisher;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateUser_ok() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("juan");
        req.setEmail("juan@example.com");
        req.setPassword("1234");
        req.setPhoneNumber("+573001112233");

        UserResponse mockResp = new UserResponse();
        mockResp.setId(1L);
        mockResp.setUsername("juan");
        mockResp.setEmail("juan@example.com");

        when(userService.register(any(RegisterRequest.class))).thenReturn(mockResp);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));

        verify(userService, times(1)).register(any(RegisterRequest.class));
        verify(userEventPublisher, times(1)).publish(any(UserCreatedEvent.class));
    }

    @Test
    void testResetPassword_invalidToken() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setToken("fake-token");
        req.setNewPassword("nueva123");

        when(userRepository.findByResetToken("fake-token"))
                .thenReturn(Optional.empty());

        mvc.perform(patch("/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError()); // porque lanza RuntimeException
    }

    @Test
    void testGetUser_ok() throws Exception {
        UserResponse mockResp = new UserResponse();
        mockResp.setId(1L);
        mockResp.setUsername("maria");
        mockResp.setEmail("maria@example.com");

        when(userService.getUser(1L)).thenReturn(mockResp);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("maria"));
    }

    @Test
    void testDeleteUser_ok() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }

}
