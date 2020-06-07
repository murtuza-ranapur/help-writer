package com.word.parser.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceRegistry implements Watcher {
    public static final String WORKER_REGISTRY_ZNODE = "/service_registry";
    public static final String COORDINATOR_REGISTRY_ZNODE = "/coor_registry";
    private final ZooKeeper zooKeeper;
    private final String znode;
    private String currentZnode = null;
    private List<String> allServiceAddresses = null;

    public ServiceRegistry(ZooKeeper zooKeeper, String znode) {
        this.zooKeeper = zooKeeper;
        this.znode = znode;
        createServiceRegistryZnode();
    }

    public void registerToCluster(String metadata) throws KeeperException, InterruptedException {
        this.currentZnode = zooKeeper.create(znode +"/n_", metadata.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    private void createServiceRegistryZnode() {
        try {
            if (zooKeeper.exists(znode, false) == null){
                zooKeeper.create(znode, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registerForUpdate(){
        try {
            updateAddresses();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String> getAllServiceAddresses() throws KeeperException, InterruptedException {
        if(allServiceAddresses==null){
            updateAddresses();
        }
        return allServiceAddresses;
    }

    public void unregisterNode() {
        try {
            if(currentZnode!=null && zooKeeper.exists(currentZnode,false)!=null){
                zooKeeper.delete(currentZnode,-1);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateAddresses() throws KeeperException, InterruptedException {
        List<String> nodes = zooKeeper.getChildren(znode,this);
        List<String> addresses = new ArrayList<>();

        for (String znode : nodes) {
            String workerPath = this.znode +"/"+znode;
            Stat stat = zooKeeper.exists(workerPath,false);
            if(stat == null){
                continue;
            }
            byte [] address = zooKeeper.getData(workerPath,false,stat);
            addresses.add(new String(address));
        }
        this.allServiceAddresses = Collections.unmodifiableList(addresses);
        System.out.println("Cluster addresses are :"+this.allServiceAddresses);
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            updateAddresses();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
