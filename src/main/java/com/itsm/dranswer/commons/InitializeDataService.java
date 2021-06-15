package com.itsm.dranswer.commons;

/*
 * @package : com.itsm.dranswer.commons
 * @name : InitializeDataService.java
 * @date : 2021-06-14 오후 6:13
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsm.dranswer.users.AgencyInfo;
import com.itsm.dranswer.users.AgencyInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InitializeDataService implements CommandLineRunner {

    @Autowired
    private AgencyInfoRepo agencyInfoRepo;

    private final String FILE_INIT_AGENCY = "init/init_agency.json";

    @Override
    public void run(String... args) throws Exception {

        try {
            initAgencyFromFile();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initAgencyFromFile() throws IOException {
        List<AgencyInfo> agencyList = new ArrayList<>();
        try (InputStream is = getStreamFromResource(FILE_INIT_AGENCY)) {
            JsonNode jsonNode = getJsonNode(is, "agencyInfo");
            agencyList = getAgencyListFromNode(jsonNode);
        }

        for(AgencyInfo agencyInfo : agencyList){
            agencyInfoRepo.save(agencyInfo);
        }
    }

    private InputStream getStreamFromResource(String fileLocation) {
        ClassLoader classLoader = InitializeDataService.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileLocation);

        if (inputStream == null) {
            throw new IllegalArgumentException("init data file \"" + fileLocation + "\" not found.");
        } else {
            return inputStream;
        }
    }

    private JsonNode getJsonNode(InputStream is, String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try (DataInputStream dis = new DataInputStream(is)) {
            jsonNode = objectMapper.readTree(dis).path(key);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return jsonNode;
    }

    private List<AgencyInfo> getAgencyListFromNode(JsonNode jsonNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<AgencyInfo> agencyList = objectMapper.convertValue(jsonNode, new TypeReference<List<AgencyInfo>>() {
        }).stream().collect(Collectors.toList());
        return agencyList;
    }

}
