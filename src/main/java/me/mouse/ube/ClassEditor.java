package me.mouse.ube;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import me.mouse.ube.warpper.ClassNodeWarpper;

public final class ClassEditor extends AnchorPane {

	@FXML
	protected TreeView<Object> treeView;
	@FXML
	protected TextArea textArea;

	private File classFile;
	private ClassNode classNode;

	public ClassEditor() {
		FXMLLoader loader = new FXMLLoader(ClassEditor.class.getResource("ClassEditor.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		loader.setCharset(StandardCharsets.UTF_8);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ClassEditor(File classFile) {
		this();

		this.classFile = classFile;
		try {
			classNode = loadBytecode(classFile);
			showASM();
			updateTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ClassNode loadBytecode(File file) throws IOException {
		InputStream is = new FileInputStream(classFile);
		ClassReader cr = new ClassReader(is);
		is.close();
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, 0);
		return classNode;
	}

	public boolean saveBytecode() {
		try {
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			classNode.accept(cw);
			FileOutputStream output = new FileOutputStream(classFile);
			output.write(cw.toByteArray());
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void showASM() {
		TraceClassVisitor cv = new TraceClassVisitor(null, new Textifier(), null);
		classNode.accept(cv);
		textArea.clear();
		textArea.setText(formatASMText(cv.p.text));
	}

	public void showASMSource() {
		TraceClassVisitor cv = new TraceClassVisitor(null, new ASMifier(), null);
		classNode.accept(cv);
		textArea.clear();
		textArea.setText(formatASMText(cv.p.text));
	}

	private String formatASMText(List<?> text) {
		StringBuilder sb = new StringBuilder();
		text.stream().forEach(i -> {
			if (i instanceof String)
				sb.append(i);
			else if (i instanceof List)
				sb.append(formatASMText((List<?>) i));
		});
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private void updateTree() {
		treeView.setCellFactory(param -> new BytecodeTreeCell());
		treeView.setRoot(BytecodeUtils.getBytecodeHandler(ClassNodeWarpper.class).impl_getNode(new ClassNodeWarpper(classNode)));
	}
}
