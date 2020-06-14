package org.apache.bookkeeper.bookieClassTests.entity;

import org.apache.bookkeeper.bookie.LedgerStorage;
import org.apache.bookkeeper.conf.ServerConfiguration;

public class MountLedgerStorageOfflineAux {

    private Boolean conf;
    private Boolean ledgerStorage;
    private Boolean expectedValue;
    private Boolean jacoco;

    public Boolean getJacoco() {
        return jacoco;
    }

    public void setJacoco(Boolean jacoco) {
        this.jacoco = jacoco;
    }

    public Boolean getConf() {
        return conf;
    }

    public void setConf(Boolean conf) {
        this.conf = conf;
    }

    public Boolean getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(Boolean expectedValue) {
        this.expectedValue = expectedValue;
    }

    public Boolean getLedgerStorage() {
        return ledgerStorage;
    }

    public void setLedgerStorage(Boolean ledgerStorage) {
        this.ledgerStorage = ledgerStorage;
    }

    public MountLedgerStorageOfflineAux(Boolean conf, Boolean ledgerStorage, Boolean expectedValue, Boolean jacoco) {
        this.conf = conf;
        this.ledgerStorage = ledgerStorage;
        this.expectedValue = expectedValue;
        this.jacoco = jacoco;
    }


}
