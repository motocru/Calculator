package com.hw5.models;

public class Response {
	private String x;
	private String y;
	private String op;
	private String result;
	private String hash;
	private String hashAlg;
	
	private Response(String x, String y, String op, String result, String hash, String hashAlg) {
		this.x = x;
		this.y = y;
		this.op = op;
		this.result = result;
		this.hash = hash;
		this.hashAlg = hashAlg;
	}
	
	public Response() {		
	}
	
	public String getX() {
		return x;
	}
	
	public String getY() {
		return y;
	}
	
	public String getOp() {
		return op;
	}
	
	public String getResult() {
		return result;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getHashAlg() {
		return hashAlg;
	}
	
	public void setX(String x) {
		this.x = x;
	}
	
	public void setY(String y) {
		this.y = y;
	}
	
	public void setOp(String op) {
		this.op = op;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public void setHashAlg(String hashAlg) {
		this.hashAlg = hashAlg;
	}
	
	public static class Builder {
		private String x;
		private String y;
		private String op;
		private String result;
		private String hash;
		private String hashAlg;
		
		public Builder X(String x) {
			this.x = x;
			return this;
		}
		
		public Builder Y(String y) {
			this.y = y;
			return this;
		}
		
		public Builder Op(String op) {
			this.op = op;
			return this;
		}
		
		public Builder Result(String result) {
			this.result = result;
			return this;
		}
		
		public Builder Hash(String hash) {
			this.hash = hash;
			return this;
		}
		
		public Builder HashAlg(String hashAlg) {
			this.hashAlg = hashAlg;
			return this;
		}
		
		public Response build() {
			return new Response(this.x, this.y, this.op, this.result, this.hash, this.hashAlg);
		}
	}
}
