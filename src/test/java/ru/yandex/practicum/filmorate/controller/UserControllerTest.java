package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    @Test
    void getMappingRCode() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    void postMappingRCode() throws Exception {
        User user = new User();
        user.setEmail("sexyshmeksi@jora.thx");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 6, 11));
        User newUser = UserController.add(user);
        String userString = gson.toJson(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userString))
                .andExpect(status().isCreated());
    }

    @Test
    void postMappingRCodeWhenInvalidBody() throws Exception {
        User user = new User();
        user.setEmail("sexyshmeksi@jora.thx");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2229, 2, 26));
        User newUser = UserController.add(user);
        String userString = gson.toJson(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postMappingRCodeWhenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMappingRCode() throws Exception {
        User user = new User();
        user.setEmail("sexyshmeksi@jora.thx");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 6, 11));
        User userInfoToUpdate = UserController.add(user);
        String userRequestString = gson.toJson(userInfoToUpdate);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(status().isOk());
    }

    @Test
    void putMappingRCodeWhenInvalidBody() throws Exception {
        User user = new User();
        user.setEmail("sexyshmeksi@jora.thx");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2222, 2, 26));
        User userInfoToUpdate = UserController.add(user);
        String userRequestString = gson.toJson(userInfoToUpdate);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putMappingRCodeWhenInvalidID() throws Exception {
        User user = new User();
        user.setEmail("sexyshmeksi@jora.thx");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 6, 11));
        User userInfoToUpdate = UserController.add(user);
        userInfoToUpdate.setId(404L);
        String userRequestString = gson.toJson(userInfoToUpdate);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(status().isNotFound());
    }

    static class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            if (localDate == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(localDate.toString());
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            String dateString = jsonReader.nextString();
            if (dateString.isBlank()) {
                return null;
            }
            return LocalDate.parse(dateString);
        }
    }
}