package com.spring.simulation.clazz;

import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {
  public Class<?> loadClassFromStream(InputStream inputStream)
      throws ClassNotFoundException, IOException, IOException {
    byte[] buffer = new byte[inputStream.available()];
    inputStream.read(buffer);
    return defineClass(null, buffer, 0, buffer.length);
  }
}
