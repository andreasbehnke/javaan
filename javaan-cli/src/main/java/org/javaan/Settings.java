package org.javaan;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

import org.javaan.commands.StandardOptions;

public class Settings {
	
	private static final String GRAPH2D_FRAME_HEIGHT_KEY = "GRAPH2D_FRAME_HEIGHT";
	
	private static final String GRAPH2D_FRAME_WIDTH_KEY = "GRAPH2D_FRAME_WIDTH";
	
	private static final String GRAPH2D_FRAME_X_KEY = "GRAPH2D_FRAME_X";
	
	private static final String GRAPH2D_FRAME_Y_KEY = "GRAPH2D_FRAME_Y";
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(JavaanCli.class);
	
	public static void setGraph2dFrameLocationAndSize(JFrame frame) {
		int width = PREFERENCES.getInt(GRAPH2D_FRAME_WIDTH_KEY, 450);
        int height = PREFERENCES.getInt(GRAPH2D_FRAME_HEIGHT_KEY, 350);
        frame.setSize(width, height);
        int x = PREFERENCES.getInt(GRAPH2D_FRAME_X_KEY, 0);
        int y = PREFERENCES.getInt(GRAPH2D_FRAME_Y_KEY, 0);
        frame.setLocation(x, y);
	}
	
	public static void putGraph2dFrameLocationAndSize(JFrame frame) {
		PREFERENCES.putInt(GRAPH2D_FRAME_WIDTH_KEY, frame.getWidth());
		PREFERENCES.putInt(GRAPH2D_FRAME_HEIGHT_KEY, frame.getHeight());
		PREFERENCES.putInt(GRAPH2D_FRAME_X_KEY, frame.getLocation().x);
		PREFERENCES.putInt(GRAPH2D_FRAME_Y_KEY, frame.getLocation().y);
	}
	
	public static boolean isResolveDependenciesInClassHierarchy() {
		return PREFERENCES.getBoolean(StandardOptions.OPT_RESOLVE_DEPENDENCIES_IN_CLASS_HIERARCHY, false);
	}
	
	public static boolean isResolveMethodImplementations() {
		return PREFERENCES.getBoolean(StandardOptions.OPT_RESOLVE_METHOD_IMPLEMENTATIONS, false);
	}

	public static boolean isDisplay2dGraph() {
		return PREFERENCES.getBoolean(StandardOptions.OPT_DISPLAY_2D_GRAPH, false);
	}
}
