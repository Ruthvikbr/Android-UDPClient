package com.eatsy.udpclient

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    private val serverIp = "10.0.0.46"  // Change to the server's IP
    private val serverPort = 9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val button:Button = findViewById(R.id.button)
        button.setOnClickListener {
            sendMessage("Hello Server")
        }
    }

    private fun sendMessage(message:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = DatagramSocket()
                val serverAddress = InetAddress.getByName(serverIp)
                val sendData = message.toByteArray()

                val packet = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
                socket.send(packet)
                Log.d("PACKER", "Sent")
                // Wait for a response
                val buffer = ByteArray(1024)
                val responsePacket = DatagramPacket(buffer, buffer.size)
                socket.receive(responsePacket)
                val responseMessage = String(responsePacket.data, 0, responsePacket.length)

                println("Received from server: $responseMessage")

            } catch (e: Exception) {
                print(e.toString())
            }
        }
    }
}