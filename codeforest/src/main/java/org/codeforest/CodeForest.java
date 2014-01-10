package org.codeforest;

import java.awt.GraphicsConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.codeforest.model.VertexSceneContext;
import org.codeforest.scenegraph.BoxTreeLayout;
import org.codeforest.scenegraph.EdgeNodeFactory;
import org.codeforest.scenegraph.LineEdgeFactory;
import org.codeforest.scenegraph.TableLayout;
import org.codeforest.scenegraph.TreePlanter;
import org.codeforest.scenegraph.TreeWidthCalculator;
import org.codeforest.scenegraph.VertexNodeFactory;
import org.codeforest.scenegraph.VertexTreeSceneBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.bytecode.JarFileLoader;
import org.javaan.graph.VertexEdge;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Package;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.EdgeReversedGraph;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * First test: Display a simple JGraph
 */
public class CodeForest extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	private SimpleUniverse univ = null;
	
	private BranchGroup scene = null;

	private javax.swing.JPanel drawingPanel;

	public CodeForest(String[] fileNames) throws IOException {
		// Initialize the GUI components
		initComponents();
		
		// create class context
		ClassContext classContext = createClassContext(fileNames);

		// Create Canvas3D and SimpleUniverse; add canvas to drawing panel
		Canvas3D c = createUniverse();
		drawingPanel.add(c, java.awt.BorderLayout.CENTER);

		// Create the content branch and add it to the universe
		scene = createSceneGraph(classContext);
		univ.addBranchGraph(scene);

		OrbitBehavior orbit = new OrbitBehavior(c);
		orbit.setSchedulingBounds(new BoundingSphere(
				new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
		univ.getViewingPlatform().setViewPlatformBehavior(orbit);
	}
	
	private ClassContext createClassContext(String[] fileNames) throws IOException {
		List<org.javaan.model.Type> types = new JarFileLoader().loadJavaClasses(fileNames);
		return new ClassContextBuilder(types).build();
	}
	
	private List<Package> getNoneEmptyPackages(ClassContext classContext) {
		List<Package> packages = new ArrayList<Package>();
		for (Package package1 : classContext.getPackages()) {
			if (classContext.getClassesOfPackage(package1).size() > 0) {
				packages.add(package1);
			}
		}
		Collections.sort(packages);
		return packages;
	}

	private BranchGroup createSceneGraph(ClassContext classContext) {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		
		// retrieve all classes which extend Object
		Set<Clazz> rootClasses = classContext.getDirectSpecializationsOfClass(new Clazz("java.lang.Object"));
		DirectedGraph<Clazz, VertexEdge<Clazz>> specializationClasses = new EdgeReversedGraph<>(classContext.getSuperClassGraph());
		
		// build layout context
		VertexSceneContext<Clazz> context = new VertexSceneContext<Clazz>();

		// calculate vertex widths and row from package
		List<Package> packages = getNoneEmptyPackages(classContext);
		TreeWidthCalculator<Clazz, VertexEdge<Clazz>> treeWidthCalculator = 
				new TreeWidthCalculator<Clazz, VertexEdge<Clazz>>(context, specializationClasses);
		for (Clazz clazz : rootClasses) {
			treeWidthCalculator.calculateVertexWidth(clazz);
			int packageIndex = packages.indexOf(classContext.getPackageOfType(clazz));
			context.get(clazz).setRow(packageIndex);
		}
		
		VertexNodeFactory<Clazz> shapeFactory = new VertexNodeFactory<Clazz>() {
			public Node createNode(Clazz vertex) {
				return new ColorCube(0.4);
			}
		};
		EdgeNodeFactory<Clazz, VertexEdge<Clazz>> edgeNodeFactory = new LineEdgeFactory<Clazz, VertexEdge<Clazz>>();
		VertexTreeSceneBuilder<Clazz, VertexEdge<Clazz>> treeBuilder = new VertexTreeSceneBuilder<Clazz, VertexEdge<Clazz>>(
				context, specializationClasses, shapeFactory, edgeNodeFactory, new BoxTreeLayout<Clazz>(context, 2d, 3d));
		
		TreePlanter<Clazz> planter = new TreePlanter<Clazz>(context, treeBuilder, new TableLayout<Clazz>(context, 2d, 10d, 2d));
		TransformGroup transformGroup = planter.createScene(rootClasses);
		objRoot.addChild(transformGroup);

		// Have Java 3D perform optimizations on this scene graph.
		objRoot.compile();

		return objRoot;
	}

	private Canvas3D createUniverse() {
		// Get the preferred graphics configuration for the default screen
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		// Create a Canvas3D using the preferred configuration
		Canvas3D c = new Canvas3D(config);

		// Create simple universe with view branch
		univ = new SimpleUniverse(c);

		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		ViewingPlatform viewingPlatform = univ.getViewingPlatform();

		TransformGroup viewTransform = viewingPlatform
				.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		t3d.lookAt(new Point3d(-100, 100, 500), new Point3d(0, 100, 0), new Vector3d(
				0, 1, 0));
		t3d.invert();
		viewTransform.setTransform(t3d);
		View view = univ.getViewer().getView();
		view.setBackClipDistance(1000.0);
		view.setMinimumFrameCycleTime(5);

		return c;
	}

	private void initComponents() {
		drawingPanel = new javax.swing.JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("HelloUniverse");
		drawingPanel.setLayout(new java.awt.BorderLayout());

		drawingPanel.setPreferredSize(new java.awt.Dimension(500, 500));
		getContentPane().add(drawingPanel, java.awt.BorderLayout.CENTER);

		pack();
	}

	/**
	 * @param args List of jar files
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new CodeForest(args).setVisible(true);
				} catch (IOException e) {
					System.out.println("Could not load libraries: " +  e);
				}
			}
		});
	}

}
