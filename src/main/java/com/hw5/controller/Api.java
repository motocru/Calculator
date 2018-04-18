package com.hw5.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import com.hw5.models.Response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class Api {
	
	/*global variables for the default X and Y values*/
	BigInteger ax = new BigInteger("0");
	BigInteger sx = new BigInteger("1");
	BigInteger mx = new BigInteger("2");
	BigInteger dx = new BigInteger("4");
	BigInteger px = new BigInteger("8");
	BigInteger ay = new BigInteger("0");
	BigInteger sy = new BigInteger("0");
	BigInteger my = new BigInteger("3");
	BigInteger dy = new BigInteger("1");
	BigInteger py = new BigInteger("2");
	
	/*GET method section*/
	@RequestMapping(value="/{operation}", method = RequestMethod.GET)
	public Response result( @PathVariable String operation, @RequestParam(value = "x", required = false, defaultValue = "") String x, @RequestParam(value = "y", required = false, defaultValue= "") String y, @RequestHeader(value = "hash-alg") String hash) throws BadRequest {
		if (hash == null) throw new BadRequest("You need a hash algorithm in the header");
		String op = Symbol(operation);
		if (x.equals("")) x = XGetter(op);
		else if (!isInteger(x)) throw new BadRequest("X is not an integer");
		if (y.equals("")) y = YGetter(op);
		else if (!isInteger(y)) throw new BadRequest("Y is not an integer");
		else if (isInteger(y) && new BigInteger(y).compareTo(new BigInteger("0")) < 0 ) {
			throw new BadRequest("cannot have a negative integer when using a power function");
		}
		String result = Result(op, x, y);
		String hashInstance = hasher(op, x, y, result, hash);
		Response response = new Response.Builder().X(x).Y(y).Op(op).Hash(hashInstance).HashAlg(hash).Result(result).build();
		return response;
	}
	
	/*POST method section*/
	@RequestMapping(value="/{operation}", method = RequestMethod.POST)
	public Response resPost(@PathVariable String operation, @RequestParam(value = "x", required = false, defaultValue = "") String x, @RequestParam(value ="y", required = false, defaultValue = "") String y, @RequestHeader(value = "hash-alg") String hash) throws BadRequest {
		String op = Symbol(operation);
		if (x.equals("")) x = XGetter(op);
		else if (!isInteger(x)) throw new BadRequest("X is not an integer");
		else XSetter(x, op);
		if (y.equals("")) y = YGetter(op);
		else if (!isInteger(y)) throw new BadRequest("Y is not an integer");
		else if (isInteger(y) && new BigInteger(y).compareTo(new BigInteger("0")) < 0) {
			throw new BadRequest("cannot have a negative integer when using a power function");
		}
		else YSetter(y, op);
		String result = Result(op, x, y);
		String hashInstance = hasher(op, x, y, result, hash);
		Response response = new Response.Builder().X(x).Y(y).Hash(hashInstance).HashAlg(hash).Op(op).Result(result).build();
		return response;
	}
	
	private boolean isInteger(String num) {
		int i = 0;
		if (num.charAt(0) == '-') {
			if (num.length() == 1) return false;
			i = 1;
		}
		for (; i < num.length(); i++) {
			char c = num.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}
	
	private String Symbol(String operation) throws BadRequest {
		if (operation.equals("add")) return "+";
		else if (operation.equals("sub")) return "-";
		else if (operation.equals("mul")) return "x";
		else if (operation.equals("div")) return "/";
		else if (operation.equals("pow")) return "^";
		throw new BadRequest(operation+": is not a valid opereation");
	}
	
	private String XGetter(String op) {
		if (op.equals("+")) return ax.toString();
		else if (op.equals("-")) return sx.toString();
		else if (op.equals("x")) return mx.toString();
		else if (op.equals("/")) return dx.toString();
		else return px.toString();
	}
	
	private void XSetter(String x, String op) {
		if (op.equals("+")) ax = new BigInteger(x);
		else if (op.equals("-")) sx = new BigInteger(x);
		else if (op.equals("x")) mx = new BigInteger(x);
		else if (op.equals("/")) dx = new BigInteger(x);
		else px = new BigInteger(x);
	}
	
	private String YGetter(String op) {
		if (op.equals("+")) return "0";
		else if (op.equals("-")) return "0";
		else if (op.equals("x")) return "3";
		else if (op.equals("/")) return "1";
		else return "2";
	}
	
	private void YSetter(String y, String op) {
		if (op.equals("+")) ay = new BigInteger(y);
		else if (op.equals("-")) sy = new BigInteger(y);
		else if (op.equals("x")) my = new BigInteger(y);
		else if (op.equals("/")) dy = new BigInteger(y);
		else py = new BigInteger(y);
	}
	
	private String Result(String op, String x, String y) {
		BigInteger bigX = new BigInteger(x);
		BigInteger bigY = new BigInteger(y);
		if (op.equals("+")) return bigX.add(bigY).toString();
		else if (op.equals("-")) return bigX.subtract(bigY).toString();
		else if (op.equals("x")) return bigX.multiply(bigY).toString();
		else if (op.equals("/")) return bigX.divide(bigY).toString();
		else return bigX.pow(bigY.intValue()).toString();
	}
	
	private String hasher(String op, String x, String y, String result, String hash) throws BadRequest {
		hash = hash.toUpperCase();
		String data = x+op+y+"="+result;
		try {
			MessageDigest md = MessageDigest.getInstance(hash);
			byte[] hashRes = md.digest(data.getBytes("UTF-8"));
			return DatatypeConverter.printHexBinary(hashRes).toLowerCase();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new BadRequest(hash+":is not a vialble algorithm for hashing");
		}
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public class BadRequest extends Exception {
		public BadRequest(String message) {
			super(message);
		}
	}
}
