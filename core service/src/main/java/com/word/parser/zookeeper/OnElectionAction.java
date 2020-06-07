package com.word.parser.zookeeper;

import com.word.parser.articleextraction.adapter.in.web.ArticleExtractionController;
import org.apache.zookeeper.KeeperException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OnElectionAction implements OnElectionCallback {
    private final ServiceRegistry workderServiceRegistry;
    private final ServiceRegistry coordinatorServiceRegistry;
    private final int port;

    public OnElectionAction(ServiceRegistry workderServiceRegistry, ServiceRegistry coordinatorServiceRegistry, int port) {
        this.workderServiceRegistry = workderServiceRegistry;
        this.coordinatorServiceRegistry = coordinatorServiceRegistry;
        this.port = port;
    }

    @Override
    public void onLeaderElection() {
        workderServiceRegistry.unregisterNode();
        workderServiceRegistry.registerForUpdate();

        try {
            String currentServerAddress =
                    String.format("http://%s:%d%s", InetAddress.getLocalHost().getCanonicalHostName(), port,
                            ArticleExtractionController.EXTRACT_LINK);
            coordinatorServiceRegistry.registerToCluster(currentServerAddress);
        } catch (UnknownHostException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWorkerElection() {
        try {
            String currentServerAddress =
                    String.format("http://%s:%d%s", InetAddress.getLocalHost().getCanonicalHostName(), port,
                            ArticleExtractionController.EXTRACT_ARTICLE);
            workderServiceRegistry.registerToCluster(currentServerAddress);
        } catch (UnknownHostException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
