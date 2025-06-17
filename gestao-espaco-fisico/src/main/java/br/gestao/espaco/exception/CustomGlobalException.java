package br.gestao.espaco.exception;

@SuppressWarnings("serial")
public abstract class CustomGlobalException extends RuntimeException {
	public abstract String getMsg();
}
