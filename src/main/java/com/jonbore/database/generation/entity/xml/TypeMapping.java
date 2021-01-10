package com.jonbore.database.generation.entity.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "type-mappings")
public class TypeMapping {
    @XmlElement(name = "db-type")
    private List<DbType> types;

    public List<DbType> getTypes() {
        return types;
    }

    public void setTypes(List<DbType> types) {
        this.types = types;
    }
}
