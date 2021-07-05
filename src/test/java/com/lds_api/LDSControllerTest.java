package com.lds_api;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void testMicroMeasure() throws Exception {
    	System.out.println("test5");
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Bus\", \"resource2\" : \"Car\",\"property\" : \"abstract\"}],\"options\":{\"weight\" :0.3,\"measureType\":\"normalizedLevenshtein\"}}"
    			);
    	mockMvc.perform(post("/microMeasure")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
	
	@Test
    public void testNewBenchmark() throws Exception {
    	System.out.println("test1");
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
    public void testExistingBenchmark() throws Exception {
    	System.out.println("test2");
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Bus\", \"resource2\" : \"Car\",\"benchmark\": 0.45864},{\"resource1\":\"Eiffel_Tower\",\"resource2\":\"Gustave_Eiffel\",\"benchmark\": 0.236894},{\"resource1\":\"Eiffel_Tower\",\"resource2\":\"Statue_of_Liberty\",\"benchmark\": 0.1364}],\"options\":{ \"benchmark\":true,\"benchmarkName\":\"mc30\",\"correlationType\":\"pearson\",\"threads\":1,\"useIndex\":true,\"measureType\":\"Resim\"}}"
    			);
    	mockMvc.perform(post("/similarity")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
    
    @Test
    public void testSimpleSimilarity() throws Exception {
    	System.out.println("test3");
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
    	System.out.println("test4");
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Bus\", \"resource2\" : \"Car\"},{ \"resource1\": \"Eiffel_Tower\", \"resource2\" : \"Gustave_Eiffel\"},{ \"resource1\": \"Eiffel_Tower\", \"resource2\" : \"Statue_of_Liberty\"},{ \"resource1\": \"Cat\", \"resource2\" : \"Dog\"},{ \"resource1\": \"The_Noah\", \"resource2\" : \"The_Pack_(2010_film)\"}],\"options\":{ \"benchmark\":false,\"benchmarkName\":\"\",\"correlationType\":\"\",\"threads\":1,\"useIndex\":true,\"measureType\":\"Resim\"}}"
    			);
    	mockMvc.perform(post("/similarity")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
    
    /*@Test
    public void testMultipleSimilarityWithoutIndex() throws Exception {
    	System.out.println("test5");
    	JSONObject jo = new JSONObject(
    			"{\"ldDatasetMain\" :{\"name\": \"DBPedia_en\",\"prefixes\":{ \"nsPrefixMap\":{\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"dbpedia\":\"http://dbpedia.org/resource/\",\"dbpediaowl\":\"http://dbpedia.org/ontology/\",\"rdf\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"}},\"link\":\"http://dbpedia.org/sparql\",\"defaultGraph\" : \"http://dbpedia.org\",\"baseResourceURL\" : \"http://dbpedia.org/resource/\"},\"resources\" : [ { \"resource1\": \"Bus\", \"resource2\" : \"Car\"},{ \"resource1\": \"Eiffel_Tower\", \"resource2\" : \"Gustave_Eiffel\"},{ \"resource1\": \"Eiffel_Tower\", \"resource2\" : \"Statue_of_Liberty\"},{ \"resource1\": \"Cat\", \"resource2\" : \"Dog\"},{ \"resource1\": \"The_Noah\", \"resource2\" : \"The_Pack_(2010_film)\"}],\"options\":{ \"benchmark\":false,\"benchmarkName\":\"\",\"correlationType\":\"\",\"threads\":1,\"useIndex\":false,\"measureType\":\"Resim\"}}"
    			);
    	mockMvc.perform(post("/similarity")
    			  .contentType("application/json;charset=UTF-8")
    		      .content(jo.toString()))
    		      .andExpect(MockMvcResultMatchers.status().isOk())
    		      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }*/
    
    @Test
    public void testGetMeasures() throws Exception {
    	System.out.println("test6");
    	mockMvc.perform(post("/measures")
    			  .contentType("application/json;charset=UTF-8"))
    		      .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
}
