package co.spribe.utils;

import co.spribe.exceptions.JsonProcessingRuntimeException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Utility class for JSON serialization and deserialization using Jackson. */
public class JsonUtil {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private JsonUtil() {}

  /**
   * Serialize an object to a pretty-printed JSON string.
   *
   * @param object the object to serialize
   * @param ignoreNull whether to ignore null fields during serialization
   * @return the pretty-printed JSON string representation of the object
   * @throws JsonProcessingRuntimeException if serialization fails
   */
  public static String toJson(Object object, boolean ignoreNull) {
    ObjectMapper mapper =
        ignoreNull ? MAPPER.copy().setSerializationInclusion(JsonInclude.Include.NON_NULL) : MAPPER;

    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new JsonProcessingRuntimeException("Failed to serialize object to JSON", e);
    }
  }

  public static String toJson(Object object) {
    return toJson(object, true);
  }

  /**
   * Convert an object to another type using Jackson's conversion capabilities.
   *
   * @param fromValue the source object to convert
   * @param toValueType the target class type
   * @param ignoreNull whether to ignore null fields during conversion
   * @param <T> the target type
   * @return the converted object of the target type
   * @throws JsonProcessingRuntimeException if conversion fails
   */
  public static <T> T convertValue(Object fromValue, Class<T> toValueType, boolean ignoreNull) {
    ObjectMapper mapper =
        ignoreNull ? MAPPER.copy().setSerializationInclusion(JsonInclude.Include.NON_NULL) : MAPPER;

    try {
      return mapper.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      throw new JsonProcessingRuntimeException(
          "Failed to convert object to " + toValueType.getSimpleName(), e);
    }
  }

  /**
   * Clone an object by serializing and deserializing it.
   *
   * @param original the original object to clone
   * @param type the class type of the cloned object
   * @param <T> the type of the cloned object
   * @return a deep clone of the original object
   * @throws JsonProcessingRuntimeException if cloning fails
   */
  public static <T> T cloneObject(Object original, Class<T> type) {
    try {
      return MAPPER.readValue(MAPPER.writeValueAsString(original), type);
    } catch (JsonProcessingException e) {
      throw new JsonProcessingRuntimeException(
          "Failed to clone object of type " + type.getSimpleName(), e);
    }
  }
}
