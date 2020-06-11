package org.apache.bookkeeper.bookieClassTests.tests;

import org.apache.bookkeeper.bookieClassTests.Utils.BookKeeperClusterTestCase;
import org.apache.bookkeeper.bookieClassTests.entity.MountLedgerStorageOfflineAux;
import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.*;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.apache.bookkeeper.meta.LedgerManager;
import org.apache.bookkeeper.stats.StatsLogger;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class MountLedgerStorageOfflineTest extends BookKeeperClusterTestCase {

    private MountLedgerStorageOfflineAux testEntity;

    public MountLedgerStorageOfflineTest(MountLedgerStorageOfflineAux testEntity) {
        super(3);
        this.testEntity = testEntity;
    }

    @Parameterized.Parameters
    public static Collection<MountLedgerStorageOfflineAux> getParameters() {
        return Arrays.asList(
                                                    // null, null,false
                new MountLedgerStorageOfflineAux(false, false, false),
                                                 // valid  , null ,true
                new MountLedgerStorageOfflineAux(true, false, true),
                                                // valid, valid, true
                new MountLedgerStorageOfflineAux(true, true, true)
                // new mountLedgerStorageOfflineAux(new ServerConfiguration(), null, true)r

        );
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        File test = new File("/tmp/bk-data/current");
        test.mkdirs();


    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File("/tmp/bk-data/"));

    }

    @Test
    public void testMethod() {
        Boolean result = false;
        Boolean expected = testEntity.getExpectedValue();

        LedgerStorage ledgerStorage = bs.get(0).getBookie().getLedgerStorage();
        LedgerStorage res;
        ServerConfiguration conf = bsConfs.get(0);
        try {
            if (testEntity.getLedgerStorage()) {

                res = Bookie.mountLedgerStorageOffline(conf, ledgerStorage);
            } else {
                if(!testEntity.getConf()){
                    res = Bookie.mountLedgerStorageOffline(null, ledgerStorage);
                }else {
                    res = Bookie.mountLedgerStorageOffline(conf, ledgerStorage);
                }
            }
                result = true;
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            result = false;
        }

        Assert.assertEquals(expected, result);


    }

}
