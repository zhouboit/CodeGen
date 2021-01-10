package com.jonbore.database.generation.entity.xml;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TypeMappingFactory {

    public static TypeMapping loadMappings() {
        try {
            JAXBContext context = JAXBContext.newInstance(TypeMapping.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (TypeMapping) unmarshaller.unmarshal(TypeMappingFactory.class.getResourceAsStream("/jdbc-java-types.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Map<String, Mapping>> getMapping() {
        TypeMapping mapping = loadMappings();
        Map<String, Map<String, Mapping>> typeMapping = new HashMap<>();
        if (mapping != null && mapping.getTypes() != null && !mapping.getTypes().isEmpty()) {
            mapping.getTypes().forEach(type -> {
                Map<String, Mapping> map = new HashMap<>();
                List<Mapping> mappings = type.getMappings();
                if (mappings != null && !mappings.isEmpty()) {
                    mappings.forEach(m -> {
                        map.put(m.getJdbc(), m);
                    });
                }
                typeMapping.put(type.getName(), map);
            });
        }
        return typeMapping;
    }
}
