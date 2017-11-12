package com.hk.pimote;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hk.pimote.packet.PacketHandler;
import com.hk.pimote.packets.PacketIdentity;
import com.hk.pimote.packets.PacketSetting;
import com.hk.pimote.packets.PacketStatus;
import com.hk.pimote.stream.InStream;
import com.hk.pimote.stream.OutStream;
import com.hk.pimote.stream.Stream;
import com.pi4j.io.gpio.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: [Client|Server]");
            System.exit(-1);
        }

        try {
            if (args[0].equals("Client")) {
                System.out.println("Starting Client");
                startClient();
            } else if (args[0].equals("Server")) {
                System.out.println("Starting Server");
                startServer();
            } else {
                System.err.println("Usage: [Client|Server]");
                System.exit(-1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void startClient() throws Exception {
        while (true) {

            Socket s;
            try {
                s = new Socket("127.0.0.1", 1337);
            } catch (Exception e) {
                continue;
            }

            Stream in = new InStream(s.getInputStream());
            Stream out = new OutStream(s.getOutputStream());
            PacketHandler handler = new PacketHandler(in, out);

            handler.writePacket(new PacketIdentity("Room"));
            PacketStatus statusPacket = handler.readPacket(PacketStatus.class);

            final GpioController gpio = GpioFactory.getInstance();

            final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "Lamp", PinState.HIGH);

            pin.setShutdownOptions(true, PinState.LOW);
            pin.low();

            if (statusPacket.status == PacketStatus.STATUS_OK) {
                boolean isOn = false;
                while (true) {
                    try {
                        PacketStatus sp = handler.readPacket(PacketStatus.class);
                        System.out.println("Entered Loop");

                        if (sp.status == PacketStatus.STATUS_TURN) {
                            System.out.println("Status Turn");
                            pin.toggle();

                            isOn = !isOn;
                            handler.writePacket(new PacketSetting(isOn));

                        } else if (sp.status == PacketStatus.STATUS_SHUTDOWN) {
                            System.out.println("Status Shutdown");
                            gpio.shutdown();
                            s.close();
                            break;
                        }
                    } catch (Exception e) {

                    }
                }
            } else {
                System.out.println("What?");
                s.close();
            }
        }

    }

    private static void startServer() throws Exception {
        final ServerSocket s = new ServerSocket(45114);

        final Socket st = s.accept();
        Stream in = new InStream(st.getInputStream());
        Stream out = new OutStream(st.getOutputStream());
        final PacketHandler handler = new PacketHandler(in, out);

        PacketIdentity identityPacket = handler.readPacket(PacketIdentity.class);

        JFrame frame = new JFrame("Control: " + identityPacket.name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        final JButton turnButton = new JButton("Turn On");
        turnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    handler.writePacket(new PacketStatus(PacketStatus.STATUS_TURN));
                    PacketSetting settingPacket = handler.readPacket(PacketSetting.class);
                    turnButton.setText(settingPacket.isOn ? "Turn Off" : "Turn On");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        panel.add(turnButton);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    handler.writePacket(new PacketStatus(PacketStatus.STATUS_SHUTDOWN));
                    st.close();
                    s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        frame.setContentPane(panel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        handler.writePacket(new PacketStatus(PacketStatus.STATUS_OK));
    }
}
