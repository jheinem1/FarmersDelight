package net.minecraft.resources;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;
import java.util.function.UnaryOperator;

public final class ResourceLocation implements Comparable<ResourceLocation> {
	public static final Codec<ResourceLocation> CODEC = Identifier.CODEC.xmap(ResourceLocation::new, ResourceLocation::identifier);
	public static final StreamCodec<ByteBuf, ResourceLocation> STREAM_CODEC = Identifier.STREAM_CODEC.map(ResourceLocation::new, ResourceLocation::identifier);
	public static final char NAMESPACE_SEPARATOR = Identifier.NAMESPACE_SEPARATOR;
	public static final String DEFAULT_NAMESPACE = Identifier.DEFAULT_NAMESPACE;
	public static final String REALMS_NAMESPACE = Identifier.REALMS_NAMESPACE;

	private final Identifier identifier;

	private ResourceLocation(Identifier identifier) {
		this.identifier = Objects.requireNonNull(identifier);
	}

	public ResourceLocation(String namespace, String path) {
		this(Identifier.fromNamespaceAndPath(namespace, path));
	}

	public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
		return new ResourceLocation(Identifier.fromNamespaceAndPath(namespace, path));
	}

	public static ResourceLocation parse(String value) {
		try {
			return new ResourceLocation(Identifier.parse(value));
		} catch (RuntimeException ex) {
			throw new ResourceLocationException(ex.getMessage(), ex);
		}
	}

	public static ResourceLocation withDefaultNamespace(String path) {
		return new ResourceLocation(Identifier.withDefaultNamespace(path));
	}

	public static ResourceLocation tryParse(String value) {
		Identifier parsed = Identifier.tryParse(value);
		return parsed == null ? null : new ResourceLocation(parsed);
	}

	public static ResourceLocation tryBuild(String namespace, String path) {
		Identifier parsed = Identifier.tryBuild(namespace, path);
		return parsed == null ? null : new ResourceLocation(parsed);
	}

	public static ResourceLocation bySeparator(String value, char separator) {
		return new ResourceLocation(Identifier.bySeparator(value, separator));
	}

	public static ResourceLocation tryBySeparator(String value, char separator) {
		Identifier parsed = Identifier.tryBySeparator(value, separator);
		return parsed == null ? null : new ResourceLocation(parsed);
	}

	public static ResourceLocation read(StringReader reader) throws CommandSyntaxException {
		return new ResourceLocation(Identifier.read(reader));
	}

	public static ResourceLocation readNonEmpty(StringReader reader) throws CommandSyntaxException {
		return new ResourceLocation(Identifier.readNonEmpty(reader));
	}

	public String getPath() {
		return this.identifier.getPath();
	}

	public String getNamespace() {
		return this.identifier.getNamespace();
	}

	public ResourceLocation withPath(String path) {
		return new ResourceLocation(this.identifier.withPath(path));
	}

	public ResourceLocation withPath(UnaryOperator<String> pathOperator) {
		return new ResourceLocation(this.identifier.withPath(pathOperator));
	}

	public ResourceLocation withPrefix(String prefix) {
		return new ResourceLocation(this.identifier.withPrefix(prefix));
	}

	public ResourceLocation withSuffix(String suffix) {
		return new ResourceLocation(this.identifier.withSuffix(suffix));
	}

	public String toDebugFileName() {
		return this.identifier.toDebugFileName();
	}

	public String toLanguageKey() {
		return this.identifier.toLanguageKey();
	}

	public String toShortLanguageKey() {
		return this.identifier.toShortLanguageKey();
	}

	public String toShortString() {
		return this.identifier.toShortString();
	}

	public String toLanguageKey(String prefix) {
		return this.identifier.toLanguageKey(prefix);
	}

	public String toLanguageKey(String prefix, String suffix) {
		return this.identifier.toLanguageKey(prefix, suffix);
	}

	public Identifier identifier() {
		return this.identifier;
	}

	@Override
	public String toString() {
		return this.identifier.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ResourceLocation other && this.identifier.equals(other.identifier);
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}

	@Override
	public int compareTo(ResourceLocation other) {
		return this.identifier.compareTo(other.identifier);
	}
}
