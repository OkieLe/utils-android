package io.github.boopited.droidutils.network.restful.interceptor

import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory

abstract class DelegatingSocketFactory(
        private val mSocketFactory: SocketFactory)
    : SocketFactory() {

    override fun createSocket(): Socket {
        val socket = mSocketFactory.createSocket()
        configureSocket(socket)
        return socket
    }

    override fun createSocket(host: String?,
                              port: Int): Socket {
        val socket = mSocketFactory.createSocket(host, port)
        configureSocket(socket)
        return socket
    }

    override fun createSocket(host: String?,
                              port: Int,
                              localHost: InetAddress?,
                              localPort: Int): Socket {
        val socket = mSocketFactory.createSocket(host, port, localHost, localPort)
        configureSocket(socket)
        return socket
    }

    override fun createSocket(host: InetAddress?,
                              port: Int): Socket {
        val socket = mSocketFactory.createSocket(host, port)
        configureSocket(socket)
        return socket
    }

    override fun createSocket(address: InetAddress?,
                              port: Int,
                              localAddress: InetAddress?,
                              localPort: Int): Socket {
        val socket = mSocketFactory.createSocket(address, port, localAddress, localPort)
        configureSocket(socket)
        return socket
    }

    abstract fun configureSocket(socket: Socket)
}