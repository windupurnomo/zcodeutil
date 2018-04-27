package id.zcode.util.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import id.zcode.util.Helper;
import id.zcode.util.model.Response;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Iterator;

public class BaseController {
    protected ResponseEntity FORBIDDEN = new ResponseEntity(HttpStatus.FORBIDDEN);

    private static <T> T serializeQueryString(String paramIn, Class<T> clazz) {
        JSONObject a = new JSONObject(createObject(paramIn));
        JSONObject jsonObject = new JSONObject(createObject(paramIn));
        Iterator<String> iter = a.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = jsonObject.getString(key);
            if (key.contains("_")) {
                fattening(jsonObject, key, value);
                jsonObject.remove(key);
            } else {
                jsonObject.put(key, value);
            }
        }
        Gson gson = new GsonBuilder().create();
        T p = gson.fromJson(jsonObject.toString(), clazz);
        return p;
    }

    private static String createObject(String paramIn) {
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        paramIn = "{\"" + paramIn + "\"}";
        return paramIn;
    }

    private static void fattening(JSONObject jsonObject, String key, String value) {
        String[] arr = key.split("_");
        String k = arr[0];
        String v = arr[1];
        JSONObject o = jsonObject.isNull(k) ?
                new JSONObject() :
                jsonObject.getJSONObject(k);
        if (arr.length > 2) {
            jsonObject.put(k, o);
            String newKey = key.substring(key.indexOf("_") + 1, key.length());
            fattening(o, newKey, value);
        } else {
            o.put(v, value);
            jsonObject.put(k, o);
        }
    }

    protected <T> T loadValuesOf(String queryString, Class<T> clazz) {
        if (queryString == null) try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return serializeQueryString(queryString, clazz);
    }

    protected ResponseEntity getHttpStatus(Response response) {
        if (response == null)
            return new ResponseEntity(new Response(true), HttpStatus.OK);
        if (response.isError())
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        HttpStatus hs = response.getData() == null ? HttpStatus.BAD_REQUEST :
                HttpStatus.OK;
        return new ResponseEntity(response, hs);
    }

    protected ResponseEntity getHttpStatus(Response response, HttpStatus httpStatus) {
        if (response == null)
            return new ResponseEntity(new Response(true), HttpStatus.OK);
        if (response.isError())
            return new ResponseEntity(response, httpStatus);
        HttpStatus hs = response.getData() == null ? httpStatus :
                HttpStatus.OK;
        return new ResponseEntity(response, hs);
    }

    protected ResponseEntity getHttpStatus(Response response, HttpHeaders headers) {
        if (response == null)
            return new ResponseEntity(new Response(true), HttpStatus.OK);
        HttpStatus hs = response.getData() == null ? HttpStatus.BAD_REQUEST :
                HttpStatus.OK;
        return new ResponseEntity(response, headers, hs);
    }

    private enum FieldType {
        INT, DOUBLE, BYTE, SHORT, LONG, FLOAT, BOOLEAN, CHAR, JAVALANGSTRING, JAVAUTILUUID;

        static public FieldType lookup(String id) {
            return Helper.lookupEnum(FieldType.class, id);
        }
    }
}
