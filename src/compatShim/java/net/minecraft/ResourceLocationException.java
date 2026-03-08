package net.minecraft;

public class ResourceLocationException extends IllegalArgumentException {
	public ResourceLocationException(String message) {
		super(message);
	}

	public ResourceLocationException(String message, Throwable cause) {
		super(message, cause);
	}
}
