package ru.ghost;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.management.OperatingSystemMXBean;

import java.io.OutputStream;
import java.lang.management.ManagementFactory;

public class StatsToArduino {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: java -j StatsToArduino <serial-port>");
            return;
        }

        SerialPort serialPort = SerialPort.getCommPort(args[0]);
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        byte[] buffer = new byte[2];

        if (serialPort.openPort()) {

            try (OutputStream outputStream = serialPort.getOutputStream()) {
                while (true) {
                    buffer[0] = (byte) (int) (os.getCpuLoad()  * 100);
                    buffer[1] = (byte) Math.toIntExact(100 - (os.getFreeMemorySize() / (os.getTotalMemorySize() / 100)));
                    outputStream.write(buffer);
                    outputStream.flush();
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                serialPort.closePort();
            }
        }
    }
}