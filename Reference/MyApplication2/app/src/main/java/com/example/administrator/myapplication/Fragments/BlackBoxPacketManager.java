package com.example.administrator.myapplication.Fragments;

import com.example.administrator.myapplication.Module.BlackBoxModule.Packet;

import java.util.ArrayList;

/**
 * Created by test02 on 2017-11-29.
 */

public class BlackBoxPacketManager {

    private ArrayList<blackBoxPacketContainer> blackboxPacketDataContainer = new ArrayList<blackBoxPacketContainer>();

    public class blackBoxPacketContainer{

        private Packet theLatestPacket;
        private String key;
        private ArrayList<Packet> dataContainer = new ArrayList<Packet>();

        public blackBoxPacketContainer(String _key){
            key = _key;
        }

        public boolean addPacketData(Packet packet)
        {
            if(packet.BlackboxID.equals(key)){

                theLatestPacket = packet;
                dataContainer.add(packet);
                return true;
            }
            return false;
        }

        public Packet getLatestPacket(){ return theLatestPacket; }
        public String getKey(){
            return key;
        }

        public ArrayList<Packet> getDatas(){
            return (ArrayList<Packet>)dataContainer.clone();
        }
    }

    public boolean addBlackBox(String id)
    {
        if(!isContained(id)){
            blackboxPacketDataContainer.add(new BlackBoxPacketManager.blackBoxPacketContainer(id));
            return true;
        }

        return false;
    }

    public boolean addPacketdata(String id, Packet packet){

        blackBoxPacketContainer container = getPacketContainer(id);
        if(container != null){

            container.addPacketData(packet);
            return true;
        }

        return false;
    }

    public blackBoxPacketContainer getPacketContainer(String id){

        for(blackBoxPacketContainer container : blackboxPacketDataContainer){

            if(container.getKey().equals(id)){
                return container;
            }
        }

        return null;
    }

    public blackBoxPacketContainer getPacketContainer(int i){

        if(i < blackboxPacketDataContainer.size()) {

            return blackboxPacketDataContainer.get(i);
        }

        return null;
    }

    private boolean isContained(String id){

        boolean isContained = false;
        for( blackBoxPacketContainer container : blackboxPacketDataContainer){

            if(container.getKey().equals(id)){
                isContained = true;
            }
        }

        return isContained;
    }
}
