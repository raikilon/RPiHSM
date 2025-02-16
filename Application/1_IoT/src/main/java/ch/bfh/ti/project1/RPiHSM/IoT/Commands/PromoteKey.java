package ch.bfh.ti.project1.RPiHSM.IoT.Commands;

import ch.bfh.ti.project1.RPiHSM.IoT.Utils.Constants;
import ch.bfh.ti.project1.RPiHSM.IoT.Utils.JSONUtils;
import org.keyczar.KeyczarTool;

/**
 * <h1>PromoteKey</h1>
 * Promotes a key by using the <a href="https://github.com/google/keyczar/wiki/KeyczarTool">KeyczarTool</a>.
 */
public class PromoteKey implements CommandI {

    private SerialHelperI serialHelper;
    private String keyPath;
    private int version;

    /**
     * Parses the received parameters.
     *
     * @param commands  parameters for the key promotion ([0] userPath, [1] name, [2] version)
     * @param serialHelper an instance of {@link SerialHelperI}
     */
    public PromoteKey(String[] commands, SerialHelperI serialHelper) {
        this.serialHelper = serialHelper;
        keyPath = commands[0] + Constants.DIRECTORY + commands[1];
        version = Integer.parseInt(commands[2]);
    }


    /**
     * Promotes a key in a given key set using the <a href="https://github.com/google/keyczar/wiki/KeyczarTool">KeyczarTool</a>.
     * If the status of the key is INACTIVE or ACTIVE the {@link SerialHelperI#ready()} is called otherwise {@link SerialHelperI#error()}.
     * Prerequisites: the given key set and key version must exist.
     */
    @Override
    public void execute() {

        if (JSONUtils.checkKeySetStatus(keyPath, version, Constants.ACTIVE, Constants.INACTIVE)) {
            KeyczarTool.main(new String[]{Constants.PROMOTE, "--location=" + keyPath, "--version=" + version});
            serialHelper.ready();
        } else
            serialHelper.error();
    }
}