package org.fileManagement.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /*
   * Write all members to file
   */
  public static <T> void writeObjectToJsonFile(List<T> objects, String filePath) {
    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), objects);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * Read all members from file
   */
  public static <T> List<T> readObjectsFromJsonFile(String filePath, Class<T> class_) {
    File file = new File(filePath);
    if (!file.exists()) {
      return new ArrayList<>();
    }
    try {
      return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, class_));
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /*
   * Add a single member
   */

  public static <T> void addObjectToJsonFile(String filePath, T newObject, Class<T> class_) {
    List<T> objects = readObjectsFromJsonFile(filePath, class_);
    objects.add(newObject);
    writeObjectToJsonFile(objects, filePath);
  }
}
