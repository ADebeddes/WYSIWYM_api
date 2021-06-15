package com.lds_api;

import com.lds_api.controller.LDSController;
import com.lds_api.service.LDSService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LDSController.class)
public class LDSControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private LDSService ldsService;

    @Test
    public void testGetResim() throws Exception {
        //mockMvc.perform(get("/resim")).andExpect(status().isOk());
    }
    
}
