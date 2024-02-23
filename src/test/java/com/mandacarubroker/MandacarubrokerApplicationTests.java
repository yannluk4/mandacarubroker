package com.mandacarubroker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldGetAllStocks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void shouldNotGetStockByInexistentId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks/{id}", "teste_falha"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void shouldCreateStock() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void shouldNotAllowCreateStockWithNonPositivePrice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  -10}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void shouldNotAllowCreateStockWithZeroValueOnPrice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  0}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void shouldReturnDataAfterCreate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("Test Stock"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.symbol").value("SANB4"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(45.2));
	}

	@Test
	void shouldReturnSuccessfulCodeAfterGetAllStocks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void shouldReturnCreatedCodeAfterCreatedStockData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void shouldReturnNotFoundWhenIdNotFoundOnGetData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks/{id}", "non_existing_id"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void shouldReturnNotAllowedWhenIdNotFoundOnPostData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks/{id}", "non_existing_id")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
	}

	@Test
	void shouldReturnNotFoundWhenIdNotFoundOnPutData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/stocks/{id}", "non_existing_id")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"TST4\", \"price\":  45}"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void shouldReturnNotFoundWhenIdNotFoundOnDeleteData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/{id}", "non_existing_id"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}


	@Test
	void shouldUpdateStockById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/stocks/{id}", "f19197c4-38be-462c-a99f-4d32e91c74f3")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"TST4\", \"price\":  45}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void shouldNotUpdateStockByInexistentId() throws Exception {
		// Perform a PUT request with a non-existent ID
		mockMvc.perform(MockMvcRequestBuilders.put("/stocks/{id}", "")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"Updated Stock\", \"symbol\": \"UPD\" }"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void shouldDeleteStockById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/{id}", "1b1ab7e8-bf1c-4cbf-98a9-94288e04fb87"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void shouldNotAllowDeleteAllStocks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/stocks"))
				.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
	}
}
