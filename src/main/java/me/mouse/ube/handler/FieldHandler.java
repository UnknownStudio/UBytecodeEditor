package me.mouse.ube.handler;

import static me.mouse.ube.BytecodeUtils.getAccess;

import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.util.TraceSignatureVisitor;

import javafx.scene.control.TreeItem;
import me.mouse.ube.BytecodeUtils;
import me.mouse.ube.warpper.FieldNodeWarpper;

public class FieldHandler implements BytecodeHandler<FieldNodeWarpper> {

	@Override
	public String getText(FieldNodeWarpper n) {
		StringBuilder sb = new StringBuilder();

		sb.append(getAccess(n.node.access));

		TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
		SignatureReader r = new SignatureReader(n.node.desc);
		r.acceptType(sv);
		sb.append(sv.getDeclaration()).append(" ");

		sb.append(n.node.name);
		if (n.node.value != null) {
			sb.append(" = ");
			if (n.node.value instanceof String) {
				sb.append('\"').append(n.node.value).append('\"');
			} else {
				sb.append(n.node.value);
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public TreeItem<?> getNode(FieldNodeWarpper node) {
		TreeItem<?> item = new TreeItem<>(node);
		if(node.node.visibleAnnotations!=null)
			node.node.visibleAnnotations.stream().forEach(i->item.getChildren().add(
					BytecodeUtils.getBytecodeHandler(i.getClass()).impl_getNode(i)));
		if(node.node.visibleTypeAnnotations!=null)
			node.node.visibleTypeAnnotations.stream().forEach(i->item.getChildren().add(
					BytecodeUtils.getBytecodeHandler(i.getClass()).impl_getNode(i)));
		return item;
	}
}
