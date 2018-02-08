package de.cryten.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;

import de.cryten.MainClass;

public class ClassPath {
	
	public static void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error " + url + "");
        }
    }
	
	public static void checkLibs() {
		
        try {
            final File[] libs = new File[] {
                    new File(MainClass.getInstance().getDataFolder(), "guava-19.0.jar"),
                    new File(MainClass.getInstance().getDataFolder(), "slf4j-api-1.7.25.jar"),
                    new File(MainClass.getInstance().getDataFolder(), "HikariCP-2.7.3.jar") };
            for (final File lib : libs) {
                if (!lib.exists()) {
                    JarUtils.extractFromJar(lib.getName(), lib.getAbsolutePath());
                }
            }
            for (final File lib : libs) {
                if (!lib.exists()) {
                	MainClass.getInstance().getLogger().warning("Plugin! Finde Lib nicht: " + lib.getName());
                    Bukkit.getServer().getPluginManager().disablePlugin(MainClass.getInstance());
                    return;
                }
                ClassPath.addClassPath(JarUtils.getJarUrl(lib));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
	}
	
	
}
