package com.atzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static void createResourceFile(File file, RPGInventory plugin) {
        File file_ = new File(plugin.getDataFolder() + "/" + file.getPath());
        if (file_.exists())
            return;

        plugin.saveResource(file.getPath(), false);
        plugin.getLogger().info("Resource file " + file.getPath() + " created.");
    }

    public static void rewrite(File file, String text) throws IOException {
        //This method erases the text into the file and writes the new text
        if (!file.exists())
            return;

        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(text);
        bw.flush();
        bw.close();
        writer.close();
    }

    public static void createFile(File file, RPGInventory plugin) {
        if (file.exists())
            return;

        if (file.getParentFile() != null && !file.getParentFile().exists())
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();

        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            plugin.getLogger().info("File " + file.getPath() + " created.");
        } catch (IOException e) {
            plugin.getLogger().severe("Error while creating file " + file.getPath() + ": " + e.getMessage());
        }
    }

}
