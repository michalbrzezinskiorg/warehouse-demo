package utils.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

public class PageableResponseParser {
    public static UUID grabOneIdOfProduct(ArrayList content, int index) {
        try{
            String parse = ((LinkedHashMap) content.get(index)).get("id").toString();
            return UUID.fromString(parse);
        } catch (Exception e){
            throw new RuntimeException("id of returned object is not UUID");
        }
    }

    public static  ArrayList getContent(LinkedHashMap products) {
        try{
            return (ArrayList) products.get("content");} catch (Exception e){
            throw new RuntimeException("problem with parsing response content");
        }
    }
}
