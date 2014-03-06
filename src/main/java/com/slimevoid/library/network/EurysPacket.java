/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * Lesser General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>
 */
package com.slimevoid.library.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;

/**
 * Packet Information Base
 * 
 * @author Eurymachus
 * 
 */
public abstract class EurysPacket {
    /**
     * Used to separate packets into a different send queue.
     */
    public boolean isChunkDataPacket = false;

    /**
     * The channel for the packet
     */
    private String channel;

    /**
     * Sets the packet channel
     * 
     * @param channel
     *            the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Retrieves the channel for this packet
     * 
     * @return channel
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Writes data to the packet
     * 
     * @param data
     *            the outputstream to write to
     * 
     * @throws IOException
     *             if data is corrupt/null
     */
    public abstract void writeData(DataOutputStream data) throws IOException;

    /**
     * Reads data from the packet
     * 
     * @param data
     *            the inputstream to read from
     * 
     * @throws IOException
     *             if data is corrupt/null
     */
    public abstract void readData(DataInputStream data) throws IOException;

    /**
     * The packet ID usually listed with PacketIds.class
     * 
     * @return the Packet ID for this packet instance
     */
    public abstract int getID();

    /**
     * Gets a readable output for this packet instance
     * 
     * @param full
     *            should return the full packet text
     * 
     * @return toString()
     */
    public String toString(boolean full) {
        return toString();
    }

    /**
     * Gets a readable output for this packet instance
     */
    @Override
    public String toString() {
        return getID() + " " + getClass().getSimpleName();
    }

    public FMLProxyPacket getPacket() {
        boolean isServer = FMLCommonHandler.instance().getSide() == Side.SERVER;
        Side target = isServer ? Side.CLIENT : Side.SERVER;
        byte[] bytes = this.getByteArray();
        ByteBuf buffer;
        try {
            buffer = Unpooled.wrappedBuffer(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            buffer = Unpooled.EMPTY_BUFFER;
        }
        FMLProxyPacket fmlPacket = new FMLProxyPacket(buffer, this.getChannel());
        fmlPacket.setTarget(target);
        // FMLProxyPacket fmlSidedPacket = isServer ? new
        // FMLProxyPacket(this.getServerPacket()) : new
        // FMLProxyPacket(this.getClientPacket());
        return fmlPacket;
    }

    /**
     * Retrieves the Custom Packet and Payload data as Packet250CustomPayload
     */
    public C17PacketCustomPayload getClientPacket() {
        C17PacketCustomPayload packet = new C17PacketCustomPayload(this.getChannel(), this.getByteArray());
        return packet;
    }

    public S3FPacketCustomPayload getServerPacket() {
        S3FPacketCustomPayload packet = new S3FPacketCustomPayload(this.getChannel(), this.getByteArray());
        return packet;
    }

    private byte[] getByteArray() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        try {
            data.writeByte(getID());
            writeData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes.toByteArray();
    }
}
