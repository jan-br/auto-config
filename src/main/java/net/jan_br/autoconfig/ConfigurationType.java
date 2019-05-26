package net.jan_br.autoconfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public interface ConfigurationType<T extends ConfigurationPath> {

    void save(ConfigurationAccessor configurationAccessor);

    void delete();

    T root();

    String[] format();

    ConfigurationFormatter getFormatter();

    void init(ConfigurationAccessor accessor);


    default void createFile(File file, String... contents) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
               if( file.createNewFile()) {
                   PrintWriter fileWriter = new PrintWriter(file);
                   Arrays.stream(contents).forEach(fileWriter::println);
                   fileWriter.flush();
                   fileWriter.close();
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
