package net.sapfii.modutilities.features.interfaces;

public interface CommandListeningFeature {

    CommandResult onCommand(String command);

    enum CommandResult {
        PASS(false), CANCEL(true);
        final boolean value;
        CommandResult(boolean value) { this.value = value; }
        public boolean Value() { return this.value; }
    }
}
