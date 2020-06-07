package com.word.parser.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ZookeeperConfiguration {
    // Inject which port we were assigned
    @Value("${server.port}")
    private int port;

    @Bean
    public ZooKeeper zooKeeper(){
        ZookeeperApplication application = new ZookeeperApplication();
        try {
            return application.connectToZookeeper();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Qualifier("master")
    @Bean
    public ServiceRegistry master(ZooKeeper zooKeeper){
        return new ServiceRegistry(zooKeeper, ServiceRegistry.COORDINATOR_REGISTRY_ZNODE);
    }

    @Qualifier("slave")
    @Bean
    public ServiceRegistry slave(ZooKeeper zooKeeper){
        return new ServiceRegistry(zooKeeper, ServiceRegistry.WORKER_REGISTRY_ZNODE);
    }

    @Bean
    public LeaderElection leaderElection(@Qualifier("master") ServiceRegistry master,
                                         @Qualifier("slave") ServiceRegistry slave,
                                         ZooKeeper zooKeeper){
        OnElectionCallback onElectionCallback = new OnElectionAction(slave, master, port);

        LeaderElection leaderElection = new LeaderElection(zooKeeper,onElectionCallback);
        try {
            leaderElection.participateInElection();
            leaderElection.electLeader();
            return leaderElection;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


}
