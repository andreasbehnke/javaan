package org.codeforest;

import java.awt.GraphicsConfiguration;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.codeforest.layout.BoxTreeLayout;
import org.codeforest.layout.TableLayout;
import org.codeforest.layout.TreeWidthCalculator;
import org.codeforest.layout.VertexSceneContext;
import org.codeforest.scenegraph.EdgeNodeFactory;
import org.codeforest.scenegraph.LineEdgeFactory;
import org.codeforest.scenegraph.TreePlanter;
import org.codeforest.scenegraph.VertexNodeConnector;
import org.codeforest.scenegraph.VertexNodeFactory;
import org.codeforest.scenegraph.VertexTreeSceneBuilder;
import org.javaan.bytecode.CallGraphBuilder;
import org.javaan.bytecode.ClassContextBuilder;
import org.javaan.bytecode.JarFileLoader;
import org.javaan.graph.UnsupportedEdgeFactory;
import org.javaan.graph.VertexEdge;
import org.javaan.model.CallGraph;
import org.javaan.model.ClassContext;
import org.javaan.model.Clazz;
import org.javaan.model.Dependency;
import org.javaan.model.Package;
import org.javaan.model.Type.JavaType;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
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

	private SimpleUniverse univ;
	
	private BranchGroup scene;
	
	private BranchGroup objRoot;
	
	private BranchGroup callGraphBranchGroup;

	private javax.swing.JPanel drawingPanel;

	public CodeForest(String[] fileNames) throws IOException {
		// Initialize the GUI components
		initComponents();
		
		// create class context
		List<org.javaan.model.Type> types = new JarFileLoader().loadJavaClasses(fileNames);
		ClassContext classContext = new ClassContextBuilder(types).build();
		CallGraph callGraph = new CallGraphBuilder(classContext, true, false).build();

		// Create Canvas3D and SimpleUniverse; add canvas to drawing panel
		final Canvas3D c = createUniverse();
		drawingPanel.add(c, java.awt.BorderLayout.CENTER);

		// Create the content branch and add it to the universe
		scene = createSceneGraph(classContext, callGraph);
		scene.compile();
		univ.addBranchGraph(scene);

		final OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_TRANSLATE);
		orbit.setZoomFactor(10d);
		orbit.setTransFactors(10d, 10d);
		orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE));
		univ.getViewingPlatform().setViewPlatformBehavior(orbit);
		c.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 'c') {
					if (callGraphBranchGroup.getParent() == null) {
						scene.addChild(callGraphBranchGroup);
					} else {
						callGraphBranchGroup.detach();
					}
				}
			}
		});
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
	
	private Appearance createInheritanceAppearance() {
		Appearance appearance = new Appearance();
		ColoringAttributes coloringAttributes = new  ColoringAttributes(new Color3f(1,0,0), ColoringAttributes.NICEST);
		appearance.setColoringAttributes(coloringAttributes);;
		return appearance;
	}
	
	private Appearance createUsageAppearance() {
		Appearance appearance = new Appearance();
		ColoringAttributes coloringAttributes = new  ColoringAttributes(new Color3f(0,0,1), ColoringAttributes.NICEST);
		appearance.setColoringAttributes(coloringAttributes);;
		return appearance;
	}
	
	private DirectedGraph<Clazz, Dependency> filterClasses(DirectedGraph<org.javaan.model.Type, Dependency> graph) {
		DirectedGraph<Clazz, Dependency> classDependencyGraph = new DefaultDirectedGraph<>(new UnsupportedEdgeFactory<Clazz, Dependency>());
		for (org.javaan.model.Type type : graph.vertexSet()) {
			if (type.getJavaType() == JavaType.CLASS) {
				classDependencyGraph.addVertex((Clazz)type);
			}
		}
		for (Dependency dependency : graph.edgeSet()) {
			org.javaan.model.Type source = graph.getEdgeSource(dependency);
			org.javaan.model.Type target = graph.getEdgeTarget(dependency);
			if (source.getJavaType() == JavaType.CLASS && target.getJavaType() == JavaType.CLASS) {
				classDependencyGraph.addEdge((Clazz)source, (Clazz)target, dependency);
			}
		}
		return classDependencyGraph;
	}

	private BranchGroup createSceneGraph(ClassContext classContext, CallGraph callGraph) {
		DirectedGraph<Clazz, Dependency> condensedCallGraph = filterClasses(callGraph.getInternalGraphs().getUsageOfTypeGraph());

		// Create the root of the branch graph
		objRoot = new BranchGroup();
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		// retrieve all classes which extend Object
		Set<Clazz> rootClasses = classContext.getDirectSpecializationsOfClass(new Clazz("java.lang.Object"));
		DirectedGraph<Clazz, VertexEdge<Clazz>> specializationClasses = new EdgeReversedGraph<>(classContext.getInternalGraphs().getSuperClassGraph());
		
		// build layout context
		VertexSceneContext<Clazz> context = new VertexSceneContext<>();

		// calculate vertex widths and row from package
		List<Package> packages = getNoneEmptyPackages(classContext);
		TreeWidthCalculator<Clazz, VertexEdge<Clazz>> treeWidthCalculator = new TreeWidthCalculator<>(context, specializationClasses);
		for (Clazz clazz : rootClasses) {
			treeWidthCalculator.calculateVertexWidth(clazz);
			int packageIndex = packages.indexOf(classContext.getPackageOfType(clazz));
			context.get(clazz).setRow(packageIndex);
		}
		
		// create factory for class nodes
		VertexNodeFactory<Clazz> shapeFactory = new VertexNodeFactory<Clazz>() {
			public Node createNode(Clazz vertex) {
				return new ColorCube(0.4);
			}
		};
		
		// create factory for inheritance edges
		EdgeNodeFactory<Clazz, VertexEdge<Clazz>> inheritanceEdgeNodeFactory = new LineEdgeFactory<>(createInheritanceAppearance());
				
		VertexTreeSceneBuilder<Clazz, VertexEdge<Clazz>> treeBuilder = new VertexTreeSceneBuilder<Clazz, VertexEdge<Clazz>>(
				context, specializationClasses, shapeFactory, inheritanceEdgeNodeFactory, new BoxTreeLayout<Clazz>(context, 2d, 3d));
		
		TreePlanter<Clazz> planter = new TreePlanter<Clazz>(context, treeBuilder, new TableLayout<Clazz>(context, 2d, 10d, 2d));
		TransformGroup transformGroup = planter.createScene(rootClasses);
		objRoot.addChild(transformGroup);
		
		EdgeNodeFactory<Clazz, Dependency> usageEdgeNodeFactory = new LineEdgeFactory<>(createUsageAppearance());
		VertexNodeConnector<Clazz, Dependency> connector = new VertexNodeConnector<>(context, usageEdgeNodeFactory);
		callGraphBranchGroup = new BranchGroup();
		callGraphBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		callGraphBranchGroup.addChild(connector.createScene(condensedCallGraph));
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
