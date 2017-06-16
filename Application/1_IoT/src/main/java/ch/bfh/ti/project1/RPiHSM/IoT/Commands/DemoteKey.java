package ch.bfh.ti.project1.RPiHSM.IoT.Commands;

import ch.bfh.ti.project1.RPiHSM.IoT.Utils.Constants;
import ch.bfh.ti.project1.RPiHSM.IoT.Utils.JSONUtils;
import org.keyczar.KeyczarTool;

/**
 * <h1>DemoteKey</h1>
 * Demotes a key by using the <a href="https://github.com/google/keyczar/wiki/KeyczarTool">KeyczarTool</a>.
 */
public class DemoteKey implements CommandI {

    private SerialHelperI serialHelper;
    private String keyPath;
    private int version;

    /**
     * Parses the received parameters.
     *
     * @param commands  parameters for the key demotion ([0] userPath, [1] name, [2] version)
     * @param serialHelper an instance of {@link SerialHelperI}
     */
    public DemoteKey(String[] commands, SerialHelperI serialHelper) {
        keyPath = commands[0] + Constants.DIRECTORY + commands[1];
        version = Integer.parseInt(commands[2]);
        this.serialHelper = serialHelper;
    }


    /**
     * Demotes a key in a given key set using the <a href="https://github.com/google/keyczar/wiki/KeyczarTool">KeyczarTool</a>.
     * If the status of the key is PRIMARY or ACTIVE the {@link SerialHelperI#ready()} is called otherwise {@link SerialHelperI#error()}.
     * Prerequisites: the given key set and key version must exist.
     */
    @Override
    public void execute() {

        if (JSONUtils.checkKeySetStatus(keyPath, version, Constants.PRIMARY, Constants.ACTIVE)) {

            KeyczarTool.main(new String[]{Constants.DEMOTE, "--location=" + keyPath, "--version=" + version});

            serialHelper.ready();
        } else {
            serialHelper.error();
        }
    }
}