package com.example.presentation.resources;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ETagTools {

	public static String generate(Object content) {
    	if(content == null)
    		throw new IllegalArgumentException("El contenido no puede ser nullo");
        byte[] digest = Integer.toHexString(content.hashCode()).getBytes(StandardCharsets.UTF_8);
		try {
			digest = MessageDigest.getInstance("MD5").digest(digest);
		} catch (NoSuchAlgorithmException e) {
			log.warn("ETag sin hash"); 
		}
        return "\"" + HexFormat.of().formatHex(digest) + "\"";
    }
    public static boolean ifMatch(Object content, String ifMatch) {
    	if(ifMatch == null) return false;
    	if(!ifMatch.startsWith("\"")) ifMatch = "\"" + ifMatch;
    	if(!ifMatch.endsWith("\"")) ifMatch += "\"";
       return generate(content).equals(ifMatch);
    }
    public static boolean ifNoneMatch(Object content, String ifNoneMatch) {
        return ifNoneMatch != null && !ifMatch(content, ifNoneMatch);
    }
}
