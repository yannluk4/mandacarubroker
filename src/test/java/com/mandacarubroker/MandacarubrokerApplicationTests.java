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

	/*** Check that you can get all the stocks. */
	@Test
	void shouldGetAllStocks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/*** Check that you can't get a stock with a non-existent ID. */
	@Test
	void shouldNotGetStockByInexistentId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks/{id}", "teste_falha"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/*** Check if you can create a new stock. */
	@Test
	void shouldCreateStock() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	/*** Check that it is not possible to create a stock with a non-positive price.*/
	@Test
	void shouldNotAllowCreateStockWithNonPositivePrice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  -10}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	/*** Check that it is not possible to create a stock with a zero price.*/
	@Test
	void shouldNotAllowCreateStockWithZeroValueOnPrice() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  0}"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	/*** Checks that the data returned after creating a stock has the correct structure.*/
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

	/*** Checks that the return code after getting all the allotments is successful (200).*/
	@Test
	void shouldReturnSuccessfulCodeAfterGetAllStocks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/*** Checks that the return code after creating data for a stock is "created" (201).*/
	@Test
	void shouldReturnCreatedCodeAfterCreatedStockData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	/*** Checks if the return code is "not found" (404) when the ID is not found when retrieving data.*/
	@Test
	void shouldReturnNotFoundWhenIdNotFoundOnGetData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stocks/{id}", "non_existing_id"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/*** Checks that the return code is "not allowed" (400) when the ID is not found when posting data.*/
	@Test
	void shouldReturnNotAllowedWhenIdNotFoundOnPostData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stocks/{id}", "non_existing_id")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"SANB4\", \"price\":  45.2}"))
				.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
	}

	/*** Checks if the return code is "not found" (404) when the ID is not found when updating data.*/
	@Test
	void shouldReturnNotFoundWhenIdNotFoundOnPutData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/stocks/{id}", "non_existing_id")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"TST4\", \"price\":  45}"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/*** Checks if the return code is "not found" (404) when the ID is not found when deleting data.*/
	@Test
	void shouldReturnNotFoundWhenIdNotFoundOnDeleteData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/{id}", "non_existing_id"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/*** Checks whether it is possible to update a stock by ID.*/
	@Test
	void shouldUpdateStockById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/stocks/{id}", "f19197c4-38be-462c-a99f-4d32e91c74f3")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"companyName\": \"Test Stock\", \"symbol\": \"TST4\", \"price\":  45}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	/*** Checks that it is not possible to update a stock with a non-existent ID.*/
	@Test
	void shouldNotUpdateStockByInexistentId() throws Exception {
		// Perform a PUT request with a non-existent ID
		mockMvc.perform(MockMvcRequestBuilders.put("/stocks/{id}", "")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{ \"name\": \"Updated Stock\", \"symbol\": \"UPD\" }"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/*** Checks whether it is possible to delete a stock by ID.*/
	@Test
	void shouldDeleteStockById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/{id}", "1b1ab7e8-bf1c-4cbf-98a9-94288e04fb87"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/*** Check that it is not possible to delete all stocks.*/
	@Test
	void shouldNotAllowDeleteAllStocks() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/stocks"))
				.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
	}
}
