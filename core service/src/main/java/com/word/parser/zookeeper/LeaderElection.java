package com.word.parser.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

public class LeaderElection implements Watcher {
    private static final String ELECTION_PREFIX = "/election";
    private ZooKeeper zooKeeper;
    private String currentZNodeName;
    private OnElectionCallback onElectionCallback;

    public LeaderElection(ZooKeeper zooKeeper, OnElectionCallback onElectionCallback) {
        this.onElectionCallback = onElectionCallback;
        this.zooKeeper = zooKeeper;
    }
    public void participateInElection() throws KeeperException, InterruptedException {
        String name = ELECTION_PREFIX+"/c_";
        String znodeFullPath = zooKeeper.create(name,new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("znode name: "+znodeFullPath);
        this.currentZNodeName = znodeFullPath.replace(ELECTION_PREFIX+"/","");
    }


    public void electLeader() throws KeeperException, InterruptedException {
        Stat predecessorStat = null;
        List<String> participants = zooKeeper.getChildren(ELECTION_PREFIX,false);
        Collections.sort(participants);
        while (predecessorStat==null) {
            if (participants.get(0).equals(currentZNodeName)) {
                System.out.println("I am the leader :" + currentZNodeName);
                onElectionCallback.onLeaderElection();
                return;
            } else {
                int predecessorIndex = Collections.binarySearch(participants, currentZNodeName) - 1;
                String predecessorName = participants.get(predecessorIndex);
                System.out.println("Watching :" + predecessorName);
                predecessorStat = zooKeeper.exists(ELECTION_PREFIX + "/" + predecessorName, this);
            }
        }
        onElectionCallback.onWorkerElection();
    }

    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()){
            case NodeDeleted:
                try {
                    electLeader();
                } catch (InterruptedException | KeeperException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
