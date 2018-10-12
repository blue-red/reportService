package com.cwn.wizbank.report.repository.dialect;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.UserType;
import org.postgresql.jdbc.PgArray;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 *  Postgresql JSON Array Data Type
 * @author Andrew.xiao 2018-05-03
 **/
public class JsonArrayType implements UserType{

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (CollectionUtils.isEmpty((List)value)) {
            st.setNull(index, Types.OTHER);
        } else {
            List<Map<String,Object>> jsonList = (List<Map<String,Object>>)value;
            st.setObject(index, toArrayTypeString(jsonList), Types.OTHER);
        }
    }

    @Override
    public Object deepCopy(Object originalValue) throws HibernateException {
        if (originalValue != null) {
            try {
                return mapper.readValue(mapper.writeValueAsString(originalValue),
                        returnedClass());
            } catch (IOException e) {
                throw new HibernateException("Failed to deep copy object", e);
            }
        }
        return null;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        PgArray o = (PgArray) rs.getObject(names[0]);
        List<Map<String,Object>> result = new ArrayList<>();
        if (o!= null) {
            String newFormat = "[" + o.toString().substring(1,o.toString().length()-1) +"]";
            JSONArray jsonArray = JSONArray.fromObject(newFormat);
            Map<String,Object> newItem;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));
                newItem = new HashMap<>();
                for (Object o1 : jsonObject.keySet()) {
                    if (jsonObject.get(o1) != null && !(jsonObject.get(o1) instanceof JSONNull)) {
                        newItem.put(o1.toString(),jsonObject.get(o1));
                    }
                }
                result.add(newItem);
            }
        }
        return result;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object copy = deepCopy(value);
        if (copy instanceof Serializable) {
            return (Serializable) copy;
        }
        throw new SerializationException(String.format("Cannot serialize '%s', %s is not Serializable.", value, value.getClass()), null);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return 0;
        }
        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

    @Override
    public Class<?> returnedClass() {
        return List.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    private String toArrayTypeString(List<Map<String,Object>> mapList){
        StringBuffer appender = new StringBuffer("{");
        for (Map<String, Object> mapItem : mapList) {
            String mapItemString = toJsonString(mapItem);
            appender.append(mapItemString)
                    .append(",");
        }
        String result = appender.subSequence(0,appender.length()-1).toString() + "}";
        //String result = "{\"{\\\"yy\\\":3}\", \"{\\\"tt\\\":44}\"}";
        return result;
    }

    private String toJsonString(Map<String, Object> mapItem) {
        StringBuffer appender = new StringBuffer("\"{");
        for (String s : mapItem.keySet()) {
            appender.append("\\\"")
                    .append(s)
                    .append("\\\":\\\"")
                    .append(mapItem.get(s))
                    .append("\\\"")
                    .append(",");
        }
        String result = appender.subSequence(0,appender.length()-1) + "}\"";
        return result;
    }

}