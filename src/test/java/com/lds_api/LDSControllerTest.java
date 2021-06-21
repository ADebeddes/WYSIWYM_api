package com.lds_api;

import com.lds_api.controller.LDSController;
import com.lds_api.service.LDSService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
public class LDSControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void testNewBenchmark() throws Exception {
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Bus\", \"resource2\" : \"Car\",\"benchmark\": 0.45864},{\"resource1\":\"Eiffel_Tower\",\"resource2\":\"Gustave_Eiffel\",\"benchmark\": 0.236894},{\"resource1\":\"Eiffel_Tower\",\"resource2\":\"Statue_of_Liberty\",\"benchmark\": 0.1364}],\"options\":{ \"benchmark\":true,\"benchmarkName\":\"none\",\"correlationType\":\"pearson\",\"threads\":1,\"useIndex\":true,\"measureType\":\"Resim\"}}"
    			);
    	mockMvc.perform(post("/similarity")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
    
    @Test
    public void testSimpleSimilarity() throws Exception {
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Cat\", \"resource2\" : \"Dog\",\"benchmark\": 0.45864}],\"options\":{ \"benchmark\":false,\"benchmarkName\":\"\",\"correlationType\":\"\",\"threads\":1,\"useIndex\":true,\"measureType\":\"Resim\"}}"
    			);
    	mockMvc.perform(post("/similarity")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
    
    @Test
    public void testMultipleSimilarity() throws Exception {
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Bus\", \"resource2\" : \"Car\"},{ \"resource1\": \"Eiffel_Tower\", \"resource2\" : \"Gustave_Eiffel\"},{ \"resource1\": \"Eiffel_Tower\", \"resource2\" : \"Statue_of_Liberty\"},{ \"resource1\": \"Cat\", \"resource2\" : \"Dog\"},{ \"resource1\": \"The_Noah\", \"resource2\" : \"The_Pack_(2010_film)\"}],\"options\":{ \"benchmark\":false,\"benchmarkName\":\"\",\"correlationType\":\"\",\"threads\":1,\"useIndex\":true,\"measureType\":\"Resim\"}}"
    			);
    	mockMvc.perform(post("/similarity")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
    
}
