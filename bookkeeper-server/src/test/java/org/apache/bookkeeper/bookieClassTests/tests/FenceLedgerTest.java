package org.apache.bookkeeper.bookieClassTests.tests;

import org.apache.bookkeeper.bookieClassTests.Utils.BookKeeperClusterTestCase;
import org.apache.bookkeeper.bookieClassTests.entity.FenceLedgerAux;
import com.google.common.util.concurrent.SettableFuture;
import org.apache.bookkeeper.bookie.Bookie;
import org.apache.bookkeeper.bookie.BookieException;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(value = Parameterized.class)
public class FenceLedgerTest extends BookKeeperClusterTestCase {

    private FenceLedgerAux testEntity;

    private static Bookie bookie;

    public FenceLedgerTest(FenceLedgerAux testEntity) {
        super(3);
        this.testEntity = testEntity;
    }

    private static Long ledgerID = 2334L;

    @Parameterized.Parameters
    public  static Collection<FenceLedgerAux> getParameters() {

        return Arrays.asList(new FenceLedgerAux(-1L,null,false),
                new FenceLedgerAux(0L,new byte[]{},true),
                new FenceLedgerAux(1L,"masterKey".getBytes(),true));
                // WatchOut: even if 0, and 1 ledger's id are not real,
                // as per the implementation at class HandleFactoryImpl
                // where calling getHandle with a invalid ID it just create's it
    }



    @Before
    public void setUp() throws Exception {
        super.setUp();

        bookie = bs.get(0).getBookie();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMethod() {
        Boolean expected = testEntity.getExpectedResult();
        Boolean result;
        Long value = testEntity.getLedgerID();
        byte[] masterKey = testEntity.getMasterKey();
        try {
            SettableFuture<Boolean> test = bookie.fenceLedger(value,masterKey);
            Boolean futureBoolean = test.get(1L, TimeUnit.SECONDS);
            if(Boolean.TRUE.equals(futureBoolean)){
                result = true;
            }else{
                result = false;
            }
        } catch (IOException | BookieException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertEquals(expected,result);
    }

}
