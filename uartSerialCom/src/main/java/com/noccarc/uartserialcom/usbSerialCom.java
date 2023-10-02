package com.noccarc.uartserialcom;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class usbSerialCom {

    // baud rate = 9600 , dataBits = 0 , stopBits = 1 , parity = 0

    void getListOfCommPorts()
    {
        // Description: function to get the list of available Serial Ports

        ArrayList<Integer> commList  = new ArrayList<>();
        SerialPort[] comArr = SerialPort.getCommPorts(); // this function returns the list of arrays
        // finally return the array
    }


    static void sendData(SerialPort targetPort)
    {
        // Description: function to send Data to serial port

        System.out.printf("Sending Data to port : %s%n", targetPort);
        String data = "HelloWorld"; // data to send , later will be handled dynamically
        try {
            OutputStream outputStream = targetPort.getOutputStream();
            byte[] dataBytes = data.getBytes();
            outputStream.write(dataBytes);
            outputStream.flush(); // Flush the output stream to ensure data is sent immediately
            System.out.println("Successfully sent data to serial port.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static  void  receiveData(SerialPort targetPort)
    {
        // Description: function to receive data from serial Port

        System.out.printf("Receiving Data from port : %s%n",targetPort);
        try {

            while(true)
            {
                while(targetPort.bytesAvailable()==0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[targetPort.bytesAvailable()];
                int numRead = targetPort.readBytes(readBuffer, readBuffer.length);
                // Check if any bytes were read
                if (numRead > 0) {
                    String receivedData = new String(readBuffer, 0, numRead);
                    System.out.println("Received Data: " + receivedData);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

        try
        {
            SerialPort[] commList  = SerialPort.getCommPorts();  // return the array of all the serial port
            System.out.println("List Of Available Ports : " + Arrays.toString(commList));

            // port Descriptor
            String portDescriptor = "/dev/ttyUSB0";

            SerialPort targetPort = SerialPort.getCommPort(portDescriptor); // allocates a serial port object corresponding to the user specified descriptor

            System.out.println("setting params for serial port....");

            // set Com Ports Parameter
            boolean flag = targetPort.setComPortParameters(9600,8,1,SerialPort.NO_PARITY);
            if(flag)
            {
                System.out.println("successfully set the params for the communication Port.");
                // open port
                boolean portOpened = targetPort.openPort();
                if(portOpened)
                {
                    System.out.println("Port is Opened Successfully. Available For sending & receiving Data.");

                    //send Data
                    sendData(targetPort);

                    //receive Data
                    receiveData(targetPort);

                    //close the port
                    System.out.println("closing port....");
                    targetPort.closePort();
                    System.out.println("port closed successfully.");
                }
            }
            else
            {
                System.out.println("not able to set the params ");
            }

        }catch (Exception e)
        {
            System.out.println("Error : " + e.getMessage());
            e.printStackTrace();
        }






    }

}
