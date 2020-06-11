package org.apache.bookkeeper.bookieClassTests.entity;

public class FenceLedgerAux {
    private Long ledgerID;
    private byte[] masterKey;
    private Boolean expectedResult;

    public FenceLedgerAux(Long ledgerID, byte[] masterKey, Boolean expectedResult) {
        this.ledgerID = ledgerID;
        this.masterKey = masterKey;
        this.expectedResult = expectedResult;
    }

    public Long getLedgerID() {
        return ledgerID;
    }

    public void setLedgerID(Long ledgerID) {
        this.ledgerID = ledgerID;
    }

    public byte[] getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(byte[] masterKey) {
        this.masterKey = masterKey;
    }

    public Boolean getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(Boolean expectedResult) {
        this.expectedResult = expectedResult;
    }
}
