package org.javaan;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

import org.javaan.commands.StandardOptions;

public class Settings {
	
	private static final String GRAPH2D_FRAME_HEIGHT_KEY = "GRAPH2D_FRAME_HEIGHT";
	
	private static final String GRAPH2D_FRAME_WIDTH_KEY = "GRAPH2D_FRAME_WIDTH";
	
	private static final String GRAPH2D_FRAME_X_KEY = "GRAPH2D_FRAME_X";
	
	private static final String GRAPH2D_FRAME_Y_KEY = "GRAPH2D_FRAME_Y";
	
	private final Preferences preferences;
	
	public Settings() {
		preferences  = Preferences.userNodeForPackage(JavaanCli.class);
	}
	
	/**
	 * used for unit tests
	 */
	public Settings(String pathName) {
		preferences = Preferences.userRoot().node(pathName);
	}
	
	public void setGraph2dFrameLocationAndSize(JFrame frame) {
		int width = preferences.getInt(GRAPH2D_FRAME_WIDTH_KEY, 450);
        int height = preferences.getInt(GRAPH2D_FRAME_HEIGHT_KEY, 350);
        frame.setSize(width, height);
        int x = preferences.getInt(GRAPH2D_FRAME_X_KEY, 0);
        int y = preferences.getInt(GRAPH2D_FRAME_Y_KEY, 0);
        frame.setLocation(x, y);
	}
	
	public void putGraph2dFrameLocationAndSize(JFrame frame) {
		preferences.putInt(GRAPH2D_FRAME_WIDTH_KEY, frame.getWidth());
		preferences.putInt(GRAPH2D_FRAME_HEIGHT_KEY, frame.getHeight());
		preferences.putInt(GRAPH2D_FRAME_X_KEY, frame.getLocation().x);
		preferences.putInt(GRAPH2D_FRAME_Y_KEY, frame.getLocation().y);
	}
	
	public void enableOption(String optionName) {
		preferences.putBoolean(optionName, true);
	}
	
	public boolean isResolveDependenciesInClassHierarchy() {
		return preferences.getBoolean(StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY, false);
	}
	
	public boolean isResolveMethodImplementations() {
		return preferences.getBoolean(StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS, false);
	}

	public boolean isDisplay2dGraph() {
		return preferences.getBoolean(StandardOptions.OPT_DISPLAY_2D_GRAPH, false);
	}
}
