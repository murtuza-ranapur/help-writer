package com.word.parser.zookeeper;

public interface OnElectionCallback {

    void onLeaderElection();

    void onWorkerElection();

}
